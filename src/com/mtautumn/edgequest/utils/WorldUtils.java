/* Used by most of the game to access/set world block data
 * Automatically determines which block is being referenced based on dungeon
 * level.
 */
package com.mtautumn.edgequest.utils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;
import com.mtautumn.edgequest.dataObjects.ChunkLocation;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.threads.ChunkManager;

public class WorldUtils {
	public static boolean noLighting = false;
	public static void wipeMaps() {
		//DataManager.savable.playerStructuresMap.clear();
		//DataManager.savable.map.clear();
		try {
			File dir = new File(GameSaves.getLocal() + "world_" + DataManager.savable.saveName);

			if (dir.exists()) {
				Files.walk(Paths.get(dir.getAbsolutePath()))
				.map(Path::toFile)
				.sorted((o1, o2) -> -o1.compareTo(o2))
				.forEach(File::delete);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DataManager.savable.loadedChunks.clear();
		DataManager.savable.lightMap.clear();
		DataManager.savable.generatedRegions.clear();
	}
	public static void setStructBlock(int x, int y, int level, short id) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		ChunkManager.getChunk(location).wall[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] = id;
		//DataManager.savable.playerStructuresMap.put(x+","+y+","+level, id);
	}
	public static short getStructBlock(int x, int y, int level) {
		if (isStructBlock(x, y, level)) {
			ChunkLocation location = new ChunkLocation(x, y, level);
			return ChunkManager.getChunk(location).wall[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)];
			//return DataManager.savable.playerStructuresMap.get(x+","+y+","+level);
		}
		return 0;
	}
	public static short getStructBlockFast(int x, int y, int level) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		return ChunkManager.getChunk(location).wall[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)];
		//return DataManager.savable.playerStructuresMap.get(x+","+y+","+level);
	}
	public static boolean isStructBlock(int x, int y, int level) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		return ChunkManager.getChunk(location).wall[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] != 0;
		//return DataManager.savable.playerStructuresMap.containsKey(x+","+y+","+level);
	}
	public static void removeStructBlock(int x, int y, int level) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		ChunkManager.getChunk(location).wall[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] = 0;
		//DataManager.savable.playerStructuresMap.remove(x+","+y+","+level);
		if (isLightSource(x, y, level)) {
			removeLightSource(x, y, level);
		}
	}
	public static void setGroundBlock(int x, int y, int level, short id) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		ChunkManager.getChunk(location).ground[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] = id;
		//DataManager.savable.map.put(x+","+y+","+level, id);
	}
	public static short getGroundBlock(int x, int y, int level) {
		if (isGroundBlock(x, y, level)) {
			ChunkLocation location = new ChunkLocation(x, y, level);
			return ChunkManager.getChunk(location).ground[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)];
			//return  DataManager.savable.map.get(x+","+y+","+level);
		}
		return 0;
	}
	public static boolean isGroundBlock(int x, int y, int level) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		return ChunkManager.getChunk(location).ground[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] != 0;
		//return DataManager.savable.map.containsKey(x+","+y+","+level);
	}
	public static void removeGroundBlock(int x, int y, int level) {
		ChunkLocation location = new ChunkLocation(x, y, level);
		ChunkManager.getChunk(location).ground[x - (int)(Math.floor(x / 10.0) * 10.0)][y - (int)(Math.floor(y / 10.0) * 10.0)] = 0;
		//DataManager.savable.map.remove(x+","+y+","+level);
	}
	public static void addLightSource(int x, int y, int level) {
		LightSource light = new LightSource(Double.valueOf(x) + 0.5, Double.valueOf(y) + 0.5, 8, level);
		DataManager.savable.lightMap.put(x+","+y+","+level, light);
		DataManager.savable.lightSources.add(light);
	}
	public static void removeLightSource(int x, int y, int level) {
		LightSource light = DataManager.savable.lightMap.get(x+","+y+","+level);
		DataManager.savable.lightSources.remove(light);
		DataManager.savable.lightMap.remove(x+","+y+","+-1);
	}
	public static boolean isLightSource(int x, int y, int level) {
		return DataManager.savable.lightMap.containsKey(x+","+y+","+level);
	}
	public static double getBrightness() {
		if (DataManager.savable.dungeonLevel > -1) {
			return 0.0;
		}
		int tempTime = DataManager.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) {
			tempTime += 2400;
		}
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) {
			brightness = 1;
		}
		if (brightness < 0) {
			brightness = 0;
		}
		if (DataManager.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-DataManager.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}

	public static void setStructBlock(Entity entity, int x, int y, short id) {
		setStructBlock(x, y, entity.dungeonLevel, id);
	}
	public static short getStructBlock(Entity entity, int x, int y) {
		return getStructBlock(x,y,entity.dungeonLevel);
	}
	public static boolean isStructBlock(Entity entity, int x, int y) {
		return isStructBlock(x, y, entity.dungeonLevel);
	}
	public static void removeStructBlock(Entity entity, int x, int y) {
		removeStructBlock(x, y, entity.dungeonLevel);
	}
	public static void setGroundBlock(Entity entity, int x, int y, short id) {
		setGroundBlock(x, y, entity.dungeonLevel, id);
	}
	public static short getGroundBlock(Entity entity, int x, int y) {
		return getGroundBlock(x, y, entity.dungeonLevel);
	}
	public static boolean isGroundBlock(Entity entity, int x, int y) {
		return isGroundBlock(x, y, entity.dungeonLevel);
	}
	public static void removeGroundBlock(Entity entity, int x, int y) {
		removeGroundBlock(x, y, entity.dungeonLevel);
	}
	public static void addLightSource(Entity entity, int x, int y) {
		addLightSource(x, y, entity.dungeonLevel);
	}
	public static void removeLightSource(Entity entity, int x, int y) {
		removeLightSource(x, y, entity.dungeonLevel);
	}
	public static boolean isLightSource(Entity entity, int x, int y) {
		return isLightSource(x, y, entity.dungeonLevel);
	}
	public static double getBrightness(Entity entity) {
		if (entity.dungeonLevel > -1) {
			return 0.0;
		}
		int tempTime = DataManager.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) {
			tempTime += 2400;
		}
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) {
			brightness = 1;
		}
		if (brightness < 0) {
			brightness = 0;
		}
		if (DataManager.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-DataManager.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}

	public static void setStructBlock(Location location, short id) {
		setStructBlock(location.x, location.y, location.level, id);
	}
	public static short getStructBlock(Location location) {
		return getStructBlock(location.x, location.y, location.level);
	}
	public static boolean isStructBlock(Location location) {
		return isStructBlock(location.x, location.y, location.level);
	}
	public static void removeStructBlock(Location location) {
		removeStructBlock(location.x, location.y, location.level);
	}
	public static void setGroundBlock(Location location, short id) {
		setGroundBlock(location.x, location.y, location.level, id);
	}
	public static short getGroundBlock(Location location) {
		return getGroundBlock(location.x, location.y, location.level);
	}
	public static boolean isGroundBlock(Location location) {
		return isGroundBlock(location.x, location.y, location.level);
	}
	public static void removeGroundBlock(Location location) {
		removeGroundBlock(location.x, location.y, location.level);
	}
	public static void addLightSource(Location location) {
		addLightSource(location.x, location.y, location.level);
	}
	public static void removeLightSource(Location location) {
		removeLightSource(location.x, location.y, location.level);
	}
	public static boolean isLightSource(Location location) {
		return isLightSource(location.x, location.y, location.level);
	}
	public static double getBrightness(Location location) {
		if (location.level > -1) {
			return 0.0;
		}
		int tempTime = DataManager.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) {
			tempTime += 2400;
		}
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) {
			brightness = 1;
		}
		if (brightness < 0) {
			brightness = 0;
		}
		if (DataManager.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-DataManager.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}
}
