/* Used by most of the game to access/set world block data
 * Automatically determines which block is being referenced based on dungeon
 * level.
 */
package com.mtautumn.edgequest.utils;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Entity;

public class WorldUtils {
	private DataManager dm;
	public OverworldUtils ou;
	public boolean noLighting = false;
	public WorldUtils(DataManager dm) {
		this.dm = dm;
		ou = new OverworldUtils(dm);
	}
	public void wipeMaps() {
		dm.savable.playerStructuresMap.clear();
		dm.savable.map.clear();
		dm.savable.lightMap.clear();
		dm.savable.generatedRegions.clear();
	}
	public void setStructBlock(int x, int y, int level, short id) {
		dm.savable.playerStructuresMap.put(x+","+y+","+level, id);
	}
	public short getStructBlock(int x, int y, int level) {
		if (isStructBlock(x, y, level)) {
			return dm.savable.playerStructuresMap.get(x+","+y+","+level);
		}
		return 0;
	}
	public short getStructBlockFast(int x, int y, int level) {
		return dm.savable.playerStructuresMap.get(x+","+y+","+level);
	}
	public boolean isStructBlock(int x, int y, int level) {
		return dm.savable.playerStructuresMap.containsKey(x+","+y+","+level);
	}
	public void removeStructBlock(int x, int y, int level) {
		dm.savable.playerStructuresMap.remove(x+","+y+","+level);
		if (isLightSource(x, y, level)) {
			removeLightSource(x, y, level);
		}
	}
	public void setGroundBlock(int x, int y, int level, short id) {
		dm.savable.map.put(x+","+y+","+level, id);
	}
	public short getGroundBlock(int x, int y, int level) {
		if (isGroundBlock(x, y, level)) {
			return  dm.savable.map.get(x+","+y+","+level);
		}
		return 0;
	}
	public boolean isGroundBlock(int x, int y, int level) {
		return dm.savable.map.containsKey(x+","+y+","+level);
	}
	public void removeGroundBlock(int x, int y, int level) {
		dm.savable.map.remove(x+","+y+","+level);
	}
	public void addLightSource(int x, int y, int level) {
		LightSource light = new LightSource(Double.valueOf(x) + 0.5, Double.valueOf(y) + 0.5, 8, level);
		dm.savable.lightMap.put(x+","+y+","+level, light);
		dm.savable.lightSources.add(light);
	}
	public void removeLightSource(int x, int y, int level) {
		LightSource light = dm.savable.lightMap.get(x+","+y+","+level);
		dm.savable.lightSources.remove(light);
		dm.savable.lightMap.remove(x+","+y+","+-1);
	}
	public boolean isLightSource(int x, int y, int level) {
		return dm.savable.lightMap.containsKey(x+","+y+","+level);
	}
	public double getBrightness() {
		if (dm.savable.dungeonLevel > -1) {
			return 0.0;
		}
		int tempTime = dm.savable.time - 200;
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
		if (dm.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-dm.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}
	
	public void setStructBlock(Entity entity, int x, int y, short id) {
			setStructBlock(x, y, entity.dungeonLevel, id);
	}
	public short getStructBlock(Entity entity, int x, int y) {
		return getStructBlock(x,y,entity.dungeonLevel);
	}
	public boolean isStructBlock(Entity entity, int x, int y) {
		return isStructBlock(x, y, entity.dungeonLevel);
	}
	public void removeStructBlock(Entity entity, int x, int y) {
		removeStructBlock(x, y, entity.dungeonLevel);
	}
	public void setGroundBlock(Entity entity, int x, int y, short id) {
		setGroundBlock(x, y, entity.dungeonLevel, id);
	}
	public short getGroundBlock(Entity entity, int x, int y) {
		return getGroundBlock(x, y, entity.dungeonLevel);
	}
	public boolean isGroundBlock(Entity entity, int x, int y) {
		return isGroundBlock(x, y, entity.dungeonLevel);
	}
	public void removeGroundBlock(Entity entity, int x, int y) {
		removeGroundBlock(x, y, entity.dungeonLevel);
	}
	public void addLightSource(Entity entity, int x, int y) {
		addLightSource(x, y, entity.dungeonLevel);
	}
	public void removeLightSource(Entity entity, int x, int y) {
		removeLightSource(x, y, entity.dungeonLevel);
	}
	public boolean isLightSource(Entity entity, int x, int y) {
		return isLightSource(x, y, entity.dungeonLevel);
	}
	public double getBrightness(Entity entity) {
		if (entity.dungeonLevel > -1) {
			return 0.0;
		}
		int tempTime = dm.savable.time - 200;
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
		if (dm.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-dm.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}
	
	public void setStructBlock(Location location, short id) {
		setStructBlock(location.x, location.y, location.level, id);
	}
	public short getStructBlock(Location location) {
		return getStructBlock(location.x, location.y, location.level);
	}
	public boolean isStructBlock(Location location) {
		return isStructBlock(location.x, location.y, location.level);
	}
	public void removeStructBlock(Location location) {
		removeStructBlock(location.x, location.y, location.level);
	}
	public void setGroundBlock(Location location, short id) {
		setGroundBlock(location.x, location.y, location.level, id);
	}
	public short getGroundBlock(Location location) {
		return getGroundBlock(location.x, location.y, location.level);
	}
	public boolean isGroundBlock(Location location) {
		return isGroundBlock(location.x, location.y, location.level);
	}
	public void removeGroundBlock(Location location) {
		removeGroundBlock(location.x, location.y, location.level);
	}
	public void addLightSource(Location location) {
		addLightSource(location.x, location.y, location.level);
	}
	public void removeLightSource(Location location) {
		removeLightSource(location.x, location.y, location.level);
	}
	public boolean isLightSource(Location location) {
		return isLightSource(location.x, location.y, location.level);
	}
	public double getBrightness(Location location) {
		if (location.level > -1) {
			return 0.0;
		}
		int tempTime = dm.savable.time - 200;
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
		if (dm.savable.dryness < - 0.2) {
			brightness -= (brightness - 0.2) * (-dm.savable.dryness - 0.2) * 0.4;
		}
		return brightness;
	}
}
