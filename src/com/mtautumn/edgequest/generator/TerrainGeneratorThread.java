package com.mtautumn.edgequest.generator;

import java.util.ArrayList;
import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.dataObjects.RoadState;
import com.mtautumn.edgequest.generator.RoadAStar.IntCoord;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.threads.BlockUpdateManager;

//import com.mtautumn.edgequest.updates.UpdateLighting;

public class TerrainGeneratorThread extends Thread {

	//private UpdateLighting updateLight;
	private LightSource light;
	public BlockUpdateManager blockUpdatManager;
	private TerrainGenerator terrainGenerator;
	private DataManager dm;
	private int x;
	private int y;
	private int level;
	private long dungeonSeedBase;
	private long villageSeedBase;

	public TerrainGeneratorThread(TerrainGenerator terrainGenerator, DataManager dm, int x, int y, int level) {

		this.terrainGenerator = terrainGenerator;
		this.x = x;
		this.y = y;
		this.level = level;
		this.dm = dm;

	}

	@Override
	public void run() {

		dungeonSeedBase = generateSeed(dm.savable.seed,213);
		villageSeedBase = generateSeed(dm.savable.seed,625);

		if (level == -1) {

			genArea(x,y);
			if (isDungeonBlock(x,y)) {
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x,y,level + 1);
			}

		} else if (level == 0) {
			genArea(x,y);
			if (isDungeonBlock(x,y)) {
				genDungeon(x,y, level);
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x, y, level);
				genEmptyGround(x, y, level + 1);
			}
		} else {
			if (isDungeonBlock(x,y)) {
				genDungeon(x,y,level - 1);
				genDungeon(x,y,level);
				genDungeon(x,y,level + 1);
			} else {
				genEmptyGround(x,y,level - 1);
				genEmptyGround(x,y,level);
				genEmptyGround(x,y,level + 1);
			}
		}
	}

	public void genArea(int x, int y) {
		if (!beenGenerated(x,y,-1)) {
			for (int i = x; i < x + 100; i++) {
				for (int j = y; j < y + 100; j++) {
					terrainGenerator.generateBlock(i, j);
					dm.entitySpawn.considerEntity(new Location(i, j, -1));
				}
			}
			generated(x,y,-1);
		}
		if (doesContainDungeon(x,y)) {
			genDungeon(x,y,dm.savable.dungeonLevel);
		} else {
			genEmptyGround(x,y,dm.savable.dungeonLevel);
			if (doesContainVillage(x,y)) {
				genVillage(x,y);
			} else {
				if (!beenGenerated(x, y, -2)) {
					generated(x,y,-2);
				}
			}
		}
		genRoad(x,y);

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
						RoadGenerator roadGenerator = new RoadGenerator(villageXCoords.get(i), villageYCoords.get(i), villageXCoords.get(j), villageYCoords.get(j), dm.savable.seed);
						roadGenerator.generate();
						roadState.add(roadGenerator.getRoads(chunkX, chunkY));
					}
				}
			}
			int centerX = 50,centerY = 50;
			if (roadState.countRoads() > 0) {
				boolean groundFound = false;
				short water = dm.system.blockNameMap.get("water").getID();
				short ice = dm.system.blockNameMap.get("ice").getID();
				int offset = 0;
				while(!groundFound && offset < 50) {
					for (int x2 = 50 - offset; x2 <= 50 + offset; x2++) {
						short id = dm.world.getGroundBlock(x2 + x, y + 50 + range, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = x2;
							centerY = 50 + range;
						}
						id = dm.world.getGroundBlock(x2 + x, y + 50 - range, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = x2;
							centerY = 50 - range;
						}
					}
					for (int y2 = 50 - offset; y2 <= 50 + offset; y2++) {
						short id = dm.world.getGroundBlock(x + 50 + range, y2 + y, -1);
						if (id != water && id != ice) {
							groundFound = true;
							centerX = 50 + range;
							centerY = y2;
						}
						id = dm.world.getGroundBlock(x + 50 - range, y2 + y, -1);
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
					dm.world.setGroundBlock(x, y + 50, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x,y + 50);
				}

				if (roadState.roadRight && !firstRoad) {
					firstRoad = true;
					startX = 100;
					startY = 50;
					dm.world.setGroundBlock(x + 100, y + 50, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
				} else if (roadState.roadRight) {
					endX = 99;
					endY = 50;
					dm.world.setGroundBlock(x + 100, y + 50, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
				}

				if (roadState.roadTop && !firstRoad) {
					firstRoad = true;
					startX = 50;
					startY = 0;
					dm.world.setGroundBlock(x + 50, y, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
				} else if (roadState.roadTop) {
					endX = 50;
					endY = 1;
					dm.world.setGroundBlock(x + 50, y, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
				}

				if (roadState.roadBottom && !firstRoad) {
					firstRoad = true;
					startX = 50;
					startY = 100;
					dm.world.setGroundBlock(x + 50, y + 100, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
				} else if (roadState.roadBottom) {
					endX = 50;
					endY = 99;
					dm.world.setGroundBlock(x + 50, y + 100, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
				}

				if (endX != 0 && endY != 0) {
					generateRoad(x+startX,y+startY,x+endX,y+endY, dm.savable.seed);
				} else {
					System.err.println("Could not generate road");
				}
			} else {
				if (roadState.roadLeft) {
					dm.world.setGroundBlock(x, y + 50, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x,y + 50);
					generateRoad(x,y+50,x+centerX,y+centerY,dm.savable.seed);
				}

				if (roadState.roadRight) {
					dm.world.setGroundBlock(x + 100, y + 50, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 100,y + 50);
					generateRoad(x+100,y+50,x+centerX,y+centerY,dm.savable.seed);
				}

				if (roadState.roadTop) {
					dm.world.setGroundBlock(x + 50, y, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y);
					generateRoad(x+50,y,x+centerX,y+centerY,dm.savable.seed);
				}

				if (roadState.roadBottom) {
					dm.world.setGroundBlock(x + 50, y + 100, -1, dm.system.blockNameMap.get("asphalt").getID());
					genRoadBlockPerimeter(x + 50,y + 100);
					generateRoad(x+50,y+100,x+centerX,y+centerY,dm.savable.seed);
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
				dm.world.setStructBlock(stairs[0] + x, stairs[1] + y, -1, dm.system.blockNameMap.get("dungeon").getID());
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
					dm.world.setGroundBlock(pX,pY, level, dm.system.blockNameMap.get("stone").getID());
					switch (dungeonMap[i][j]) {
					case Tiles.DIRT:
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dirt").getID());
						break;
					case Tiles.FLOOR:
						dm.entitySpawn.considerEntity(new Location(pX, pY, level));
						break;
					case Tiles.UP_STAIR:
						//make ladders into small light sources
						light = new LightSource(pX, pY, 4, level);
						light.onEntity = true;
						dm.savable.lightSources.add(light);
						light.posX += 0.5;
						light.posY += 0.5;
						dm.blockUpdateManager.lighting.urc.update(light);

						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dungeonUp").getID());
						break;
					case Tiles.DOWN_STAIR:
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dungeon").getID());
						dm.savable.dungeonStairs.put(x+","+y+","+level,new int[]{i,j});
						break;
					case Tiles.WATER:
						dm.world.setGroundBlock(pX,pY,level, dm.system.blockNameMap.get("water").getID());
						break;
					default:
						break;
					}

				}

			}

			generated(x,y,level);

		}

	}
	public int[] getDungeonStairs(int x, int y, int level) { //Stairs going down from level
		double dungeonChance = new Random(generateSeed(dungeonSeedBase,x,y)).nextDouble();
		if (dungeonChance > 0.5) {

			if (dm.savable.dungeonStairs.containsKey(x+","+y+","+level)) {
				return dm.savable.dungeonStairs.get(x+","+y+","+level);
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
						if (!dm.system.blockIDMap.get(dm.world.getGroundBlock(stairsX + x, stairsY + y, level)).isName("water") && !dm.system.blockIDMap.get(dm.world.getGroundBlock(stairsX + x, stairsY + y, level)).isName("ice")) {
							stairsOk = true;
						}
						if (!dm.world.isGroundBlock(stairsX + x, stairsY + y, -1)) {
							try {
								Thread.sleep(50);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} while (!dm.world.isGroundBlock(stairsX + x, stairsY + y, -1));

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
						dm.world.setGroundBlock(stairsX + x + island.get(i)[0],stairsY + y + island.get(i)[1], level, dm.system.blockNameMap.get("grass").getID());
					}

				}

				dm.savable.dungeonStairs.put(x+","+y+","+level,new int[]{stairsX,stairsY});
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

					dm.world.setGroundBlock(x + i,y + j, level, dm.system.blockNameMap.get("stone").getID());

					if (caves[i][j] == 0) {
						dm.world.setStructBlock(x + i,y + j, level, dm.system.blockNameMap.get("dirt").getID());
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
					String name = dm.system.blockIDMap.get(dm.world.ou.getGroundBlock(i, j)).getName();
					avoidanceMap[i-x-offsetX][j-y-offsetY] = (!name.equals("water") && !name.equals("ice"));
				}
			}

			int[][] villageMap = new VillageGenerator(villageWidth, villageHeight, maxRooms, villageRandom.nextLong(), new Center(villageWidth/2, villageHeight/2), avoidanceMap).build();

			for(int i = 0; i < villageMap.length; i++) {
				for(int j = 0; j < villageMap[i].length; j++) {
					String name = "";
					switch (villageMap[i][j]) {
					case (Tiles.DARK_WOOD):
						name = dm.system.blockIDMap.get(dm.world.ou.getGroundBlock(i+x+offsetX, j+y+offsetY)).getName();
					if (!name.equals("water") && !name.equals("ice")) {
						dm.world.ou.setStructBlock(i+x+offsetX, j+y+offsetY, dm.system.blockNameMap.get("darkWood").getID());
					}
					break;
					case (Tiles.LIGHT_WOOD):
						name = dm.system.blockIDMap.get(dm.world.ou.getGroundBlock(i+x+offsetX, j+y+offsetY)).getName();
					if (!name.equals("water") && !name.equals("ice")) {
						dm.world.ou.setGroundBlock(i+x+offsetX, j+y+offsetY, dm.system.blockNameMap.get("lightWood").getID());
						if (dm.world.ou.isStructBlock(i+x+offsetX, j+y+offsetY)) {
							dm.world.ou.removeStructBlock(i+x+offsetX, j+y+offsetY);
						}
					}
					break;
					default:
						break;
					}

				}

			}

			generated(x,y,-2);

		}

	}

	public boolean isDungeonBlock(int x, int y) {

		double dungeonChance = new Random(generateSeed(dungeonSeedBase,x,y)).nextDouble();

		if (dungeonChance > 0.5) {
			return true;
		}

		return false;

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
		if (dungeonChance > 0.7) {
			return true;
		}
		return false;
	}
	public boolean beenGenerated(int x, int y, int level) {
		return dm.savable.generatedRegions.contains(x + "," + y + "," + level);
	}
	public void generated(int x, int y, int level) {
		dm.savable.generatedRegions.add(x + "," + y + "," + level);
	}


	public void genRoadBlockPerimeter(int x, int y) {
		dm.world.setGroundBlock(x - 1, y, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x + 1, y, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x - 1, y - 1, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x, y - 1, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x + 1, y - 1, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x - 1, y + 1, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x + 1, y + 1, -1, dm.system.blockNameMap.get("asphalt").getID());
		dm.world.setGroundBlock(x, y + 1, -1, dm.system.blockNameMap.get("asphalt").getID());

		dm.world.removeStructBlock(x - 1, y, -1);
		dm.world.removeStructBlock(x + 1, y, -1);
		dm.world.removeStructBlock(x - 1, y - 1, -1);
		dm.world.removeStructBlock(x, y - 1, -1);
		dm.world.removeStructBlock(x + 1, y - 1, -1);
		dm.world.removeStructBlock(x - 1, y + 1, -1);
		dm.world.removeStructBlock(x + 1, y + 1, -1);
		dm.world.removeStructBlock(x, y + 1, -1);
	}
	public void generateRoad(int startX, int startY, int endX, int endY, long seed) {
		RoadAStar aStar = new RoadAStar(dm);
		ArrayList<IntCoord> path = aStar.findPath(startX, startY, endX, endY, -1, dm);
		for (IntCoord coord : path) {
			genRoadBlockPerimeter(coord.x,coord.y);

		}
		for (IntCoord coord : path) {
			dm.world.setGroundBlock(coord.x, coord.y, -1, dm.system.blockNameMap.get("asphalt").getID());
		}
	}
}
