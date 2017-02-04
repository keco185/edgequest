/* Used by most of the game to access/set world block data
 * Automatically determines which block is being referenced based on dungeon
 * level.
 */
package com.mtautumn.edgequest.utils;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Entity;

public class WorldUtils {
	private DataManager dm;
	public OverwordUtils ou;
	public WorldUtils(DataManager dm) {
		this.dm = dm;
		ou = new OverwordUtils(dm);
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
	public boolean isStructBlock(int x, int y, int level) {
		return dm.savable.playerStructuresMap.containsKey(x+","+y+","+level);
	}
	public void removeStructBlock(int x, int y, int level) {
		dm.savable.playerStructuresMap.remove(x+","+y+","+level);
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

	public void setLight(int x, int y, int level, byte val) {
		if (val > -127) {
			dm.savable.lightMap.put(x+","+y+","+level, val);
		} else
			removeLight(x,y,level);
	}
	public byte getLight(int x, int y, int level) {
		if (isLight(x, y, level)) {
			return dm.savable.lightMap.get(x+","+y+","+level);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(int x, int y, int level) {
		return dm.savable.lightMap.containsKey(x+","+y+","+level);
	}
	public void removeLight(int x, int y, int level) {
		dm.savable.lightMap.remove(x+","+y+","+level);
	}
	public double getBrightness() {
		if (dm.savable.dungeonLevel > -1)
			return 0.0;
		int tempTime = dm.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) tempTime += 2400;
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
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

	public void setLight(Entity entity, int x, int y, byte val) {
		setLight(x,y,entity.dungeonLevel,val);
	}
	public byte getLight(Entity entity, int x, int y) {
		return getLight(x,y,entity.dungeonLevel);
	}
	public boolean isLight(Entity entity, int x, int y) {
		return isLight(x,y,entity.dungeonLevel);
	}
	public void removeLight(Entity entity, int x, int y) {
		removeLight(x,y,entity.dungeonLevel);

	}
	public double getBrightness(Entity entity) {
		if (entity.dungeonLevel > -1)
			return 0.0;
		int tempTime = dm.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) tempTime += 2400;
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
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

	public void setLight(Location location, byte val) {
		if (val > -127) {
			setLight(location.x, location.y, location.level, val);
		} else
			removeLight(location);
	}
	public byte getLight(Location location) {
		return getLight(location.x, location.y, location.level);
	}
	public boolean isLight(Location location) {
		return isLight(location.x, location.y, location.level);
	}
	public void removeLight(Location location) {
		removeLight(location.x, location.y, location.level);
	}
	public double getBrightness(Location location) {
		if (location.level > -1)
			return 0.0;
		int tempTime = dm.savable.time - 200;
		double brightness = 0.0;
		if (tempTime < 1200) tempTime += 2400;
		double distFromMidnight = Math.abs(tempTime - 2400);
		if (distFromMidnight > 600) {
			brightness = 1;
		} else if (distFromMidnight > 400){
			brightness = distFromMidnight * 0.004 - 1.4;
		} else {
			brightness = 0.2;
		}
		if (brightness > 1) brightness = 1;
		if (brightness < 0) brightness = 0;
		return brightness;
	}
}
