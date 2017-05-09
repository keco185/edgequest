/* Responsible for making sure terrain exists. If it doesn't it tells
 * the terrain generator to create it.
 */
package com.mtautumn.edgequest.threads;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.generator.TerrainGenerator;
import com.mtautumn.edgequest.generator.TerrainGeneratorThread;

public class TerrainManager extends Thread {
	public TerrainGenerator terrainGenerator;
	public TerrainManager() {
		terrainGenerator = new TerrainGenerator();
	}
	@Override
	public void run() {
		while (SystemData.running) {
			try {
				if (!SystemData.isGameOnLaunchScreen) {
					ArrayList<int[]> regionsToGen = new ArrayList<int[]>();
					ArrayList<TerrainGeneratorThread> threads = new ArrayList<TerrainGeneratorThread>();
					int minX = (SystemData.minTileX < SystemData.minTileXGen) ? SystemData.minTileX : SystemData.minTileXGen;
					int maxX = (SystemData.maxTileX > SystemData.maxTileXGen) ? SystemData.maxTileX : SystemData.maxTileXGen;
					int minY = (SystemData.minTileY < SystemData.minTileYGen) ? SystemData.minTileY : SystemData.minTileYGen;
					int maxY = (SystemData.maxTileY > SystemData.maxTileYGen) ? SystemData.maxTileY : SystemData.maxTileYGen;
					for (int i = (int) (Math.floor((minX - 20.0) / 100.0)*100); i <=  maxX + 20; i+= 100) {
						for (int j = (int) (Math.floor((minY - 20.0) / 100.0)*100); j <=  maxY + 20; j+= 100) {
							regionsToGen.add(new int[]{i,j});
						}
					}
					for (int i = 0; i < regionsToGen.size(); i++) {
						if (!DataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + DataManager.savable.dungeonLevel) || !DataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + (DataManager.savable.dungeonLevel+1)) || (!DataManager.savable.generatedRegions.contains(regionsToGen.get(i)[0] + "," + regionsToGen.get(i)[1] + "," + (DataManager.savable.dungeonLevel - 1)) && DataManager.savable.dungeonLevel > -1)) {
						TerrainGeneratorThread tgt = new TerrainGeneratorThread(terrainGenerator, regionsToGen.get(i)[0], regionsToGen.get(i)[1], DataManager.savable.dungeonLevel);
						tgt.start();
						threads.add(tgt);
						}
					}
					boolean waiting = threads.size() > 0;
					if (waiting) {
						SystemData.blockGenerationLastTick = true;
					} else {
						SystemData.blockGenerationLastTick = false;
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
						SystemData.loadingWorld = false;
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
