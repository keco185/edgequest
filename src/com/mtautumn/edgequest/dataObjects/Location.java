package com.mtautumn.edgequest.dataObjects;

import com.mtautumn.edgequest.entities.Entity;

public class Location {
	public int x = 0;
	public int y = 0;
	public int level = -1;
	public Location(int d, int e, int level) {
		this.x = d;
		this.y = e;
		this.level = level;
	}
	public Location(Location location) {
		this.x = location.x;
		this.y = location.y;
		this.level = location.level;
	}
	public Location(Entity entity) {
		this.x = (int) Math.floor(entity.getX());
		this.y = (int) Math.floor(entity.getY());
		this.level = entity.dungeonLevel;
	}
	public boolean isEqual(Location location) {
		if (location.level == -1 && level == -1) {
			return x == location.x && y == location.y;
		}
		return x == location.x && y == location.y && level == location.level;
	}
}
