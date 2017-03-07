package com.mtautumn.edgequest.generator;

// NOTE: final kwrd might be a problem
/*
 * Tile class holds the number that corresponds to each tile
 */
public final class Tile {
	
	// Can't have anyone just making tiles all of a sudden
	// What is this? Python?
	private Tile() {}
	
	// Tile constants
	public final static int EMPTY = -1;
	public final static int DIRT = 0;
	public final static int FLOOR = 1;
	public final static int UP_STAIR = 2;
	public final static int DOWN_STAIR = 3;
	public final static int WATER = 4;
	public final static int SNOW = 5;
	public final static int SAND = 6;
	public final static int GRASS = 7;
	
}

