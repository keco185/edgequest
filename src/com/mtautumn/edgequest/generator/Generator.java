package com.mtautumn.edgequest.generator;

import com.mtautumn.edgequest.generator.room.Center;
import com.mtautumn.edgequest.generator.room.Room;

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
	
	/**
	 * Check to see if the house is in a good location
	 * <p>
	 * This may or may not be removed, haven't decided
	 * @param room  Room object to check
	 * @return      true if the room is in a good postion, false if not
	 * @see         Room
	 * @see         Center
	 * @see         VillageGenerator
	 */
	public boolean roomOk(Room room);
	
	/**
	 * Take a room and mark it as off limits for future rooms
	 * @param room Room to avoid
	 * @see   Generator
	 */
	public void addRoomAvoid(Room room);
	
}
