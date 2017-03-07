package com.mtautumn.edgequest.generator;

/**
 * This class manages rooms by defining their center coordinates and width and lengths.
 * Think of it as a Rectangle class.
 */
public class Room {
	
	int width;
	int height;
	
	int xLoc;
	int yLoc;
	
	int[][] room;
	
	Center center;
	
	/**
	 * This constructor takes basic area and coordinate location to create a room
	 * 
	 * @param  width  an absolute URL giving the base location of the image
	 * @param  height the location of the image, relative to the url argument
	 * @param  xLoc   the x coordinate location
	 * @param  yLoc   the y coordinate location
	 * @see           Room
	 */
	public Room(int width, int height, int xLoc, int yLoc) {
		
		// Dimensions of the room
		this.width = width;
		this.height = height;
		
		// Location of top left corner on map
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		
		// 2D Array
		this.room = new int[width][height];
		
		// Set room array to all 1s, as 1s signify open floor tiles
		// NOTE: Maybe it's possible to remove this in the future, this is mostly wasted cpu cycles
		for(int w = 0; w < width; w++) {
			
			for(int h = 0; h < height; h++) {
				this.room[w][h] = Tile.FLOOR;
			}
			
		}
		
		// Center of the new room
		this.center = new Center((int) Math.floor(width / 2) + xLoc, (int) Math.floor(height / 2) + yLoc);
		
	}
	
	/**
	 * This constructor takes basic area and a Center location to create a room
	 * Similar to the other constructor, but uses a center instead of 2 ints
	 * 
	 * @param  width  an absolute URL giving the base location of the image
	 * @param  height the location of the image, relative to the url argument
	 * @param  center the center coordinate of the room
	 * @see    Room
	 * @see    Center
	 */
	public Room(int width, int height, Center center) {
		
		// Dimensions of the room
		this.width = width;
		this.height = height;
				
		// Location of top left corner on map
		this.xLoc = (int) (center.x - Math.floor(width/2));
		this.yLoc = (int) (center.y - Math.floor(height / 2));
				
		// 2D Array
		this.room = new int[width][height];
				
		// Set room array to all 1s, as 1s signify open floor tiles
		// NOTE: Maybe it's possible to remove this in the future, this is mostly wasted cpu cycles
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				this.room[w][h] = Tile.FLOOR;
			}
		}
				
		// Center of the new room
		this.center = center;
		
	}
	
	/**
	 * Returns the room array. It's not a good idea to use this in most cases, but it exists
	 * 
	 * @return 2D array of ints that represents the room, no coordinate data included
	 * @see    Room
	 */
	public int[][] getRoom() {
		
		return this.room;
		
	}
	
	/**
	 * Returns the center of the room. Useful for connecting up two rooms
	 * 
	 * @return Center location of room as a center object
	 * @see    Room 
	 * @see    Center
	 */
	public Center getCenter() {
		
		return this.center;
		
	}

}
