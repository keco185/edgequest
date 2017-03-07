package com.mtautumn.edgequest.generator;

/**
 * This class is essentially a coordinate class to maintain the position
 * of the center of rooms within the dungeon. It's useful to define certain positions
 * and locations, but is primarily used for rooms.
 */
public class Center { 
	
	public final int x; 
	public final int y; 

	/**
	 * This constructor takes an x, y coordinate input to initialize itself
	 *
	 * @param  x  x coordinate
	 * @param  y  y coordinate
	 * @see       Center
	 */
	public Center(int x, int y) { 
		this.x = x; 
	    this.y = y; 
	}
	
	/**
	 * This constructor takes no inputs, and initalizes itself to (0, 0)
	 *
	 * @see       Center
	 */
	public Center() {
		this.x = 0; 
	    this.y = 0; 
	}
	
}
