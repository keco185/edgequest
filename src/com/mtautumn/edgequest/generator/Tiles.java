package com.mtautumn.edgequest.generator;

/**
 * This class holds all the tile data as integers for 
 * easy reading in the rest of the generator class
 * 
 * @author Gray
 */
public final class Tiles {
	
	// Can't have anyone just making tiles all of a sudden
	private Tiles() {}
	
	// Tile constants
	public final static short EMPTY = -1;
	public final static short DIRT = 0;
	public final static short FLOOR = 1;
	public final static short UP_STAIR = 2;
	public final static short DOWN_STAIR = 3;
	public final static short WATER = 4;
	public final static short SNOW = 5;
	public final static short SAND = 6;
	public final static short GRASS = 7;
	public final static short DARK_WOOD = 8;
	public final static short LIGHT_WOOD = 9;
	
}

