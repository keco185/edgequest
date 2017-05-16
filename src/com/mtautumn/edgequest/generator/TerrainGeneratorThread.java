package com.mtautumn.edgequest.generator;

import java.util.ArrayList;
import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.dataObjects.RoadState;
import com.mtautumn.edgequest.generator.overlay.Cave;
import com.mtautumn.edgequest.generator.road.RoadAStar;
import com.mtautumn.edgequest.generator.road.RoadGenerator;
import com.mtautumn.edgequest.generator.road.RoadAStar.IntCoord;
import com.mtautumn.edgequest.generator.room.Center;
import com.mtautumn.edgequest.generator.tile.Tiles;
import com.mtautumn.edgequest.dataObjects.ChunkLocation;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.threads.BlockUpdateManager;
import com.mtautumn.edgequest.threads.ChunkManager;
import com.mtautumn.edgequest.utils.WorldUtils;

//import com.mtautumn.edgequest.updates.UpdateLighting;

public class TerrainGeneratorThread extends Thread {

	//private UpdateLighting updateLight;
	private LightSource light;
	public BlockUpdateManager blockUpdatManager;
	private TerrainGenerator terrainGenerator;
	private int x;
	private int y;
	private int level;
	private long dungeonSeedBase;
	private long villageSeedBase;

	public TerrainGeneratorThread(TerrainGenerator terrainGenerator, int x, int y, int level) {

		this.terrainGenerator = terrainGenerator;
		this.x = x;
		this.y = y;
		this.level = level;

	}

