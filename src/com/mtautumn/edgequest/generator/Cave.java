package com.mtautumn.edgequest.generator;



public class Cave {
	
	/* 
	 * 
	 * Set of functions to apply to a special cave map that is a 2D array of floating point values
	 * 
	 * Just use makeAndApplyCave and you'll be set basically
	 * 
	 */
	
	// Easiest way to make a cave and apply
	public static int[][] makeAndApplyCave(int[][] dunMap, long seed, float thresh) {
		return overlayCave(thresholdMap(makeCaves(100,100,seed), thresh), dunMap);
	}
	public static int[][] generateCave(long seed, float thresh) {
		return thresholdMap(makeCaves(100,100,seed), thresh);
	}
	// Create a basic 2D map of floats
	private static float[][] makeCaves(int width, int height, long seed) {
		SimplexNoise smplxNoise = new SimplexNoise(width/4, 0.5, seed);
		float[][] map = new float[width][height];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				map[x][y] = (float) smplxNoise.getNoise(x, y);
			}
		}
		return map;
	}
	private static int[][] thresholdMap(float[][] map, float thresh) {
		int[][] thresholdedMap = new int[map.length][map[0].length];
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				if (map[x][y] > thresh) {
					thresholdedMap[x][y] = 1;
				}
			}
		}
		return thresholdedMap;
	}
	// Overlay a cave map on top of a dungeon map.
	// NOTE: This function assumes that the 
	// cave map and dungeon map are of the same dimensions and that a 
	// threshold has been applied to the cave map
	private static int[][] overlayCave(int[][] caveMap, int[][] dunMap) {
		for (int i = 0; i < caveMap.length ; i++) {
			for (int j = 0; j < caveMap[0].length; j++) {
				// Only knock down walls
				if (caveMap[i][j] == 1 && dunMap[i][j] != Tile.FLOOR) {
					dunMap[i][j] = Tile.FLOOR;
				}
			}
		}
		return dunMap;
	}
	
}