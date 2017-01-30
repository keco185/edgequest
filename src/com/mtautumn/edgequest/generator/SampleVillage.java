package com.mtautumn.edgequest.generator;

import java.util.Random;

public class SampleVillage {
	// Variables asked for in the constructor
	int x;
	int y;
	int maxRooms;

	// Actual amount of rooms being used
	int currentMaxRooms;

	// Array of all rooms being used
	Room[] rooms;
	
	// 2D Array to store the map
	int[][] map;
	
	//2D array with false blocks being areas that cannot be built on
	boolean[][] avoidanceArray;

	// RNG
	long seed;
	Random rng;
	
	// Start loc
	Center start = new Center();

	public SampleVillage(int x, int y, int maxRooms, long seed, Center start, boolean[][] avoidanceMap) {
		this.seed = seed;
		rng = new Random(seed);
		this.x = x;
		this.y = y;
		this.maxRooms = maxRooms;
		this.avoidanceArray = avoidanceMap;
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
			int tries = 0;
			while (!roomOk(this.rooms[i]) && tries < 1000) {
				this.rooms[i] = new Room(rng.nextInt((int) Math.floor(this.x / 4) + 3), rng.nextInt((int) Math.floor(this.y / 4) + 3), rng.nextInt(this.x), rng.nextInt(this.y));
				tries++;
			}
			if (tries >= 1000) {
				currentMaxRooms--;
				Room[] roomsTemp = new Room[currentMaxRooms];
				for (int j = 0; j < i; j++) {
					roomsTemp[j] = this.rooms[i];
				}
				this.rooms = roomsTemp;
				i--;
			}
		}

	}
	
	private boolean roomOk(Room room) {
		boolean roomOK = true;
		if (room.width > 3 && room.height > 3 && room.center.x + room.width/2 < this.x && room.center.y + room.height/2 < this.y && room.center.x - room.width/2 > 0 && room.center.y - room.height/2 > 0) {
			
			for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2; i++) {
				for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2; j++) {
					if (!avoidanceArray[i][j]) {
						return false;
					}
				}
			}
		return roomOK;
		}
		return false;
	}
	public int[][] getSampleVillage() {
		
		for (int i = 0; i < currentMaxRooms; i++ ) {
			// Get current room
			Room room = this.rooms[i];

			// Draw it to the map as a 1 (floor)
			for (int w = 0; w < room.width; w++) {

				for (int h = 0; h < room.height; h++) {
					boolean bounds = (w + room.xLoc < x) && (h + room.yLoc < y) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0);
					// Check bounds
					if (bounds && (w == room.width - 1|| h == room.height - 1|| w == 0 || h == 0)) {

						this.map[w + room.xLoc][h + room.yLoc] = 1;

					}

				}
				
			}

		}
		for (int i = 0; i < currentMaxRooms; i++ ) {
			Room room = this.rooms[i];
			for (int w = 1; w + 1< room.width; w++) {

				for (int h = 1; h + 1< room.height; h++) {
					if (w + room.xLoc > -1 && w + room.xLoc < this.x && h + room.yLoc > -1 && h + room.yLoc < this.y) {
						this.map[w + room.xLoc][h + room.yLoc] = 2;
					}
				}
			}
		}
		
		return this.map;
		
	}
}
