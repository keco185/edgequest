package com.mtautumn.edgequest.generator;

import java.util.Random;

/* 
 * Set of functions to apply to a special cave map that is a 2D array of floating point values
 * Just use makeAndApplyCave and you'll be set basically.
 * 
 */

public class Cave {
	
	// Constant to set a tile to be replaced
	final static int REPLACE = 1;
	
	// Variables to get with the constructor
	int width;
	int height;
	long seed;
	
	// RNG
	Random rng;
	
	// Constructor to save seed and dimensions
	Cave(int width, int height, long seed) {
		
		this.width = width;
		this.height = height;
		this.seed = seed;
		
		this.rng = new Random(seed);
		
	}
	
	// Easiest way to make a cave and apply
	public int[][] makeAndApplyCave(int[][] dunMap, float thresh) {
		
		return overlayCave(thresholdMap(makeCaves(), thresh), dunMap);
		
	}
	
	public int[][] generateCave(float thresh) {
		
		return thresholdMap(makeCaves(), thresh);
		
	}
	
	// Create a basic 2D map of floats
	private float[][] makeCaves() {
		SimplexNoise smplxNoise = new SimplexNoise(this.width/4, 0.5, this.seed + this.rng.nextInt());
		
		float[][] simplexMap = new float[this.width][this.height];
		
		for (int x = 0; x < simplexMap.length; x++) {
			
			for (int y = 0; y < simplexMap[x].length; y++) {
				simplexMap[x][y] = (float) smplxNoise.getNoise(x, y);
			}
			
		}
		
		return simplexMap;
		
	}
	
	private static int[][] thresholdMap(float[][] map, float thresh) {
		
		int[][] threshMap = new int[map.length][map[0].length];
		
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				if (map[x][y] > thresh) {
					threshMap[x][y] = REPLACE;
				}
			}
		}
		
		return threshMap;
		
	}
	
	// Overlay a cave map on top of a dungeon map.
	// NOTE: This function assumes that the 
	// cave map and dungeon map are of the same dimensions and that a 
	// threshold has been applied to the cave map
	private static int[][] overlayCave(int[][] threshMap, int[][] dunMap) {
		
		for (int i = 0; i < threshMap.length ; i++) {
			
			for (int j = 0; j < threshMap[0].length; j++) {
				
				// Only knock down walls
				if (threshMap[i][j] == REPLACE && dunMap[i][j] != Tile.FLOOR) {
					dunMap[i][j] = Tile.FLOOR;
				}
				
			}
			
		}
		
		return dunMap;
		
	}
	
}