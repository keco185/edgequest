package com.mtautumn.edgequest.generator.tile;

/**
 * This class holds all the tile data as integers for 
 * easy reading in the rest of the generator class
 * 
 * @author Gray
 */
public enum Tiles {
	
	EMPTY(-1),
	
	UP_STAIR(10),
	DOWN_STAIR(11),
	
	DIRT_WALL(20),
	GRASS_WALL(21),
	STONE_WALL(22),
	SNOW_WALL(23),
	SAND_WALL(24),
	DARK_WOOD_WALL(25),
	LIGHT_WOOD_WALL(26),
	
	DIRT_FLOOR(30),
	GRASS_FLOOR(31),
	STONE_FLOOR(32),
	SNOW_FLOOR(33),
	SAND_FLOOR(34),
	DARK_WOOD_FLOOR(35),
	LIGHT_WOOD_FLOOR(36),
	
	WATER(100),
	ICE(101);
	
	private final int value;

    Tiles(final int newValue) {
        value = newValue;
    }

    public int getTile() { return value; }
	
}

