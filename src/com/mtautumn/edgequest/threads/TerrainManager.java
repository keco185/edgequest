/* Responsible for making sure terrain exists. If it doesn't it tells
 * the terrain generator to create it.
 */
package com.mtautumn.edgequest.threads;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.generator.TerrainGenerator;
import com.mtautumn.edgequest.generator.TerrainGeneratorThread;

public class TerrainManager extends Thread {
	DataManager dataManager;
	public TerrainGenerator terrainGenerator;
	public TerrainManager(DataManager dataManager) {
		this.dataManager = dataManager;
		terrainGenerator = new TerrainGenerator(dataManager);
	}
	@Override
	public void run() {
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					ArrayList<int[]> regionsToGen = new ArrayList<int[]>();
					ArrayList<TerrainGeneratorThread> threads = new ArrayList<TerrainGeneratorThread>();
					int minX = (dataManager.system.minTileX < dataManager.system.minTileXGen) ? dataManager.system.minTileX : dataManager.system.minTileXGen;
					int maxX = (dataManager.system.maxTileX > dataManager.system.maxTileXGen) ? dataManager.system.maxTileX : dataManager.system.maxTileXGen;
					int minY = (dataManager.system.minTileY < dataManager.system.minTileYGen) ? dataManager.system.minTileY : dataManager.system.minTileYGen;
					int maxY = (dataManager.system.maxTileY > dataManager.system.maxTileYGen) ? dataManager.system.maxTileY : dataManager.system.maxTileYGen;
					for (int i = (int) (Math.floor((minX - 20.0) / 100.0)*100); i <=  maxX + 20; i+= 100) {
						for (int j = (int) (Math.floor((minY - 20.0) / 100.0)*100); j <=  maxY + 20; j+= 100) {
							regionsToGen.add(new int[]{i,j});
						}
					}
					for (int i = 0; i < regionsToGen.size(); i++) {
						if (!dataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + dataManager.savable.dungeonLevel) || !dataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + (dataManager.savable.dungeonLevel+1)) || (!dataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + (dataManager.savable.dungeonLevel - 1)) && dataManager.savable.dungeonLevel > -1)) {
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
