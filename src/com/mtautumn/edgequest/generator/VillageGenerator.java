package com.mtautumn.edgequest.generator;

import java.util.Arrays;
import java.util.Random;

import org.json.*;

import com.mtautumn.edgequest.generator.overlay.Village;
import com.mtautumn.edgequest.generator.room.Center;

/**
 * This class is used to make a 2D Array of 'Tiles' as a representation
 * of villages that will be converted into actual blocks in the game world.
 * 
 * @see Generator
 * @author Gray
 */
public class VillageGenerator implements Generator {
	
	// Variables asked for in the constructor
	int width;
	int height;
	
	// 2D Array to store the map
	int[][] map;
	
	// 2D array with false blocks being areas that cannot be built on
	boolean[][] avoidanceArray;

	// RNG
	long seed;
	Random rng;
	
	// Start loc
	Center start = new Center();
	
	Village village;
	
	/**
	 * This is the main constructor for the village generator
	 * 
	 * @param width     width of map
	 * @param height    height of map
	 * @param maxHouses maximum number of houses to create
	 * @param seed      seed for the random number generator
	 * @param start     center object to define the coordinate where the first house spawns
	 * @see             Center
	 */
	public VillageGenerator(int width, int height, int maxHouses, long seed, Center start, boolean[][] avoidanceMap) {
		this.seed = seed;
		this.rng = new Random(seed);
		this.width = width;
		this.height = height;

		this.village = new Village(width, height, maxHouses, seed, start, avoidanceMap);
		
		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[width][height];

	}
	
	/*
	 * Interface methods
	 */
	
	/**
	 * Clears the map object that the feature stores tile data to
	 * 
	 * @see         Generator
	 */
	@Override
	public void clearMap() {
		map = new int[width][height];
	}
	
	/**
	 * Prints the map object to the console as integers
	 * 
	 * @see         Generator
	 */
	@Override
	public void debugPrintMap() {
		for (int[] row : map) {
		    System.out.println(Arrays.toString(row));
		}
	}
	
	/**
	 * Build the feature and return it
	 *
	 * @return      2D array of ints that represent the feature as tiles
	 * @see         Generator
	 */
	@Override
	public int[][] build() {
		
		return village.overlay(map);
		
	}
	
	public void jsonTest() {
		
		JSONObject obj = new JSONObject("{\"error\": \"village generated\" }");
		System.out.println(obj.get("error"));
		
	}

	
}