	@Override
	public void run() {

		dungeonSeedBase = generateSeed(DataManager.savable.seed,213);
		villageSeedBase = generateSeed(DataManager.savable.seed,625);

		if (level == -1) {

			genArea(x,y);
			loadChunks(x,y,level+1);
			if (doesContainDungeon(x,y)) {
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x,y,level + 1);
			}
			unloadChunks(x,y,level+1);

		} else if (level == 0) {
			genArea(x,y);
			loadChunks(x,y,level+1);
			if (doesContainDungeon(x,y)) {
				genDungeon(x,y, level);
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x, y, level);
				genEmptyGround(x, y, level + 1);
			}
			unloadChunks(x,y,level+1);
		} else {
			loadChunks(x,y,level+1);
			loadChunks(x,y,level-1);
			if (doesContainDungeon(x,y)) {
				genDungeon(x,y,level - 1);
				genDungeon(x,y,level);
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x,y,level - 1);
				genEmptyGround(x,y,level);
				genEmptyGround(x,y,level + 1);
			}
			unloadChunks(x,y,level+1);
			unloadChunks(x,y,level-1);
		}
	}
	public void loadChunks(int x, int y, int level) {
		for (int i = x; i <= x+100; i+=10) {
			for (int j = y; j <= y + 100; j+=10) {
				ChunkManager.loadChunk(new ChunkLocation(i, j, level));
				ChunkManager.getChunk(new ChunkLocation(i,j,level)).inUse = true;
			}
		}
	}
	public void unloadChunks(int x, int y, int level) {
		for (int i = x; i <= x+100; i+=10) {
			for (int j = y; j <= y + 100; j+=10) {
				ChunkManager.getChunk(new ChunkLocation(i,j,level)).inUse = false;
			}
		}
	}
	public void genArea(int x, int y) {
		if (!beenGenerated(x,y,-1)) {
			loadChunks(x,y,-1);
			for (int i = x; i < x + 100; i++) {
				for (int j = y; j < y + 100; j++) {
					terrainGenerator.generateBlock(i, j);
					DataManager.entitySpawn.considerEntity(new Location(i, j, -1));
				}
			}
			generated(x,y,-1);
			unloadChunks(x,y,-1);
		}
		if (doesContainDungeon(x,y)) {
			loadChunks(x,y,DataManager.savable.dungeonLevel);
			genDungeon(x,y,DataManager.savable.dungeonLevel);
		} else {
			loadChunks(x,y,DataManager.savable.dungeonLevel);
			genEmptyGround(x,y,DataManager.savable.dungeonLevel);
			if (doesContainVillage(x,y)) {
				genVillage(x,y);
			} else {
				if (!beenGenerated(x, y, -2)) {
					generated(x,y,-2);
				}
			}
		}
		genRoad(x,y);
		unloadChunks(x,y,DataManager.savable.dungeonLevel);

	}
	public void genRoad(int x, int y) {
		if (!beenGenerated(x,y,-3)) {
			int chunkX = x / 100;
			int chunkY = y / 100;
			int range = 5;
			ArrayList<Integer> villageXCoords = new ArrayList<Integer>();
			ArrayList<Integer> villageYCoords = new ArrayList<Integer>();
			for (int i = chunkX - range; i <= chunkX + range; i++) {
				for (int j = chunkY - range; j <= chunkY + range; j++) {
					if (doesContainVillage(i * 100, j * 100)) {
						villageXCoords.add(i);
						villageYCoords.add(j);
					}
				}
			}
			RoadState roadState = new RoadState();
			for (int i = 0; i < villageXCoords.size(); i++) {
				for (int j = i + 1; j < villageXCoords.size(); j++) {
					int dX = villageXCoords.get(i) - villageXCoords.get(j);
					int dY = villageYCoords.get(i) - villageYCoords.get(j);
					double distance = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
					if (distance <= range) {
						RoadGenerator roadGenerator = new RoadGenerator(villageXCoords.get(i), villageYCoords.get(i), villageXCoords.get(j), villageYCoords.get(j), DataManager.savable.seed);
						roadGenerator.generate();
						roadState.add(roadGenerator.getRoads(chunkX, chunkY));
					}
				}
			}
			int centerX = 50,centerY = 50;
			if (roadState.countRoads() > 0) {
				boolean groundFound = false;
				short water = SystemData.blockNameMap.get("water").getID();
				short ice = SystemData.blockNameMap.get("ice").getID();
				int offset = 0;
				while(!groundFound && offset < 50) {
					for (int x2 = 50 - offset; x2 <= 50 + offset; x2++) {
						short id = WorldUtils.getGroundBlock(x2 + x, y + 50 + range, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = x2;
							centerY = 50 + range;
						}
						id = WorldUtils.getGroundBlock(x2 + x, y + 50 - range, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = x2;
							centerY = 50 - range;
						}
					}
					for (int y2 = 50 - offset; y2 <= 50 + offset; y2++) {
						short id = WorldUtils.getGroundBlock(x + 50 + range, y2 + y, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = 50 + range;
							centerY = y2;
						}
						id = WorldUtils.getGroundBlock(x + 50 - range, y2 + y, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = 50 - range;
							centerY = y2;
						}
					}
					offset++;
				}
			}
			if (roadState.countRoads() == 2 && !doesContainVillage(chunkX * 100, chunkY * 100)) {
				boolean firstRoad = false;
				int startX = 0,startY = 0,endX = 0,endY = 0;
				if (roadState.roadLeft) {
					firstRoad = true;
					startX = 0;
					startY = 50;
					WorldUtils.setGroundBlock(x, y + 50, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x,y + 50);
				}

				if (roadState.roadRight && !firstRoad) {
					firstRoad = true;
					startX = 100;
					startY = 50;
					WorldUtils.setGroundBlock(x + 100, y + 50, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
				} else if (roadState.roadRight) {
					endX = 99;
					endY = 50;
					WorldUtils.setGroundBlock(x + 100, y + 50, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
				}

				if (roadState.roadTop && !firstRoad) {
					firstRoad = true;
					startX = 50;
					startY = 0;
					WorldUtils.setGroundBlock(x + 50, y, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
				} else if (roadState.roadTop) {
					endX = 50;
					endY = 1;
					WorldUtils.setGroundBlock(x + 50, y, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
				}

				if (roadState.roadBottom && !firstRoad) {
					firstRoad = true;
					startX = 50;
					startY = 100;
					WorldUtils.setGroundBlock(x + 50, y + 100, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
				} else if (roadState.roadBottom) {
					endX = 50;
					endY = 99;
					WorldUtils.setGroundBlock(x + 50, y + 100, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
				}

				if (endX != 0 && endY != 0) {
					generateRoad(x+startX,y+startY,x+endX,y+endY, DataManager.savable.seed);
				} else {
					System.err.println("Could not generate road");
				}
			} else {
				if (roadState.roadLeft) {
					WorldUtils.setGroundBlock(x, y + 50, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x,y + 50);
					generateRoad(x,y+50,x+centerX,y+centerY,DataManager.savable.seed);
				}

				if (roadState.roadRight) {
					WorldUtils.setGroundBlock(x + 100, y + 50, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
					generateRoad(x+100,y+50,x+centerX,y+centerY,DataManager.savable.seed);
				}

				if (roadState.roadTop) {
					WorldUtils.setGroundBlock(x + 50, y, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
					generateRoad(x+50,y,x+centerX,y+centerY,DataManager.savable.seed);
				}

				if (roadState.roadBottom) {
					WorldUtils.setGroundBlock(x + 50, y + 100, -1, SystemData.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
					generateRoad(x+50,y+100,x+centerX,y+centerY,DataManager.savable.seed);
				}
			}
			generated(x,y,-3);

		}

	}

	public void genDungeon(int x, int y, int level) {
		if (!beenGenerated(x,y,level)) {
			//generate dungeon
			int[] stairs = getDungeonStairs(x,y,level-1);

			if (level == 0) {
				WorldUtils.setStructBlock(stairs[0] + x, stairs[1] + y, -1, SystemData.blockNameMap.get("dungeon").getID());
			}

			// Get Temperature map
			double[][] tempMap = new double[100][100];
			for (int i = 0 ; i < 100; i++) {
				for (int j = 0; j < 100; j++) {
					try {
						tempMap[i][j] = terrainGenerator.temperatureMapFiltered.get(x+i+","+y+j);
					} catch (NullPointerException e) {
						tempMap[i][j] = 0;
					}

				}
			}

			int[][] dungeonMap = new DungeonGenerator(100, 100, 10, generateSeed(dungeonSeedBase,x,y,level), new Center(stairs[0], stairs[1]), tempMap).build();

			for (int i = 0; i < dungeonMap.length; i++) {
				for (int j = 0; j < dungeonMap[1].length; j++) {
					int pX = i + x;
					int pY = j + y;
					WorldUtils.setGroundBlock(pX,pY, level, SystemData.blockNameMap.get("stone").getID());
					// Shit.
					if (dungeonMap[i][j] == Tiles.DIRT.getTile()) {
						WorldUtils.setStructBlock(pX,pY,level, SystemData.blockNameMap.get("dirt").getID());
					} else if (dungeonMap[i][j] == Tiles.FLOOR.getTile()) {
						DataManager.entitySpawn.considerEntity(new Location(pX, pY, level));
					} else if (dungeonMap[i][j] == Tiles.UP_STAIR.getTile()) {
						//make ladders into small light sources
						light = new LightSource(pX, pY, 4, level);
						light.onEntity = true;
						DataManager.savable.lightSources.add(light);
						light.posX += 0.5;
						light.posY += 0.5;
						DataManager.blockUpdateManager.lighting.urc.update(light);
						WorldUtils.setStructBlock(pX,pY,level, SystemData.blockNameMap.get("dungeonUp").getID());
					} else if (dungeonMap[i][j] == Tiles.DOWN_STAIR.getTile()) {
						WorldUtils.setStructBlock(pX,pY,level, SystemData.blockNameMap.get("dungeon").getID());
						DataManager.savable.dungeonStairs.put(x+","+y+","+level,new int[]{i,j});
					} else if (dungeonMap[i][j] == Tiles.WATER.getTile()) {
						WorldUtils.setGroundBlock(pX,pY,level, SystemData.blockNameMap.get("water").getID());
					} else {
					}

				}

			}

			generated(x,y,level);

		}

	}
	public int[] getDungeonStairs(int x, int y, int level) { //Stairs going down from level
		double dungeonChance = new Random(generateSeed(dungeonSeedBase,x,y)).nextDouble();
		if (dungeonChance > 0.5) {

			if (DataManager.savable.dungeonStairs.containsKey(x+","+y+","+level)) {
				return DataManager.savable.dungeonStairs.get(x+","+y+","+level);
			}

			if (level == -1) {

				Random rng = new Random(generateSeed(dungeonSeedBase,x,y,421));
				boolean stairsOk = false;
				int stairsX = 0;
				int stairsY = 0;
				int tryCount = 0;

				while (!stairsOk && tryCount < 1000) {

					tryCount++;
					stairsX = (int) (rng.nextDouble() * 100);
					stairsY = (int) (rng.nextDouble() * 100);

					do {
						if (!SystemData.blockIDMap.get(WorldUtils.getGroundBlock(stairsX + x, stairsY + y, level)).isName("water") && !SystemData.blockIDMap.get(WorldUtils.getGroundBlock(stairsX + x, stairsY + y, level)).isName("ice")) {
							stairsOk = true;
						}
						if (!WorldUtils.isGroundBlock(stairsX + x, stairsY + y, -1)) {
							try {
								Thread.sleep(50);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} while (!WorldUtils.isGroundBlock(stairsX + x, stairsY + y, -1));

				}

				if (tryCount >= 1000) {

					rng = new Random(generateSeed(dungeonSeedBase,x,y,421));

					stairsX = (int) (rng.nextDouble() * 100);
					stairsY = (int) (rng.nextDouble() * 100);

					ArrayList<int[]> island = new ArrayList<int[]>();
					island.add(new int[]{-1,-1});
					island.add(new int[]{-1,-2});
					island.add(new int[]{-2,-1});
					island.add(new int[]{-3,0});
					island.add(new int[]{-2,0});
					island.add(new int[]{-1,0});
					island.add(new int[]{0,0});
					island.add(new int[]{1,0});
					island.add(new int[]{2,0});
					island.add(new int[]{3,0});
					island.add(new int[]{1,-1});
					island.add(new int[]{1,-2});
					island.add(new int[]{2,-1});
					island.add(new int[]{0,-1});
					island.add(new int[]{0,-2});
					island.add(new int[]{1,1});
					island.add(new int[]{1,2});
					island.add(new int[]{2,1});
					island.add(new int[]{0,1});
					island.add(new int[]{0,2});
					island.add(new int[]{-1,1});
					island.add(new int[]{-1,2});
					island.add(new int[]{-2,1});

					for (int i = 0; i < island.size(); i++) {
						WorldUtils.setGroundBlock(stairsX + x + island.get(i)[0],stairsY + y + island.get(i)[1], level, SystemData.blockNameMap.get("grass").getID());
					}

				}

				DataManager.savable.dungeonStairs.put(x+","+y+","+level,new int[]{stairsX,stairsY});
				return new int[]{stairsX,stairsY};

			}

			genDungeon(x, y, level);
			return getDungeonStairs(x,y,level);

		}

		return null;

	}
	public void genEmptyGround(int x, int y, int level) {

		if (!beenGenerated(x,y,level)) {

			Cave c = new Cave(100, 100, generateSeed(dungeonSeedBase,x,y,level));
			int[][] caves = c.generateCave(0.1f);

			for (int i = 0; i < caves.length; i++) {
				for (int j = 0; j < caves[i].length; j++) {

					WorldUtils.setGroundBlock(x + i,y + j, level, SystemData.blockNameMap.get("stone").getID());

					if (caves[i][j] == 0) {
						WorldUtils.setStructBlock(x + i,y + j, level, SystemData.blockNameMap.get("dirt").getID());
					}

				}

			}

			generated(x,y,level);

		}

	}
	public void genVillage(int x, int y) {
		if (!beenGenerated(x,y,-2)) {

			Random villageRandom = new Random(generateSeed(villageSeedBase, x, y));
			int villageWidth = (int) (15 + villageRandom.nextDouble() * 50);
			int villageHeight = (int) (15 + villageRandom.nextDouble() * 50);
			int maxRooms = (int)(3+villageRandom.nextDouble() * villageWidth * villageHeight / 240);
			int offsetX = (int) ((100 - villageWidth) * villageRandom.nextDouble());
			int offsetY = (int) ((100 - villageHeight) * villageRandom.nextDouble());

			boolean[][] avoidanceMap = new boolean[villageWidth][villageHeight];
			for (int i = x + offsetX; i < x+villageWidth+offsetX; i++) {
				for (int j = y + offsetY; j < y+villageHeight+offsetY; j++) {
					String name = SystemData.blockIDMap.get(WorldUtils.getGroundBlock(i, j, -1)).getName();
					avoidanceMap[i-x-offsetX][j-y-offsetY] = (!name.equals("water") && !name.equals("ice"));
				}
			}

			int[][] villageMap = new VillageGenerator(villageWidth, villageHeight, maxRooms, villageRandom.nextLong(), new Center(villageWidth/2, villageHeight/2), avoidanceMap).build();

			for(int i = 0; i < villageMap.length; i++) {
				for(int j = 0; j < villageMap[i].length; j++) {
					String name = "";
					if (villageMap[i][j] == (Tiles.DARK_WOOD.getTile())) {
						name = SystemData.blockIDMap.get(WorldUtils.getGroundBlock(i+x+offsetX, j+y+offsetY, -1)).getName();
						if (!name.equals("water") && !name.equals("ice")) {
							WorldUtils.setStructBlock(i+x+offsetX, j+y+offsetY, -1, SystemData.blockNameMap.get("darkWood").getID());
						}
					} else if (villageMap[i][j] == (Tiles.LIGHT_WOOD.getTile())) {
						name = SystemData.blockIDMap.get(WorldUtils.getGroundBlock(i+x+offsetX, j+y+offsetY, -1)).getName();
						if (!name.equals("water") && !name.equals("ice")) {
							WorldUtils.setGroundBlock(i+x+offsetX, j+y+offsetY, -1, SystemData.blockNameMap.get("lightWood").getID());
							if (WorldUtils.isStructBlock(i+x+offsetX, j+y+offsetY, -1)) {
								WorldUtils.removeStructBlock(i+x+offsetX, j+y+offsetY, -1);
							}
						}
					} else {
					}

				}

			}

			generated(x,y,-2);

		}

	}

	public static long generateSeed(long... vals) {

		long newSeed = vals[0];

		for (int i = 1; i < vals.length; i++) {
			newSeed = new Random(newSeed + vals[i]).nextLong();
		}

		return newSeed;

	}

	public boolean doesContainVillage(int x, int y) {
		if (doesContainDungeon(x,y)) {
			return false;
		}
		double villageChance = new Random(generateSeed(villageSeedBase,x,y)).nextDouble();
		if (villageChance > 0.97) {
			return true;
		}
		return false;
	}
	public boolean doesContainDungeon(int x, int y) {
		double dungeonChance = new Random(generateSeed(dungeonSeedBase,x,y)).nextDouble();
		if (dungeonChance > 0.5) {
			return true;
		}
		return false;
	}
	public boolean beenGenerated(int x, int y, int level) {
		return DataManager.savable.generatedRegions.contains(x + "," + y + "," + level);
	}
	public void generated(int x, int y, int level) {
		DataManager.savable.generatedRegions.add(x + "," + y + "," + level);
	}


	public void genRoadBlockPerimeter(int x, int y) {
		WorldUtils.setGroundBlock(x - 1, y, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x + 1, y, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x - 1, y - 1, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x, y - 1, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x + 1, y - 1, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x - 1, y + 1, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x + 1, y + 1, -1, SystemData.blockNameMap.get("asphalt").getID());
		WorldUtils.setGroundBlock(x, y + 1, -1, SystemData.blockNameMap.get("asphalt").getID());

		WorldUtils.removeStructBlock(x - 1, y, -1);
		WorldUtils.removeStructBlock(x + 1, y, -1);
		WorldUtils.removeStructBlock(x - 1, y - 1, -1);
		WorldUtils.removeStructBlock(x, y - 1, -1);
		WorldUtils.removeStructBlock(x + 1, y - 1, -1);
		WorldUtils.removeStructBlock(x - 1, y + 1, -1);
		WorldUtils.removeStructBlock(x + 1, y + 1, -1);
		WorldUtils.removeStructBlock(x, y + 1, -1);
	}
	public void generateRoad(int startX, int startY, int endX, int endY, long seed) {
		RoadAStar aStar = new RoadAStar();
		ArrayList<IntCoord> path = aStar.findPath(startX, startY, endX, endY, -1);
		for (IntCoord coord : path) {
			genRoadBlockPerimeter(coord.x,coord.y);

		}
		for (IntCoord coord : path) {
			WorldUtils.setGroundBlock(coord.x, coord.y, -1, SystemData.blockNameMap.get("asphalt").getID());
		}
	}
}
