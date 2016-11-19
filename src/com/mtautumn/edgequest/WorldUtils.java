/* Used by most of the game to access/set world block data
 * Automatically determines which block is being referenced based on dungeon
 * level.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class WorldUtils {
	private DataManager dm;
	public DungeonUtils du;
	public OverwordUtils ou;
	public WorldUtils(DataManager dm) {
		this.dm = dm;
		du = new DungeonUtils(dm);
		ou = new OverwordUtils(dm);
	}
	public void wipeMaps() {
		dm.savable.playerStructuresMap.clear();
		dm.savable.dungeonMap.clear();
		dm.savable.map.clear();
		dm.savable.lightMap.clear();
	}

	public void setStructBlock(int x, int y, short id) {
		if (dm.savable.isInDungeon)
			du.setStructBlock(x, y, id);
		else
			ou.setStructBlock(x, y, id);

	}
	public short getStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getStructBlock(x, y);
		return ou.getStructBlock(x, y);

	}
	public boolean isStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isStructBlock(x, y);
		return ou.isStructBlock(x, y);
	}
	public void removeStructBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeStructBlock(x, y);
		else
			ou.removeStructBlock(x, y);

	}

	public void setGroundBlock(int x, int y, short id) {
		if (dm.savable.isInDungeon)
			du.setGroundBlock(x, y, id);
		else
			ou.setGroundBlock(x, y, id);
	}
	public short getGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getGroundBlock(x, y);
		return ou.getGroundBlock(x, y);
	}
	public boolean isGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isGroundBlock(x, y);
		return ou.isGroundBlock(x, y);
	}
	public void removeGroundBlock(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeGroundBlock(x, y);
		else
			ou.removeGroundBlock(x, y);

	}

	public void setLight(int x, int y, byte val) {
		if (val > -127) {
			if (dm.savable.isInDungeon)
				du.setLight(x, y, val);
			else
				ou.setLight(x, y, val);
		} else
			removeLight(x,y);
	}
	public byte getLight(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.getLight(x, y);
		return ou.getLight(x, y);
	}
	public boolean isLight(int x, int y) {
		if (dm.savable.isInDungeon)
			return du.isLight(x, y);
		return ou.isLight(x, y);
	}
	public void removeLight(int x, int y) {
		if (dm.savable.isInDungeon)
			du.removeLight(x, y);
		else
			ou.removeLight(x, y);

	}
	public double getBrightness() {
		if (dm.savable.isInDungeon)
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
	
	public void setStructBlock(Entity entity, int x, int y, short id) {
		if (entity.dungeonLevel > -1)
			du.setStructBlock(entity, x, y, id);
		else
			ou.setStructBlock(x, y, id);

	}
	public short getStructBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.getStructBlock(entity, x, y);
		return ou.getStructBlock(x, y);

	}
	public boolean isStructBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.isStructBlock(entity, x, y);
		return ou.isStructBlock(x, y);
	}
	public void removeStructBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			du.removeStructBlock(entity, x, y);
		else
			ou.removeStructBlock(x, y);

	}

	public void setGroundBlock(Entity entity, int x, int y, short id) {
		if (entity.dungeonLevel > -1)
			du.setGroundBlock(entity, x, y, id);
		else
			ou.setGroundBlock(x, y, id);
	}
	public short getGroundBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.getGroundBlock(entity, x, y);
		return ou.getGroundBlock(x, y);
	}
	public boolean isGroundBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.isGroundBlock(entity, x, y);
		return ou.isGroundBlock(x, y);
	}
	public void removeGroundBlock(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			du.removeGroundBlock(entity, x, y);
		else
			ou.removeGroundBlock(x, y);

	}

	public void setLight(Entity entity, int x, int y, byte val) {
		if (val > -127) {
			if (entity.dungeonLevel > -1)
				du.setLight(entity, x, y, val);
			else
				ou.setLight(x, y, val);
		} else
			removeLight(entity, x, y);
	}
	public byte getLight(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.getLight(entity, x, y);
		return ou.getLight(x, y);
	}
	public boolean isLight(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			return du.isLight(entity, x, y);
		return ou.isLight(x, y);
	}
	public void removeLight(Entity entity, int x, int y) {
		if (entity.dungeonLevel > -1)
			du.removeLight(entity, x, y);
		else
			ou.removeLight(x, y);

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
		if (location.level > -1)
			du.setStructBlock(location, id);
		else
			ou.setStructBlock(location.x, location.y, id);

	}
	public short getStructBlock(Location location) {
		if (location.level > -1)
			return du.getStructBlock(location);
		return ou.getStructBlock(location.x, location.y);

	}
	public boolean isStructBlock(Location location) {
		if (location.level > -1)
			return du.isStructBlock(location);
		return ou.isStructBlock(location.x, location.y);
	}
	public void removeStructBlock(Location location) {
		if (location.level > -1)
			du.removeStructBlock(location);
		else
			ou.removeStructBlock(location.x, location.y);

	}

	public void setGroundBlock(Location location, short id) {
		if (location.level > -1)
			du.setGroundBlock(location, id);
		else
			ou.setGroundBlock(location.x, location.y, id);
	}
	public short getGroundBlock(Location location) {
		if (location.level > -1)
			return du.getGroundBlock(location);
		return ou.getGroundBlock(location.x, location.y);
	}
	public boolean isGroundBlock(Location location) {
		if (location.level > -1)
			return du.isGroundBlock(location);
		return ou.isGroundBlock(location.x, location.y);
	}
	public void removeGroundBlock(Location location) {
		if (location.level > -1)
			du.removeGroundBlock(location);
		else
			ou.removeGroundBlock(location.x, location.y);

	}

	public void setLight(Location location, byte val) {
		if (val > -127) {
			if (location.level > -1)
				du.setLight(location, val);
			else
				ou.setLight(location.x, location.y, val);
		} else
			removeLight(location);
	}
	public byte getLight(Location location) {
		if (location.level > -1)
			return du.getLight(location);
		return ou.getLight(location.x, location.y);
	}
	public boolean isLight(Location location) {
		if (location.level > -1)
			return du.isLight(location);
		return ou.isLight(location.x, location.y);
	}
	public void removeLight(Location location) {
		if (location.level > -1)
			du.removeLight(location);
		else
			ou.removeLight(location.x, location.y);

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
