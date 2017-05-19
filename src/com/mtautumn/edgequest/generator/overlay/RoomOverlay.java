package com.mtautumn.edgequest.generator.overlay;

import com.mtautumn.edgequest.generator.room.Room;

/**
 * This interface is for things that overlay rooms, and make use of the Room and Center class
 * @author Gray
 *
 */
public interface RoomOverlay {

	/**
	 * Check to see if the house is in a good location
	 * <p>
	 * This may or may not be removed, haven't decided
	 * @param room 	        Room object to check
	 * @param width 	    width of map
	 * @param hieght 	    height of map
	 * @param avoidanceMap  map of all locations to avoid
	 * @return     		    true if the room is in a good position, false if not
	 * @see         		Room
	 * @see        		    Center
	 * @see        			RoomOverlay
	 */
	default boolean roomOk(Room room, int width, int height, boolean[][] avoidanceMap) {
		if (room.width > 3 && room.height > 3 && room.center.x + (int) room.width / 2 + 1 < width && room.center.y + (int) room.height/2 + 1 < height && room.center.x - (int) room.width/2 + 1> 0 && room.center.y - (int) room.height/2 + 1 > 0) {
			
			for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2; i++) {
				
				for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2; j++) {
					
					// TODO can prob get rid of this
					try {
						// Avoidance map is really weird, it needs to be false if something needs to be avoided
						if (avoidanceMap[i][j] == false) {
							return false;
						}
					} catch ( NullPointerException e ) {
					    return true;
					}
					
				}
				
			}
			
			return true;
			
		}
		
		return false;
	};

	/**
	 * Set locations to avoid building on
	 * @param room			Room object to avoid
	 * @param avoidanceMap  map of all locations to avoid
	 * @return
	 */
	public default boolean[][] addRoomAvoid(Room room, boolean[][] avoidanceMap) {
		
		for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2 + 1; i++) {
			
			for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2 + 1; j++) {
				
				try {
					avoidanceMap[i][j] = false;
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
				
			}
			
		}
		
		return avoidanceMap;
		
	}

	/**
	 * This is the main method that overlays a given map
	 * @param map    map to overlay
	 * @return       returns map, modified
	 */
	int[][] overlay(int[][] map);

}
