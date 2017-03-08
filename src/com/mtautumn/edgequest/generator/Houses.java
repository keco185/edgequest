package com.mtautumn.edgequest.generator;

/**
 * This is an immutable singleton class meant to create and store houses as data.
 * <p>
 * This will most definitely be replaced with JSON sometime soon.
 * @author Gray
 * @see    House
 */
public final class Houses {
	
	// House 1
	public final static int[][] house1Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 8, 9, 8, 8, 8, 8, 8, 8, 8 }
	};
	
	public static House house1 = new House(house1Arr);
	
	// House 2
	public final static int[][] house2Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 8, 8, 9, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 8, 0, 0, 0, 0 },
	  	{ 8, 8, 9, 8, 8, 8, 0, 0, 0, 0 }
	};
	
	public static House house2 = new House(house2Arr);
	
	// House 3
	public final static int[][] house3Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
		{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
		{ 8, 9, 9, 9, 9, 8, 8, 9, 8, 8 },
		{ 8, 9, 9, 9, 9, 8, 0, 0, 0, 0 },
		{ 8, 8, 9, 8, 8, 8, 0, 0, 0, 0 }
	};
	
	public static House house3 = new House(house3Arr);
	
	// Town Hall
	public final static int[][] townhallArr = new int[][] {
		{0, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 0, 0},
		{0, 8, 9, 8, 9, 9, 8, 8, 8, 8, 8, 8, 8, 0},
		{8, 8, 9, 8, 9, 9, 8, 8, 8, 8, 8, 8, 8, 8},
		{8, 9, 9, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8},
		{8, 9, 9, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8},
		{8, 9, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8},
		{8, 9, 9, 8, 8, 8, 9, 9, 8, 8, 9, 9, 9, 8},
		{8, 9, 9, 8, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8},
		{8, 9, 9, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{8, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	public static House townhall = new House(townhallArr);
	
	// List of houses
	public static House[] HouseList = {house1, house2, house3};
	
}
