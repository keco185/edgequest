package com.mtautumn.edgequest.generator;

import java.util.Arrays;
import java.util.Random;

// The dungeon generator
public class Generator {
	/*
	 * 
	 * The Dungeon generator is used to make a 2D Array of 0s and 1s.
	 * 0s represent wall tiles, and 1s represent floor, or open tiles
	 * 
	 */

	// Variables asked for in the constructor
	int x;
	int y;
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
	
	// Start loc
	Center start = new Center();

	public Generator(int x, int y, int maxRooms, long seed, Center start) {
		this.seed = seed;
		rng = new Random(seed);
		this.x = x;
		this.y = y;
		this.maxRooms = maxRooms;
		
		// TODO: Pass temp to dungeon
		this.tempurature = 100;

		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[x][y];
		
		// Init start
		this.start = start;

		// Get a current number of rooms based on a random value
		this.currentMaxRooms = rng.nextInt(maxRooms) + (int) Math.floor(maxRooms / 2);

		// Initialize the array of rooms
		this.rooms = new Room[currentMaxRooms];
		
		// Init start room
		this.rooms[0] = new Room(10, 10, start);

		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		for (int i = 1; i < currentMaxRooms; i++ ) {
			this.rooms[i] = new Room(rng.nextInt((int) Math.floor(this.x / 4) + 3), rng.nextInt((int) Math.floor(this.y / 4) + 3), rng.nextInt(this.x), rng.nextInt(this.y));
			while (this.rooms[i].center.x > this.x || this.rooms[i].center.y > this.y) {
				this.rooms[i] = new Room(rng.nextInt((int) Math.floor(this.x / 4) + 3), rng.nextInt((int) Math.floor(this.y / 4) + 3), rng.nextInt(this.x), rng.nextInt(this.y));
			}
		}

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
					if ((w + room.xLoc < x) && (h + room.yLoc < y)) {

						this.map[w + room.xLoc][h + room.yLoc] = Tile.FLOOR;

					}

				}
			}

		}


	}

	// Make horizontal corridor
	private void makeHCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a horizontal corridor from one location
		// to another
		if (center1.x < center2.x) {

			for (int i = 1; i < center2.x - center1.x + 1; i++) {
				if (center1.x + i < this.x && center1.y < this.y) {
					this.map[center1.x + i][center1.y] = Tile.FLOOR;
				}

			}

		} else if (center2.x < center1.x) {

			for (int i = 1; i < center1.x - center2.x + 1; i++) {
				if (center2.x + i < this.x && center2.y < this.y) {
					this.map[center2.x + i][center2.y] = Tile.FLOOR;
				}
			}

		}


	}

	// Make vertical corridor
	private void makeVCorridor(Center center1, Center center2) {

		// Different formulas based on which center is at a larger location
		// Both accomplish the same thing, drawing a vertical corridor from one location
		// to another
		if (center1.y < center2.y) {

			for (int i = 1; i < center2.y - center1.y + 1; i++) {
				if (center1.x < this.x && center1.y + i < this.y) {
					this.map[center1.x][center1.y + i] = Tile.FLOOR;
				}
			}

		} else if (center2.y < center1.y) {

			for (int i = 1; i < center1.y - center2.y + 1; i++) {
				if (center2.x < this.x && center2.y + i < this.y) {
					this.map[center2.x][center2.y + i] = Tile.FLOOR;
				}

			}
		}


	}

	// Connect all the rooms
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

	// Apply caves
	private void applyCave() {
		
		Cave cave = new Cave();
		this.map = cave.makeAndApplyCave(this.map, this.seed + (long) this.rng.nextInt(), 0.5f);
		
	}
	
	// Creates staircase to go up and down, centered in a room
	public void addStairs() {
		
		int roomDown = rng.nextInt(rooms.length);
		
		
		while (roomDown == 0 || rooms[roomDown].center.x + 1 > this.x || rooms[roomDown].center.y + 1 > this.y) {
			roomDown = rng.nextInt(rooms.length);
		}
		
		map[rooms[0].center.x][rooms[0].center.y] = Tile.UP_STAIR;
		map[rooms[roomDown].center.x][rooms[roomDown].center.y] = Tile.DOWN_STAIR;
	
	}
	
	private void addPonds() {
		// NOTE: Lower value when not testing
		int ponds = rng.nextInt(3) - 1;
		
		while (ponds > 0) {
			
			int x = rng.nextInt(this.map.length);
			int y = rng.nextInt(this.map[0].length);
			
			DrunkardsWalk d = new DrunkardsWalk(this.seed);
			
			this.map = d.pondWalk(this.map, x, y);
			
			ponds -= 1;
			
		}
		
		//this.debugPrintMap();
		
		
	}

	// Clear the map to a blank state
	public void clearMap() {
		this.map = new int[this.x][this.y];
		// Arrays.fill(this.map, dunTemp.getWall(this.tempurature));
	}
	
	public void debugPrintMap() {
		for (int[] row : this.map) {
		    System.out.println(Arrays.toString(row));
		}
	}

	// Make a dungeon
	public void makeDungeon() {
		
		this.makeRooms();
		this.connectRooms();
	
		// this.debugPrintMap();

	}

	// Returns a 2d map
	public int[][] getNewDungeon() {
		this.clearMap();
		this.makeDungeon();
		this.applyCave();
		this.addPonds();
		this.addStairs();
		return this.map;

	}

}
