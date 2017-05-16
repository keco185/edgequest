package com.mtautumn.edgequest.generator;

/**
 * This class holds all the tile data as integers for 
 * easy reading in the rest of the generator class
 * 
 * @author Gray
 */
public enum Tiles {
	
	EMPTY (-1),
	DIRT (0),
	FLOOR (1),
	UP_STAIR (2),
	DOWN_STAIR (3),
	WATER (4),
	SNOW (5),
	SAND (6),
	GRASS (7),
	DARK_WOOD (8),
	LIGHT_WOOD (9);
	
	private final int value;

    Tiles(final int newValue) {
        value = newValue;
    }

    public int getTile() { return value; }
	
}

