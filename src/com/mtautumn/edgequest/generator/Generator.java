package com.mtautumn.edgequest.generator;

/**
 * Interface for feature generators such as dungeons and villages
 * 
 * @author Gray
 */
public interface Generator {

	/**
	 * Clears the map object that the feature stores tile data to
	 * 
	 * @see         Generator
	 */
	public void clearMap();
	
	/**
	 * Prints the map object to the console as integers.
	 * <p>
	 * This is for debugging and is optional.
	 * 
	 * @see         Generator
	 */
	default void debugPrintMap() {};
	
	/**
	 * Build the feature and return it
	 *
	 * @return      2D array of ints that represent the feature as tiles
	 * @see         Generator
	 */
	public int[][] build();
	
}
