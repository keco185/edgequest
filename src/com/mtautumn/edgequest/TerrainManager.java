/* Responsible for making sure terrain exists. If it doesn't it tells
 * the terrain generator to create it.
 */
package com.mtautumn.edgequest;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainManager extends Thread {
	DataManager dataManager;
	public TerrainGenerator terrainGenerator;
	public TerrainManager(DataManager dataManager) {
		this.dataManager = dataManager;
		terrainGenerator = new TerrainGenerator(dataManager);
	}
	public void run() {
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					ArrayList<int[]> regionsToGen = new ArrayList<int[]>();
					ArrayList<TerrainGeneratorThread> threads = new ArrayList<TerrainGeneratorThread>();
					for (int i = (int) (Math.floor((dataManager.system.minTileXGen - 20.0) / 100.0)*100); i <=  dataManager.system.maxTileXGen + 20; i+= 100) {
						for (int j = (int) (Math.floor((dataManager.system.minTileYGen - 20.0) / 100.0)*100); j <=  dataManager.system.maxTileYGen + 20; j+= 100) {
							regionsToGen.add(new int[]{i,j});
						}
					}
					for (int i = 0; i < regionsToGen.size(); i++) {
						if (!dataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + dataManager.savable.dungeonLevel)) {
						TerrainGeneratorThread tgt = new TerrainGeneratorThread(terrainGenerator, dataManager, regionsToGen.get(i)[0], regionsToGen.get(i)[1], dataManager.savable.dungeonLevel);
						tgt.start();
						threads.add(tgt);
						}
					}
					boolean waiting = threads.size() > 0;
					if (waiting) {
						dataManager.system.blockGenerationLastTick = true;
					} else {
						dataManager.system.blockGenerationLastTick = false;
					}
					while (waiting) {
						boolean shouldWait = false;
						for (int i = 0; i < threads.size() && !shouldWait; i++) {
							if (threads.get(i).isAlive()) {
								shouldWait = true;
							}
						}
						waiting = shouldWait;
					}
					if (threads.size() == 0) {
						dataManager.system.loadingWorld = false;
					}
					Thread.sleep(1000);
				} else {
					Thread.sleep(100);
				}
			} catch (Exception e) {
				
			}
		}
	}
}
