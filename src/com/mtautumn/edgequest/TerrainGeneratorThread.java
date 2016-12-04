package com.mtautumn.edgequest;

import java.util.Random;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.generator.Center;
import com.mtautumn.edgequest.generator.Generator;
import com.mtautumn.edgequest.generator.Tile;

public class TerrainGeneratorThread extends Thread{
	TerrainGenerator terrainGenerator;
	DataManager dm;
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
		double dungeonChance = new Random(generateSeed(dungeonSeedBase,x,y)).nextDouble();
		if (dungeonChance > 0.5) {
			genDungeon(x,y,dm.savable.dungeonLevel);
		} else {
			genEmptyGround(x,y,dm.savable.dungeonLevel);
			double villageChance = new Random(generateSeed(villageSeedBase,x,y)).nextDouble();
			if (villageChance > 0.5) {
				genVillage(x,y);
			} else {
				if (!beenGenerated(x, y, -2)) {
					generated(x,y,-2);
				}
			}
		}
	}
	public void genDungeon(int x, int y, int level) {
		if (!beenGenerated(x,y,level)) {
			//generate dungeon
			int[] stairs = getDungeonStairs(x,y,level-1);
			if (level == 0) {
				dm.world.setStructBlock(stairs[0] + x, stairs[1] + y, -1, dm.system.blockNameMap.get("dungeon").getID());
			}

			int[][] dungeonMap = new Generator(100, 100, 10, generateSeed(dungeonSeedBase,x,y,level), new Center(stairs[0], stairs[1])).getNewDungeon();


			for (int i = 0; i < dungeonMap.length; i++) {
				for (int j = 0; j < dungeonMap[1].length; j++) {
					int pX = i + x;
					int pY = j + y;
					dm.world.setGroundBlock(pX,pY, level, dm.system.blockNameMap.get("stone").getID());
					switch (dungeonMap[i][j]) {
					case Tile.DIRT:
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dirt").getID());
						break;
					case Tile.FLOOR:
						dm.entitySpawn.considerEntity(new Location(pX, pY, level));
						break;
					case Tile.UP_STAIR:
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dungeonUp").getID());
						break;
					case Tile.DOWN_STAIR:
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("dungeon").getID());
						dm.savable.dungeonStairs.put(x+","+y+","+level,new int[]{i,j});
						break;
					case Tile.WATER:
						// structureMap.put(x+","+y, blockNameMap.get("ground").getID());
						dm.world.setStructBlock(pX,pY,level, dm.system.blockNameMap.get("water").getID());
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
				int stairsX = (int) (rng.nextDouble() * 100);
				int stairsY = (int) (rng.nextDouble() * 100);
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
			for (int i = x; i < x+100; i++) {
				for (int j = y; j < y+100; j++) {
					dm.world.setGroundBlock(i,j, level, dm.system.blockNameMap.get("stone").getID());
					dm.world.setStructBlock(i,j, level, dm.system.blockNameMap.get("dirt").getID());
				}
			}
			generated(x,y,level);
		}
	}
	public void genVillage(int x, int y) {
		if (!beenGenerated(x,y,-2)) {
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
	public boolean beenGenerated(int x, int y, int level) {
		return dm.savable.generatedRegions.contains(x + "," + y + "," + level);
	}
	public void generated(int x, int y, int level) {
		dm.savable.generatedRegions.add(x + "," + y + "," + level);
	}
}
