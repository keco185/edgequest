package com.mtautumn.edgequest.generator.overlay;

import java.util.Random;

import com.mtautumn.edgequest.generator.DungeonGenerator;
import com.mtautumn.edgequest.generator.room.Center;
import com.mtautumn.edgequest.generator.room.Room;
import com.mtautumn.edgequest.generator.structure.DungeonFeature;
import com.mtautumn.edgequest.generator.structure.DungeonFormations;
import com.mtautumn.edgequest.generator.tile.Tiles;

/**
 * ClassicDungeon overlays a classic roguelike dungeon onto a map
 * @author Gray
 *
 */
public class ClassicDungeon implements RoomOverlay {
	
	/*
	 * Constants
	 */
	
	int ROOM_WIDTH = 10;
	int ROOM_HEIGHT = 10;
	
	/*
	 * Derived from constructor inputs
	 */
	
	int width;
	int height;
	int maxRooms;
	long seed;
	Center start;
	Random rng;
	boolean[][] avoidanceArray;
	
	/*
	 * Derived in constructor
	 */
	
	int currentMaxRooms;
	public Room[] rooms;
	
	public ClassicDungeon(int width, int height, int maxRooms, long seed, Center start, boolean[][] avoidanceArray) {
		
		this.width = width;
		this.height = height;
		this.maxRooms = maxRooms;
		this.seed = seed;
		this.start = start;
		this.rng = new Random(seed);
		this.avoidanceArray = avoidanceArray;
		
		// Get a current number of rooms based on a random value
		this.currentMaxRooms = this.rng.nextInt(maxRooms) + (int) Math.floor(maxRooms / 2);

		// Initialize the array of rooms
		this.rooms = new Room[currentMaxRooms];
				
		// Init start room
		this.rooms[0] = new Room(10, 10, start);

		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		// TODO: This should really be simplified
		for (int i = 1; i < currentMaxRooms; i++ ) {
			rooms[i] = new Room(getValueAround(ROOM_WIDTH), getValueAround(ROOM_HEIGHT), rng.nextInt(width), rng.nextInt(height));
			while (rooms[i].center.x > width || rooms[i].center.y > height || rooms[i].center.x < 0 || rooms[i].center.y < 0) {
				rooms[i] = new Room(getValueAround(ROOM_WIDTH), getValueAround(ROOM_HEIGHT), rng.nextInt(width), rng.nextInt(height));
			}
		}
		
	}
	
	/*
	 * Private methods
	 */
	
	/**
	 * Get a random value around some value n
	 * 
	 * @param n number to get a value around
	 * @see     Random
	 * @see     ClassicDungeon
	 */
	private int getValueAround(int n) {
		return (int) (rng.nextInt(n) * (4/3));
	}
	
	/*
	 * TODO: can V and H corridors be combined?
	 */

	/**
	 * Make vertical corridor between two coordinates
	 * 
	 * @param center1  coordinate of first room
	 * @param center2  coordinate of second room
	 * @see   ClassicDungeon
	 */
	private int[][] makeVCorridor(Center center1, Center center2, int[][] map) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a vertical corridor from one location
		// to another
		
		if (center1.y < center2.y) {

			for (int i = 1; i < center2.y - center1.y + 1; i++) {
				
				if (center1.x < width && center1.y + i < height) {
					map[center1.x][center1.y + i] = Tiles.FLOOR.getTile();
				}
				
			}

		} else if (center2.y < center1.y) {

			for (int i = 1; i < center1.y - center2.y + 1; i++) {
				
				if (center2.x < width && center2.y + i < height) {
					map[center2.x][center2.y + i] = Tiles.FLOOR.getTile();
				}

			}
			
		}
		
