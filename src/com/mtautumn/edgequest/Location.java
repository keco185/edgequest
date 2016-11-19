package com.mtautumn.edgequest;

public class Location {
	public int x = 0;
	public int y = 0;
	public int level = -1;
	public int dungeonX = 0;
	public int dungeonY = 0;
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Location(int x, int y, int level, int dungeonX, int dungeonY) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.dungeonX = dungeonX;
		this.dungeonY = dungeonY;
	}
	public Location(Location location) {
		this.x = location.x;
		this.y = location.y;
		this.level = location.level;
		this.dungeonX = location.dungeonX;
		this.dungeonY = location.dungeonY;
	}
	public Location(Entity entity) {
		this.x = (int) entity.getX();
		this.y = (int) entity.getY();
		this.level = entity.dungeonLevel;
		this.dungeonX = entity.dungeon[0];
		this.dungeonY = entity.dungeon[1];
	}
	public boolean isEqual(Location location) {
		if (location.level == -1 && level == -1) {
			return x == location.x && y == location.y;
		}
		return x == location.x && y == location.y && level == location.level && dungeonX == location.dungeonX && dungeonY == location.dungeonY;
	}
}
