package com.mtautumn.edgequest.generator;

import java.util.Random;

/*
 * Cellular automata that goes through a 2D array of ints and replaces tiles 
 */

/**
 * This class is used to create chaotic cellular automata to create dungeon features.
 * 
 * @author Gray
 */
public class DrunkardsWalk implements Automata {
	
	// RNG needs to be used persistently
	Random rng;
	
	public DrunkardsWalk(long seed) {
		
		this.rng = new Random(seed);
		
	}
	
	// Random walk types
	
	public int[][] shortRandomWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosFull);
	}
	
	public int[][] longRandomWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosFull);
	}
	
	public int[][] shortSemiDrunkWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosHalf);
	}
	
	public int[][] longSemiDrunkWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosHalf);
	}
	
	public int[][] shortAntWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosQuarter);
	}
	
	public int[][] longAntWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosQuarter);
	}
	
	// Wrap walk method to randomize inputs
	public int[][] randomWalk(int[][] map, int passes, float chaosChance) {
		
		return walk(map, passes, chaosChance, Tiles.DIRT, Tiles.FLOOR, rng.nextInt(map.length), rng.nextInt(map[0].length));
	
	}
	
	// Walk for ponds
	public int[][] pondWalk(int[][] map, int x, int y) {
		
		return walk(map, 2000, 1.0f, Tiles.EMPTY, Tiles.WATER, x, y);
		
	}
	
	/**
	 * Returns a 2D array of integers that represent 'walked' locations. It will take a dungeon map,
	 * and then proceed to replace a selected tile with another according to the Drunkard's Walk
	 * Cellular Automata. 
	 * <p>
	 * This method does most of the heavy lifting of this class. All other walking methods use this in some way
	 *
	 * @param  dunMap       a 2D int array, like the dungeon map from DungeonGenerator for example
	 * @param  passes       number of times to walk
	 * @param  chaosChance  chance between 0 and 1 for the automata to change directions. Higher chance = more likely to change direction
	 * @param  find         tile integer to replace. Think: we want to find this tile and replace it
	 * @param  replace      tile integer to replace found tiles with. Think: we need to replace tiles with it
	 * @param  x            starting x location
	 * @param  y            starting y location
	 * @return              a 2D int array of the dungeon map with the walk applied
	 * @see                 DrunkardsWalk
	 */
	public int[][] walk(int[][] dunMap, int passes, float chaosChance, int find, int replace, int x, int y) {
		
		int xMax = dunMap.length;
		int yMax = dunMap[0].length;
		
		// Hold the old direction
		int[] oldDir = {0, 0};
		
		for (int i = 0; i < passes; i++) {
			
			// Change the direction, if possible
			int[] dir = changeDirection(chaosChance);
			if (dir[0] == 0 && dir[1] == 0) {
				dir = oldDir;
			} else {
				oldDir = dir;
			}
			
			// Since dirs can both be 0, adding does no harm
			x += dir[0];
			y += dir[1];
			
			// Don't go out of map bounds
			if (x > xMax-1 || y > yMax-1 || x < 0 || y < 0) {x -= dir[0]; y -= dir[1]; }
			
			// Empty tiles get deleted regardless, otherwise replace the find tiles specified
			if (find == Tiles.EMPTY) {
				dunMap[x][y] = replace;
			} else if (dunMap[x][y] == find) { 
				dunMap[x][y] = replace; 
			}
			
		}
		
		return dunMap;
		
	}
	
	// Direction changer
	// Drunkards walk features inherent chaos in the direction
	private int[] changeDirection(float chaosChance) {
		
		// Array to hold direction data
		int[] i = new int[2];
		
		// Direction
		int r = this.rng.nextInt(4);
		
		// If the rng can't overcome the user set chaosChance,
		// the direction is the same as the last direction
		if (this.rng.nextFloat() > chaosChance) {
			i[0] = 0;
			i[1] = 0;
		// Otherwise we just change direction based on the r value
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
