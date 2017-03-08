package com.mtautumn.edgequest.generator;

/**
 * Documentation Pending
 * @author Gray
 *
 */
public final class Houses {
	
	public final int[][] house1Arr = new int[][]{
		{ 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 9, 8, 9, 9, 9, 9, 9, 9, 9 }
	};
	
	public House house1 = new House(house1Arr);
	
	public final int[][] house2Arr = new int[][]{
		{ 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
	  	{ 9, 8, 8, 8, 8, 9, 9, 8, 9, 9 },
	  	{ 9, 8, 8, 8, 8, 9, 0, 0, 0, 0 },
	  	{ 9, 9, 8, 9, 9, 9, 0, 0, 0, 0 }
	};
	
	public House house2 = new House(house2Arr);
	
	public final int[][] house3Arr = new int[][]{
		{ 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 },
		{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
		{ 9, 8, 8, 8, 8, 8, 8, 8, 8, 9 },
		{ 9, 8, 8, 8, 8, 9, 9, 8, 9, 9 },
		{ 9, 8, 8, 8, 8, 9, 0, 0, 0, 0 },
		{ 9, 9, 8, 9, 9, 9, 0, 0, 0, 0 }
	};
	
	public House house3 = new House(house3Arr);
	
	public final int[][] townhallArr = new int[][] {
		{8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 8},
		{8, 9, 8, 9, 8, 8, 9, 9, 9, 9, 9, 9, 9, 8},
		{9, 9, 8, 9, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9},
		{9, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9},
		{9, 8, 8, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9},
		{9, 8, 9, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9},
		{9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9},
		{9, 8, 8, 9, 8, 9, 9, 9, 9, 8, 9, 9, 9, 9},
		{9, 8, 8, 9, 0, 0, 9, 9, 0, 0, 0, 0, 0, 0},
		{9, 9, 9, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	public House townhall = new House(townhallArr);
	
	public House[] HouseList = {house1, house2, house3, townhall};
	
}
