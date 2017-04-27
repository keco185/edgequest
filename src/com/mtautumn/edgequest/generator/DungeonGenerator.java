package com.mtautumn.edgequest.generator;

import java.util.Random;

/**
 * This class is used to make a 2D Array of 'Tiles' as a representation
 * of dungeons that will be converted into actual blocks in the game world.
 * 
 * @see Generator
 * @author Gray
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
	
	// Temperature hash map
	double[][] temperatureMap;
	
	// Cave object to be created
	Cave cave;
	// DrukardsWalk object to be created
	DrunkardsWalk drunkWalk;

	/**
	 * This is the main constructor for the dungeon generator
	 * 
	 * @param width     width of map
	 * @param height    height of map
	 * @param maxRooms: maximum number of rooms to create
	 * @param seed      seed for the random number generator
	 * @param start     center object to define the coordinate where the first room spawns
	 * @see             Center
	 */
	public DungeonGenerator(int width, int height, int maxRooms, long seed, Center start, double[][] temperatureMap) {
		this.seed = seed;
		this.rng = new Random(seed);
		this.width = width;
		this.height = height;
		this.maxRooms = maxRooms;
		
		// TODO: Pass temp to dungeon
		this.temperatureMap = temperatureMap;
		
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
			this.rooms[i] = new Room(getValueAround(10), getValueAround(10), this.rng.nextInt(width), this.rng.nextInt(height));
			while (this.rooms[i].center.x > this.width || this.rooms[i].center.y > this.height) {
				this.rooms[i] = new Room(getValueAround(10), getValueAround(10), this.rng.nextInt(width), this.rng.nextInt(height));
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
	
	/**
	 * Add ponds to the dungeon via a Drunkard's Walk
	 * 
	 * @see  DrunkardsWalk
	 * @see DungeonGenerator
	 */
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
	
	/**
	 * Create staircase to go up and down, centered in a room
	 * 
	 * @see Room
	 * @see Center
	 * @see DungeonGenerator
	 */
	private void addStairs() {
		
		int roomDown = this.rng.nextInt(rooms.length);
		
		
		while (roomDown == 0 || rooms[roomDown].center.x + 1 > this.width || rooms[roomDown].center.y + 1 > this.height) {
			roomDown = this.rng.nextInt(rooms.length);
		}
		
		map[rooms[0].center.x][rooms[0].center.y] = Tiles.UP_STAIR;
		map[rooms[roomDown].center.x][rooms[roomDown].center.y] = Tiles.DOWN_STAIR;
	
	}
	
	/**
	 * Apply caves to the dungeon
	 * 
	 * @see Cave
	 * @see DungeonGenerator
	 */
	private void applyCave() {	
		
		this.map = this.cave.makeAndApplyCave(this.map, 0.1f);	
	
	}
	
	/**
	 * Apply temperature map to dungeon
	 * 
	 * @see Temperature
	 * @see DungeonGenerator
	 */
	private void applyTemperature() {
		dunTemp.overlay(this.temperatureMap, this.map);
	}
	
	/** 
	 * Connect all the rooms by making corridors between them
	 * 
	 * @see Room
	 * @See Center
	 * @see DungeonGenerator
	 */
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
	
	/**
	 * Get a random value around some value n
	 * <p>
	 * Used to be n * 4 / 3... If this doesn't work, think about using that
	 * 
	 * @param n number to get a value around
	 * @see     Random
	 * @see     DungeonGenerator
	 */
	private int getValueAround(int n) {
		return (int) (this.rng.nextInt(n) * (4/3));
	}
	
	/**
	 * Make a basic dungeon with rooms and corridors. Bland and uninteresting.
	 */
	private void makeDungeon() {
		
		this.makeRooms();
		this.connectRooms();

	}

	/**
	 * Make rooms in the dungeon
	 * 
	 * @see Room
	 * @see DungeonGenerator
	 */
	private void makeRooms() {

		for (int i = 0; i < currentMaxRooms; i++ ) {
			
			// Get current room
			Room room = this.rooms[i];

			// Draw it to the map as a 1 (floor)
			for (int w = 0; w < room.width; w++) {

				for (int h = 0; h < room.height; h++) {

					// Check bounds
					if ((w + room.xLoc < this.width) && (h + room.yLoc < this.height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0)) {

						this.map[w + room.xLoc][h + room.yLoc] = Tiles.FLOOR;

					}
					
				}
				
			}
			
		}
		
	}

	/**
	 * Make horizontal corridor between two coordinates
	 * 
	 * @param center1  coordinate of first room
	 * @param center2  coordinate of second room
	 * @see   DungeonGenerator
	 */
	private void makeHCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a horizontal corridor from one location
		// to another
		
		if (center1.x < center2.x) {

			for (int i = 1; i < center2.x - center1.x + 1; i++) {
				if (center1.x + i < this.width && center1.y < this.height) {
					this.map[center1.x + i][center1.y] = Tiles.FLOOR;
				}

			}

		} else if (center2.x < center1.x) {

			for (int i = 1; i < center1.x - center2.x + 1; i++) {
				if (center2.x + i < this.width && center2.y < this.height) {
					this.map[center2.x + i][center2.y] = Tiles.FLOOR;
				}
			}

		}


	}

	/**
	 * Make vertical corridor between two coordinates
	 * 
	 * @param center1  coordinate of first room
	 * @param center2  coordinate of second room
	 * @see   DungeonGenerator
	 */
	private void makeVCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a vertical corridor from one location
		// to another
		
		if (center1.y < center2.y) {

			for (int i = 1; i < center2.y - center1.y + 1; i++) {
				
				if (center1.x < this.width && center1.y + i < this.height) {
					this.map[center1.x][center1.y + i] = Tiles.FLOOR;
				}
				
			}

		} else if (center2.y < center1.y) {

			for (int i = 1; i < center1.y - center2.y + 1; i++) {
				
				if (center2.x < this.width && center2.y + i < this.height) {
					this.map[center2.x][center2.y + i] = Tiles.FLOOR;
				}

			}
			
		}
		
	}
	
	/*
	 * Interface methods
	 */

	/**
	 * Clears the map object that the feature stores tile data to
	 * 
	 * @see         Generator
	 */
	@Override
	public void clearMap() {
		
		this.map = new int[this.width][this.height];
		// Arrays.fill(this.map, dunTemp.getWall(this.tempurature));
		
	}
	
	/**
	 * Prints the map object to the console as integers
	 * 
	 * @see         Generator
	 */
	@Override
	public void debugPrintMap() {
		
		for (int[] row : this.map) {
			for (int col : row) {
				 System.out.print(col);
			}
			System.out.print("\n");
		}
		
	}

	/**
	 * Build the feature and return it
	 *
	 * @return      2D array of ints that represent the feature as tiles
	 * @see         Generator
	 */
	@Override
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
