package com.mtautumn.edgequest.generator;

import java.util.Random;

public class DrunkardsWalk {

	/*
	 * 
	 * Cellular automata that goes through a 2D array of ints and starts 'eating' walls (0s)
	 * 
	 */
	
	// RNG needs to be used persistently
	Random rng;
	
	int shortWalkPasses = 600;
	int longWalkPasses = 2000;
	
	public DrunkardsWalk(long seed) {
		this.rng = new Random(seed);
	}
	
	// Random walk types
	
	public int[][] shortRandomWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, 1.0f);
	}
	
	public int[][] longRandomWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, 1.0f);
	}
	
	public int[][] shortSemiDrunkWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, 0.5f);
	}
	
	public int[][] longSemiDrunkWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, 0.5f);
	}
	
	public int[][] shortAntWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, 0.25f);
	}
	
	public int[][] longAntWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, 0.25f);
	}
	
	// Walk types for environment features
	
	public int[][] pondWalk(int[][] map, int x, int y) {
		return walk(map, 2000, 1.0f, -1, 4, x, y);
	}
	
	// Main walk methods
	
	// Wrap walk method to randomize inputs
	public int[][] randomWalk(int[][] map, int passes, float chaosChance) {
		return walk(map, passes, chaosChance, 0, 1, rng.nextInt(map.length), rng.nextInt(map[0].length));
	}
	
	// Does most of the heavy lifting
	public int[][] walk(int[][] map, int passes, float chaosChance, int find, int replace, int x, int y) {
		
		// Get bounds of map
		int xMax = map.length;
		int yMax = map[0].length;
		
		// Get a temp map to make changes to
		int[][] tempMap = map;
		
		// Establish an old dir for chaosChance
		int[] oldDir = {0, 0};
		
		// Iterate an arbitrary number of times
		for (int i = 0; i < passes; i++) {
			
			// Get a new directon
			int[] dir = changeDirection(chaosChance);
			
			// See if we need to change the direction or use the old one
			if (dir[0] == 0 && dir[1] == 0) {
				dir = oldDir;
			} else {
				oldDir = dir;
			}
			
			// Move
			x += dir[0];
			y += dir[1];
			
			// Check bounds
			if (x > xMax-1 || y > yMax-1 || x < 0 || y < 0) {x -= dir[0]; y -= dir[1]; }
			
			// Chance map
			if (find == -1) {
				tempMap[x][y] = replace;
			} else if (tempMap[x][y] == find) { 
				tempMap[x][y] = replace; 
			}
			
		}
		
		return tempMap;
		
	}
	
	// Direction changer
	// I mean... it's technically a cellular autonoma
	private int[] changeDirection(float chaosChance) {
		
		int[] i = new int[2];
		int r = this.rng.nextInt(4);
		
		if (this.rng.nextFloat() > chaosChance) {
			i[0] = 0;
			i[1] = 0;
		} else {
			if (r == 0) {
				i[0] = 0;
				i[1] = 1;
			} else if (r == 1) {
				i[0] = 0;
				i[1] = -1;
			} else if (r == 2 ) {
				i[0] = -1;
				i[1] = 0;
			} else {
				i[0] = 1;
				i[1] = 0;
			}
		}
		
		return i;
		
	}
	
}
