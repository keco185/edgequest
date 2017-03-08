package com.mtautumn.edgequest.generator;

import java.util.Random;

/**
 * Documentation Pending
 * @author Gray
 *
 */
public class Houses {
	
	public final static int[][] house1Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 8, 9, 8, 8, 8, 8, 8, 8, 8 }
	};
	
	public static House house1 = new House(house1Arr);
	
	public final static int[][] house2Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
	  	{ 8, 9, 9, 9, 9, 8, 8, 9, 8, 8 },
	  	{ 8, 9, 9, 9, 9, 8, 0, 0, 0, 0 },
	  	{ 8, 8, 9, 8, 8, 8, 0, 0, 0, 0 }
	};
	
	public static House house2 = new House(house2Arr);
	
	public final static int[][] house3Arr = new int[][]{
		{ 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
		{ 8, 9, 9, 9, 9, 9, 9, 9, 9, 8 },
		{ 8, 9, 9, 9, 9, 8, 8, 9, 8, 8 },
		{ 8, 9, 9, 9, 9, 8, 0, 0, 0, 0 },
		{ 8, 8, 9, 8, 8, 8, 0, 0, 0, 0 }
	};
	
	public static House house3 = new House(house3Arr);
	
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
	
	public static House[] HouseList = {house1, house2, house3};
	
}