		return map;
		
	}
	
	/**
	 * Make horizontal corridor between two coordinates
	 * 
	 * @param center1  coordinate of first room
	 * @param center2  coordinate of second room
	 * @see   ClassicDungeon
	 */
	private int[][] makeHCorridor(Center center1, Center center2, int[][] map) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a horizontal corridor from one location
		// to another
		
		if (center1.x < center2.x) {

			for (int i = 1; i < center2.x - center1.x + 1; i++) {
				if (center1.x + i < width && center1.y < height) {
					map[center1.x + i][center1.y] = Tiles.FLOOR.getTile();
				}

			}

		} else if (center2.x < center1.x) {

			for (int i = 1; i < center1.x - center2.x + 1; i++) {
				if (center2.x + i < width && center2.y < height) {
					map[center2.x + i][center2.y] = Tiles.FLOOR.getTile();
				}
			}

		}
		
		return map;

	}
	
	/** 
	 * Connect all the rooms by making corridors between them
	 * 
	 * @see Room
	 * @See Center
	 * @see ClassicDungeon
	 */
	private int[][] connectRooms(int[][] map) {

		for (int i = 0; i < currentMaxRooms; i++ ) {
			// Initialize rooms
			Room room1;
			Room room2;

			// Get current room and link it to the next room
			// Also wrap around if possible
			if (i == currentMaxRooms - 1) {
				room1 = this.rooms[i];
				room2 = this.rooms[0];
			} else {
				room1 = this.rooms[i];
				room2 = this.rooms[i+1];
			}

			// Get our centers
			Center center1 = room1.getCenter();
			Center center2 = room2.getCenter();

			// Link them by making corridors
			map = makeVCorridor(center1, center2, makeHCorridor(center1, center2, map));

		}
		
		return map;

	}
	
	/**
	 * Make rooms in the dungeon
	 * 
	 * @see Room
	 * @see ClassicDungeon
	 */
	private int[][] makeRooms(int[][] map) {

		for (int i = 0; i < currentMaxRooms; i++ ) {
			
			// Get current room
			Room room = this.rooms[i];

			// Draw it to the map as a 1 (floor)
			for (int w = 0; w < room.width; w++) {

				for (int h = 0; h < room.height; h++) {

					// Check bounds
					if ((w + room.xLoc < this.width) && (h + room.yLoc < this.height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0)) {

						map[w + room.xLoc][h + room.yLoc] = Tiles.FLOOR.getTile();

					}
					
				}
				
			}
			
		}
		
		return map;
		
	}
	
	/*
	 * Public methods
	 */
	
	/**
	 * Add structures to the dungeon map
	 * @return 
	 * 
	 * @see DungeonGenerator
	 */
	public int[][] addStructures(int[][] map) {
		int numStructures = getValueAround(10);
		Room structs[] = new Room[numStructures];
		
		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		for (int i = 0; i < numStructures; i++ ) {
					
			int tries = 0;
					
				do {
					DungeonFeature h = new DungeonFeature(DungeonFormations.struct1arr);
					h.rotate(rng.nextInt(4));
					structs[i] = new Room(h, rng.nextInt(width), rng.nextInt(height));
					tries++;
				} while(!roomOk(structs[i], width, height, avoidanceArray) && tries < 1000);
					
				if (roomOk(structs[i], width, height, avoidanceArray)) {
					avoidanceArray = addRoomAvoid(structs[i], avoidanceArray);
				}
					
				if (tries >= 1000) {
					numStructures--;
					Room[] roomsTemp = new Room[numStructures];
					for (int j = 0; j < i; j++) {
						roomsTemp[j] = structs[i];
					}
					structs = roomsTemp;
					i--;
				}
			}
		
		for (int i = 0; i < numStructures; i++ ) {
			
			// Get current room
			Room room = structs[i];
			
			for (int w = 0; w < room.width; w++) {
				for (int h = 0; h < room.height; h++) {
					map[w + room.xLoc][h + room.yLoc] = room.room[h][w];
				}
			}
			
		}
		
		return map;
		
	}
	
	/**
	 * Create staircase to go up and down, centered in a room
	 * 
	 * @see Room
	 * @see Center
	 * @see ClassicDungeon
	 */
	public int[][] addStairs(int[][] map) {
		
		int roomDown = this.rng.nextInt(rooms.length);
		
		
		while (roomDown == 0 || rooms[roomDown].center.x + 1 > this.width || rooms[roomDown].center.y + 1 > this.height) {
			roomDown = this.rng.nextInt(rooms.length);
		}
		
		map[rooms[0].center.x][rooms[0].center.y] = Tiles.UP_STAIR.getTile();
		map[rooms[roomDown].center.x][rooms[roomDown].center.y] = Tiles.DOWN_STAIR.getTile();
		
		return map;
	
	}
	
	/**
	 * Overlay a classic roguelike dungeon onto a map
	 * 
	 * @param map  map to be overlaid onto
	 * @return     resulting map with dungeon
	 * @see RoomOverlay
	 */
	@Override
	public int[][] overlay(int[][] map) {
		map = makeRooms(map);
		map = connectRooms(map);
		return map;
	}


}
