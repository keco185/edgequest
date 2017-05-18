package com.mtautumn.edgequest.generator.overlay;

import com.mtautumn.edgequest.generator.VillageGenerator;
import com.mtautumn.edgequest.generator.room.Center;
import com.mtautumn.edgequest.generator.room.Room;

public interface RoomOverlay {

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
	default boolean roomOk(Room room, int width, int height, boolean[][] avoidanceArray) {
		if (room.width > 3 && room.height > 3 && room.center.x + (int) room.width / 2 + 1 < width && room.center.y + (int) room.height/2 + 1 < height && room.center.x - (int) room.width/2 + 1> 0 && room.center.y - (int) room.height/2 + 1 > 0) {
			
			for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2; i++) {
				
				for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2; j++) {
					
					// TODO can prob get rid of this
					try {
						if (avoidanceArray[i][j] == false) {
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

	public default boolean[][] addRoomAvoid(Room room, boolean[][] avoidanceMap) {
		for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2 + 1; i++) {
			
			for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2 + 1; j++) {
				
				avoidanceMap[i][j] = false;
				
			}
			
		}
		
		return avoidanceMap;
		
	}

	int[][] overlay(int[][] map);

}
