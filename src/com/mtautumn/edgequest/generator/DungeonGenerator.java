package com.mtautumn.edgequest.generator;

import java.util.Arrays;
import java.util.Random;

/*
 * 
 * The Dungeon generator is used to make a 2D Array of 'Tiles' as a representation
 * of dungeons that will be converted into actual blocks in the game world.
 * 
 */
public class DungeonGenerator implements Generator {
	
	// Variables asked for in the constructor
	int width;
	int height;
	int maxRooms;

	// Actual amount of rooms being used
	int currentMaxRooms;

	// Array of all rooms being used
	Room[] rooms;
	
	// Dungeon temperature and manager
	Temperature dunTemp = new Temperature();
	double tempurature;

	// 2D Array to store the map
	int[][] map;

	// RNG
	long seed;
	Random rng;
	
	// Location to spawn center of first room
	Center start = new Center();
	
	// Cave object to be created
	Cave cave;
	// DrukardsWalk object to be created
	DrunkardsWalk drunkWalk;

	/*
	 * Constructor for DungeonGenerator
	 * @param x: Width
	 * @param y: Height
	 * @param maxRooms: Maximum number of rooms to create
	 * @param seed: Seed for the random number generator
	 * @param start: Center object to define the coordinate where the first room spawns
	 */
	public DungeonGenerator(int width, int height, int maxRooms, long seed, Center start) {
		this.seed = seed;
		this.rng = new Random(seed);
		this.width = width;
		this.height = height;
		this.maxRooms = maxRooms;
		
		// TODO: Pass temp to dungeon
		this.tempurature = 100;

		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[width][height];
		
		// Init start
		this.start = start;

		// Get a current number of rooms based on a random value
		this.currentMaxRooms = this.rng.nextInt(maxRooms) + (int) Math.floor(maxRooms / 2);

		// Initialize the array of rooms
		this.rooms = new Room[currentMaxRooms];
		
		// Init start room
		this.rooms[0] = new Room(10, 10, start);

		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		// TODO: This should really be simplified
		for (int i = 1; i < currentMaxRooms; i++ ) {
			this.rooms[i] = new Room(this.rng.nextInt((int) Math.floor(width / 4) + 3), this.rng.nextInt((int) Math.floor(height / 4) + 3), this.rng.nextInt(width), this.rng.nextInt(height));
			while (this.rooms[i].center.x > this.width || this.rooms[i].center.y > this.height) {
				this.rooms[i] = new Room(this.rng.nextInt((int) Math.floor(width / 4) + 3), this.rng.nextInt((int) Math.floor(height / 4) + 3), this.rng.nextInt(width), this.rng.nextInt(height));
			}
		}
		
		// Initialize cave
		this.cave = new Cave(width, height, seed);
		// Initialize Drunkard's Walk
		this.drunkWalk = new DrunkardsWalk(seed);

	}
	
	/*
	 * Private methods
	 */
	
	// Add ponds to the dungeon
	private void addPonds() {
		// NOTE: Lower value when not testing
		int ponds = this.rng.nextInt(3) - 1;
		
		while (ponds > 0) {
			
			int x = this.rng.nextInt(this.map.length);
			int y = this.rng.nextInt(this.map[0].length);
			
			this.map = this.drunkWalk.pondWalk(this.map, x, y);
			
			ponds -= 1;
			
		}
		
	}
	
	// Create staircase to go up and down, centered in a room
	private void addStairs() {
		
		int roomDown = this.rng.nextInt(rooms.length);
		
		
		while (roomDown == 0 || rooms[roomDown].center.x + 1 > this.width || rooms[roomDown].center.y + 1 > this.height) {
			roomDown = this.rng.nextInt(rooms.length);
		}
		
		map[rooms[0].center.x][rooms[0].center.y] = Tile.UP_STAIR;
		map[rooms[roomDown].center.x][rooms[roomDown].center.y] = Tile.DOWN_STAIR;
	
	}
	
	// Apply caves to the dungeon
	private void applyCave() {	
		
		this.map = this.cave.makeAndApplyCave(this.map, 0.1f);	
	
	}
	
	// Connect all the rooms by making corridors between them
	private void connectRooms() {

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
			makeHCorridor(center1, center2);
			makeVCorridor(center1, center2);

		}

	}
	
	// Make a basic dungeon
	private void makeDungeon() {
		
		this.makeRooms();
		this.connectRooms();

	}

	// Make rooms
	private void makeRooms() {

		for (int i = 0; i < currentMaxRooms; i++ ) {
			
			// Get current room
			Room room = this.rooms[i];

			// Draw it to the map as a 1 (floor)
			for (int w = 0; w < room.width; w++) {

				for (int h = 0; h < room.height; h++) {

					// Check bounds
					if ((w + room.xLoc < this.width) && (h + room.yLoc < this.height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0)) {

						this.map[w + room.xLoc][h + room.yLoc] = Tile.FLOOR;

					}
					
				}
				
			}
			
		}
		
	}

	// Make horizontal corridor between two coordinates
	private void makeHCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a horizontal corridor from one location
		// to another
		
		if (center1.x < center2.x) {

			for (int i = 1; i < center2.x - center1.x + 1; i++) {
				if (center1.x + i < this.width && center1.y < this.height) {
					this.map[center1.x + i][center1.y] = Tile.FLOOR;
				}

			}

		} else if (center2.x < center1.x) {

			for (int i = 1; i < center1.x - center2.x + 1; i++) {
				if (center2.x + i < this.width && center2.y < this.height) {
					this.map[center2.x + i][center2.y] = Tile.FLOOR;
				}
			}

		}


	}

	// Make vertical corridor between two coordinates
	private void makeVCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a vertical corridor from one location
		// to another
		
		if (center1.y < center2.y) {

			for (int i = 1; i < center2.y - center1.y + 1; i++) {
				
				if (center1.x < this.width && center1.y + i < this.height) {
					this.map[center1.x][center1.y + i] = Tile.FLOOR;
				}
				
			}

		} else if (center2.y < center1.y) {

			for (int i = 1; i < center1.y - center2.y + 1; i++) {
				
				if (center2.x < this.width && center2.y + i < this.height) {
					this.map[center2.x][center2.y + i] = Tile.FLOOR;
				}

			}
			
		}
		
	}
	
	/*
	 * Interface methods
	 */

	// Clear the map to a blank state
	public void clearMap() {
		
		this.map = new int[this.width][this.height];
		// Arrays.fill(this.map, dunTemp.getWall(this.tempurature));
		
	}
	
	// Print the map to the console
	public void debugPrintMap() {
		
		for (int[] row : this.map) {
		    System.out.println(Arrays.toString(row));
		}
		
	}

	// Returns a 2d map
	public int[][] build() {
		
		this.clearMap();
		this.makeDungeon();
		this.applyCave();
		this.addPonds();
		this.addStairs();
		
		// this.debugPrintMap();
		
		return this.map;

	}

}
