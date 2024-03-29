package com.mtautumn.edgequest.generator.structure;

import com.mtautumn.edgequest.generator.tile.Tiles;

/**
 * This is an immutable singleton class meant to create and store houses as data.
 * <p>
 * This will most definitely be replaced with JSON sometime soon.
 * @author Gray
 * @see    VillageFeature
 */
public final class Houses {
	
	public final static int W = Tiles.DARK_WOOD_WALL.getTile();
	public final static int F = Tiles.LIGHT_WOOD_FLOOR.getTile();
	// Cause of errors?
	public final static int E = -1;
	
	// House 1
	public final static int[][] house1Arr = new int[][]{
		{ E, E, E, E, E, E, E, E, E, E, E, E },
		{ E, W, W, W, W, W, W, W, W, W, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, W, F, W, W, W, W, W, W, W, E },
	  	{ E, E, E, E, E, E, E, E, E, E, E, E }
	};
	
	public static VillageFeature house1 = new VillageFeature(house1Arr);
	
	// House 2
	public final static int[][] house2Arr = new int[][]{
		{ E, E, E, E, E, E, E, E, E, E, E, E },
		{ E, W, W, W, W, W, W, W, W, W, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, F, F, F, F, F, F, F, F, W, E },
	  	{ E, W, F, F, F, F, W, W, F, W, W, E },
	  	{ E, W, F, F, F, F, W, E, E, E, E, E },
	  	{ E, W, W, F, W, W, W, E, E, E, E, E },
	  	{ E, E, E, E, E, E, E, E, E, E, E, E }
	};
	
	public static VillageFeature house2 = new VillageFeature(house2Arr);
	
	// House 3
	public final static int[][] house3Arr = new int[][]{
		{ E, E, E, E, E, E, E, E, E, E, E, E },
		{ E, W, W, W, W, W, W, W, W, W, W, E },
		{ E, W, F, F, F, F, F, F, F, F, W, E },
		{ E, W, F, F, F, F, F, F, F, F, W, E },
		{ E, W, F, F, F, F, W, W, F, W, W, E },
		{ E, W, F, F, F, F, W, E, E, E, E, E },
		{ E, W, W, F, W, W, W, E, E, E, E, E },
		{ E, E, E, E, E, E, E, E, E, E, E, E }
	};
	
	public static VillageFeature house3 = new VillageFeature(house3Arr);
	
	// Town Hall
	public final static int[][] townhallArr = new int[][] {
		{E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E},
		{E, E, E, W, W, W, W, W, W, W, W, W, W, E, E, E},
		{E, E, W, F, W, F, F, W, W, W, W, W, W, W, E, E},
		{E, W, W, F, W, F, F, W, W, W, W, W, W, W, W, E},
		{E, W, F, F, W, F, F, F, F, F, F, F, F, F, W, E},
		{E, W, F, F, W, F, F, F, F, F, F, F, F, F, W, E},
		{E, W, F, F, F, F, F, F, F, F, F, F, F, F, W, E},
		{E, W, F, F, W, W, W, F, F, W, W, F, F, F, W, E},
		{E, W, F, F, W, E, E, E, E, E, W, W, W, W, W, E},
		{E, W, F, F, W, E, E, E, E, E, E, E, E, E, E, E},
		{E, W, W, W, W, E, E, E, E, E, E, E, E, E, E, E},
		{E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E}
	};
	
	public static VillageFeature townhall = new VillageFeature(townhallArr);
	
	// List of houses
	public static VillageFeature[] HouseList = {house1, house2, house3};
	
}
