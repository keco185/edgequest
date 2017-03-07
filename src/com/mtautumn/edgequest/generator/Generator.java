package com.mtautumn.edgequest.generator;

/*
 * Interface for Terrain Generators such as dungeons, villages, etc
 */

public interface Generator {

	// Build method returns a 2d array of integers that is parsed to
	// create features in the game world.
	public int[][] build();
	
	// Clear the map to a blank state
	public void clearMap();
	
	// Print map to console
	public void debugPrintMap();
	
}
