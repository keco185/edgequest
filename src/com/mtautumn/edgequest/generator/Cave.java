package com.mtautumn.edgequest.generator;

import java.util.Random;

/**
 * This class provides a suite of utilities for generating caves from Simplex Noise maps for
 * dungeon generation.
 * 
 * @author Gray
 */
public class Cave implements Overlay {
	
	// Constant to set a tile to be replaced
	final static int REPLACE = 1;
	
	// Variables to get with the constructor
	int width;
	int height;
	long seed;
	
	// RNG
	Random rng;
	
	/**
	 * Create a cave object to add Simplex Noise based caves to the 2d dungeon map array.
	 *
	 * @param  width  the width of the desired map
	 * @param  height the height of the desired map
	 * @param  seed   the long that is currently being used as the seed
	 */
	Cave(int width, int height, long seed) {
		
		this.width = width;
		this.height = height;
		this.seed = seed;
		
		this.rng = new Random(seed);
		
	}
	
	/**
	 * Returns a 2D Cave map over a given dungeon map. This is all you
	 * need to really make a cave.
	 *
	 * @param  dunMap  a 2D int array, like the dungeon map from DungeonGenerator for example
	 * @param  thresh  the threshold value to determine what tile becomes a cave. Values between 0-1 work best
	 * @return         the resulting dungeon map with the cave added in
	 * @see            Cave
	 */
	public int[][] makeAndApplyCave(int[][] dunMap, float thresh) {
		
		return overlay(thresholdMap(makeSimplexNoise(), thresh), dunMap);
		
	}
	
	/**
	 * Returns a threshold map ran on a map of Simplex Noise. Unlike makeAndApplyCave(), this does not overlay to another map.
	 * <p>
	 * Purely returns as 0.0 for not a cave and 1.0 for is a cave
	 *
	 * @param  thresh  the threshold value to determine what tile becomes a cave. Values between 0-1 work best
	 * @return         the threshold map as a 2D array of floats
	 * @see            Cave
	 */
	public int[][] generateCave(float thresh) {
		
		return polarizeThreshold(thresholdMap(makeSimplexNoise(), thresh));
		
	}
	
	/**
	 * Returns a 2D array of doubles based on Simplex Noise. Essentially a 2D simplex noise generator.
	 *
	 * @return      2D array of floats from 0 - 1
	 * @see         Cave
	 */
	private double[][] makeSimplexNoise() {
		SimplexNoise smplxNoise = new SimplexNoise(this.width/4, 0.5, this.seed + this.rng.nextInt());
		
		double[][] simplexMap = new double[this.width][this.height];
		
		for (int x = 0; x < simplexMap.length; x++) {
			
			for (int y = 0; y < simplexMap[x].length; y++) {
				simplexMap[x][y] = smplxNoise.getNoise(x, y);
			}
			
		}
		
		return simplexMap;
		
	}
	
	/**
	 * Returns a 2D array of doubles based on a simplex noise map after a threshold is applied.
	 *
	 * @param  simplexMap  2D array of floats from a Simplex Noise Generator
	 * @param  thresh      the threshold value to determine what tile becomes a cave. Values between 0-1 work best
	 * @return             2D array of doubles as a threshold map
	 * @see                Cave
	 */
	private static double[][] thresholdMap(double[][] simplexMap, float thresh) {
		
		double[][] threshMap = new double[simplexMap.length][simplexMap[0].length];
		
		for (int x = 0; x < simplexMap.length; x++) {
			
			for (int y = 0; y < simplexMap[x].length; y++) {
				
				if (simplexMap[x][y] > thresh) {
					threshMap[x][y] = simplexMap[x][y];
				}
				
			}
			
		}
		
		return threshMap;
		
	}
	
	/**
	 * Documentation pending
	 * @param threshMap
	 * @return
	 */
	public int[][] polarizeThreshold(double[][] threshMap) {
		
		int[][] polarizedMap = new int[threshMap.length][threshMap[0].length];
		
		for (int x = 0; x < threshMap.length; x++) {
			
			for (int y = 0; y < threshMap[x].length; y++) {
				
				if (threshMap[x][y] != 0) {
					polarizedMap[x][y] = REPLACE;
				} else {
					polarizedMap[x][y] = 0;
				}
				
			}
			
		}
		
		return polarizedMap;
		
	}
	
	/**
	 * Returns a finalized cave map based on 'overlaying' or 'subtracting' the threshold map and dungeon map
	 * from the Dungeon Generator.
	 * <p>
	 * This method assumes that the 
	 * cave map and dungeon map are of the same dimensions and that a 
	 * threshold has been applied to the cave map
	 *
	 * @param  threshMap  2D array of ints as a threshold map
	 * @param  dunMap     2D array of ints, like the dungeon map from DungeonGenerator for example
	 * @return            2D array of ints after the threshold map is 'subtracted' from the dungeon map
	 * @see               Cave
	 */

	public int[][] overlay(double[][] threshMap, int[][] dunMap) {
		
		for (int i = 0; i < threshMap.length ; i++) {
			
			for (int j = 0; j < threshMap[0].length; j++) {
				
				// Only knock down walls
				if (threshMap[i][j] != 0 && dunMap[i][j] != Tiles.FLOOR) {
					dunMap[i][j] = Tiles.FLOOR;
				}
				
			}
			
		}
		
		return dunMap;
		
	}
	
}