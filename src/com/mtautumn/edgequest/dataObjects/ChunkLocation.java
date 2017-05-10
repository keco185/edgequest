package com.mtautumn.edgequest.dataObjects;

public class ChunkLocation {
	private int x,y,level;
	public ChunkLocation(int x, int y, int level) {
		this.x = (int) Math.floor(x / 10.0);
		this.y = (int) Math.floor(y / 10.0);
		this.level = level;
	}
	public int x() {
		return x * 10;
	}
	public int y() {
		return y * 10;
	}
	public int level() {
		return level;
	}
	public String name() {
		return x+","+y+","+level+".chk";
	}
	public boolean isEqualTo(ChunkLocation location) {
		return x == location.x && y == location.y && level == location.level;
	}
}
