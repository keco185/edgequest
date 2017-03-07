package com.mtautumn.edgequest.generator;

/**
 * This interface is to provide an overarching structure for all cellular automata to implement
 * 
 * @author Gray
 */
@FunctionalInterface
public interface Automata {
	
	// Lengths of various walks to be used
	int shortWalkPasses = 600;
	int longWalkPasses = 2000;
	
	// Predefined chaos chances
	float chaosFull = 1.0f;
	float chaosHalf = 0.5f;
	float chaosQuarter = 0.25f;

	public int[][] walk(int[][] map, int passes, float chaosChance, int find, int replace, int x, int y);
	
}
