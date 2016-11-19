/* A series of methods for setting, getting, or checking blocks, structures, or
 * lighting for each tile on each level of the dungeon.(Normally accessed using
 * world utils @ dataManager.world)
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class DungeonUtils {
	DataManager dm;
	public DungeonUtils(DataManager dm) {
		this.dm = dm;
	}


	public void setStructBlock(int x, int y, short id) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setStructureBlock(dm.savable.dungeonLevel, x, y, id);
	}
	public short getStructBlock(int x, int y) {
		if (isStructBlock(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getStructureBlock(dm.savable.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isStructBlock(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isStructureBlock(dm.savable.dungeonLevel, x, y);
	}
	public void removeStructBlock(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeStructureBlock(dm.savable.dungeonLevel, x, y);
	}

	public void setGroundBlock(int x, int y, short id) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setGroundBlock(dm.savable.dungeonLevel, x, y, id);
	}
	public short getGroundBlock(int x, int y) {
		if (isGroundBlock(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getGroundBlock(dm.savable.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isGroundBlock(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isGroundBlock(dm.savable.dungeonLevel, x, y);
	}
	public void removeGroundBlock(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeGroundBlock(dm.savable.dungeonLevel, x, y);
	}

	public void setLight(int x, int y, byte val) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).setLighting(dm.savable.dungeonLevel, x, y, val);
	}
	public byte getLight(int x, int y) {
		if (isLight(x, y)) {
			return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).getLighting(dm.savable.dungeonLevel, x, y);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(int x, int y) {
		return dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).isLighting(dm.savable.dungeonLevel, x, y);
	}
	public void removeLight(int x, int y) {
		dm.savable.dungeonMap.get(dm.savable.dungeonX + "," + dm.savable.dungeonY).removeLighting(dm.savable.dungeonLevel, x, y);
	}

	public void setStructBlock(Entity entity, int x, int y, short id) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).setStructureBlock(entity.dungeonLevel, x, y, id);
	}
	public short getStructBlock(Entity entity, int x, int y) {
		if (isStructBlock(entity, x, y)) {
			return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).getStructureBlock(entity.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isStructBlock(Entity entity, int x, int y) {
		return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).isStructureBlock(entity.dungeonLevel, x, y);
	}
	public void removeStructBlock(Entity entity, int x, int y) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).removeStructureBlock(entity.dungeonLevel, x, y);
	}

	public void setGroundBlock(Entity entity, int x, int y, short id) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).setGroundBlock(entity.dungeonLevel, x, y, id);
	}
	public short getGroundBlock(Entity entity, int x, int y) {
		if (isGroundBlock(entity, x, y)) {
			return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).getGroundBlock(entity.dungeonLevel, x, y);
		}
		return 0;
	}
	public boolean isGroundBlock(Entity entity, int x, int y) {
		return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).isGroundBlock(entity.dungeonLevel, x, y);
	}
	public void removeGroundBlock(Entity entity, int x, int y) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).removeGroundBlock(entity.dungeonLevel, x, y);
	}

	public void setLight(Entity entity, int x, int y, byte val) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).setLighting(entity.dungeonLevel, x, y, val);
	}
	public byte getLight(Entity entity, int x, int y) {
		if (isLight(entity, x, y)) {
			return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).getLighting(entity.dungeonLevel, x, y);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(Entity entity, int x, int y) {
		return dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).isLighting(entity.dungeonLevel, x, y);
	}
	public void removeLight(Entity entity, int x, int y) {
		dm.savable.dungeonMap.get(entity.dungeon[0] + "," + entity.dungeon[1]).removeLighting(entity.dungeonLevel, x, y);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setStructBlock(Location location, short id) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).setStructureBlock(location.level, location.x, location.y, id);
	}
	public short getStructBlock(Location location) {
		if (isStructBlock(location)) {
			return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).getStructureBlock(location.level, location.x, location.y);
		}
		return 0;
	}
	public boolean isStructBlock(Location location) {
		return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).isStructureBlock(location.level, location.x, location.y);
	}
	public void removeStructBlock(Location location) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).removeStructureBlock(location.level, location.x, location.y);
	}

	public void setGroundBlock(Location location, short id) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).setGroundBlock(location.level, location.x, location.y, id);
	}
	public short getGroundBlock(Location location) {
		if (isGroundBlock(location)) {
			return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).getGroundBlock(location.level, location.x, location.y);
		}
		return 0;
	}
	public boolean isGroundBlock(Location location) {
		return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).isGroundBlock(location.level, location.x, location.y);
	}
	public void removeGroundBlock(Location location) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).removeGroundBlock(location.level, location.x, location.y);
	}

	public void setLight(Location location, byte val) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).setLighting(location.level, location.x, location.y, val);
	}
	public byte getLight(Location location) {
		if (isLight(location)) {
			return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).getLighting(location.level, location.x, location.y);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(Location location) {
		return dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).isLighting(location.level, location.x, location.y);
	}
	public void removeLight(Location location) {
		dm.savable.dungeonMap.get(location.dungeonX + "," + location.dungeonY).removeLighting(location.level, location.x, location.y);
	}
}
