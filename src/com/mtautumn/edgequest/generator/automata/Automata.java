package com.mtautumn.edgequest.generator.automata;

/**
 * This interface is to provide an overarching structure for all cellular automata to implement
 * 
 * Each automata has to be able to walk around the map, and thus a walk method is defined.
 * Each automata comes with a few different walk lengths, and several chaos chance variables, that influence the walk randomly.
 * 
 * @author Gray
 */
public interface Automata {
	
	// Lengths of various walks to be used
	int shortWalkPasses = 600;
	int longWalkPasses = 2000;
	
	// Predefined chaos chances
	// Higher chaos = more randomness
	
	// All random
	float chaosFull = 1.0f;
	float chaosThreeQuarters = 0.75f;
	float chaosHalf = 0.5f;
	float chaosQuarter = 0.25f;
	// No randomness
	float chaosEmpty = 0.0f;

	/**
	 * Main walk method to apply cellular automata to maps
	 * @param map			map object to overlay onto
	 * @param passes	    number of times to execute automata
	 * @param chaosChance   randomness chance
	 * @param find          blocks to replace
	 * @param replace       block to replace with
	 * @param x			    starting x location
	 * @param y			    starting y location
	 * @return				map with cellular automata overlaid
	 */
	public int[][] walk(int[][] map, int passes, float chaosChance, int find, int replace, int x, int y);
	
	// Wrap walk method to randomize inputs
	public int[][] randomWalk(int[][] map, int passes, float chaosChance);
	
	// Random Walks
	// Random walk types
	
	public default int[][] shortRandomWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosFull);
	}
	
	public default int[][] longRandomWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosFull);
	}
	
	public default int[][] shortSemiDrunkWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosHalf);
	}
	
	public default int[][] longSemiDrunkWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosHalf);
	}
	
	public default int[][] shortAntWalk(int[][] map) {
		return randomWalk(map, shortWalkPasses, chaosQuarter);
	}
	
	public default int[][] longAntWalk(int[][] map) {
		return randomWalk(map, longWalkPasses, chaosQuarter);
	}
	
}
