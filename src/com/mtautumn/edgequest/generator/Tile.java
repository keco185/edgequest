package com.mtautumn.edgequest.generator;

/**
 * This class holds all the tile data as integers for 
 * easy reading in the rest of the generator class
 */
public final class Tile {
	
	// Can't have anyone just making tiles all of a sudden
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
	public final static int DARK_WOOD = 8;
	public final static int LIGHT_WOOD = 9;
	
}

