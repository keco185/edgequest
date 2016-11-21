/* Responsible for making sure terrain exists. If it doesn't it tells
 * the terrain generator to create it.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainManager extends Thread {
	DataManager dataManager;
	public TerrainGenerator terrainGenerator;
	public TerrainManager(DataManager dataManager) {
		this.dataManager = dataManager;
		terrainGenerator = new TerrainGenerator(dataManager);
	}
	public void run() {
		int blocksPerTick = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					if (dataManager.system.blockGenerationLastTick || dataManager.system.characterMoving || dataManager.system.requestGenUpdate) {
						dataManager.system.requestGenUpdate = false;
						blocksPerTick = 0;
						for(int i = dataManager.system.minTileXGen - 2; i <= dataManager.system.maxTileXGen + 1 && blocksPerTick < 1000; i++) {
							for (int j = dataManager.system.minTileYGen - 2; j <= dataManager.system.maxTileYGen + 1; j++) {
								if (!dataManager.world.ou.isGroundBlock(i, j)) {
									terrainGenerator.generateBlock(i, j);
									dataManager.entitySpawn.considerEntity(new Location(i, j));
									blocksPerTick++;
								}
							}
						}
						dataManager.system.blockGenerationLastTick = (blocksPerTick > 0);
						if (!dataManager.system.blockGenerationLastTick && dataManager.system.loadingWorld) {
							dataManager.system.loadingWorld = false;
						}
					}
					if (dataManager.system.updateDungeon) {
						dataManager.system.blockGenerationLastTick = true;
						if (dataManager.savable.dungeonLevel >= 0) {
							dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY).requestLevel(dataManager.savable.dungeonLevel, dataManager.system.blockNameMap, dataManager);
							if (dataManager.savable.lastDungeonLevel > dataManager.savable.dungeonLevel) {
								dataManager.characterManager.characterEntity.setX(dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY).getStairsDown(dataManager.savable.dungeonLevel)[0] + 0.5);
								dataManager.characterManager.characterEntity.setY(dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY).getStairsDown(dataManager.savable.dungeonLevel)[1] + 0.5);
							} else {
								dataManager.characterManager.characterEntity.setX(dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY).getStairsUp(dataManager.savable.dungeonLevel)[0] + 0.5);
								dataManager.characterManager.characterEntity.setY(dataManager.savable.dungeonMap.get(dataManager.savable.dungeonX + "," + dataManager.savable.dungeonY).getStairsUp(dataManager.savable.dungeonLevel)[1] + 0.5);

							}
						}
						dataManager.system.updateDungeon = false;
						dataManager.savable.lastDungeonLevel = dataManager.savable.dungeonLevel;
						dataManager.blockUpdateManager.updateLighting(new Location(dataManager.characterManager.characterEntity));
					}
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
