package com.mtautumn.edgequest.generator;

import java.util.Arrays;
import java.util.Random;

public class VillageGenerator implements Generator {
	
	// Variables asked for in the constructor
	int width;
	int height;
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

	public VillageGenerator(int width, int height, int maxRooms, long seed, Center start, boolean[][] avoidanceMap) {
		this.seed = seed;
		this.rng = new Random(seed);
		this.width = width;
		this.height = height;
		this.maxRooms = maxRooms;
		this.avoidanceArray = avoidanceMap;
		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[width][height];
		
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
			this.rooms[i] = new Room(rng.nextInt((int) Math.floor(width / 4) + 3), rng.nextInt((int) Math.floor(height / 4) + 3), rng.nextInt(width), rng.nextInt(height));
			int tries = 0;
			while (!roomOk(this.rooms[i]) && tries < 1000) {
				this.rooms[i] = new Room(rng.nextInt((int) Math.floor(width / 4) + 3), rng.nextInt((int) Math.floor(height / 4) + 3), rng.nextInt(width), rng.nextInt(height));
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
		if (room.width > 3 && room.height > 3 && room.center.x + room.width/2 < this.width && room.center.y + room.height/2 < height && room.center.x - room.width/2 > 0 && room.center.y - room.height/2 > 0) {
			
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
	
	public int[][] build() {
		
		for (int i = 0; i < currentMaxRooms; i++ ) {
			// Get current room
			Room room = this.rooms[i];

			// Draw it to the map as a 1 (floor)
			for (int w = 0; w < room.width; w++) {

				for (int h = 0; h < room.height; h++) {
					boolean bounds = (w + room.xLoc < this.width) && (h + room.yLoc < height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0);
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
					if (w + room.xLoc > -1 && w + room.xLoc < this.width && h + room.yLoc > -1 && h + room.yLoc < this.height) {
						this.map[w + room.xLoc][h + room.yLoc] = 2;
					}
				}
			}
		}
		
		return this.map;
		
	}
	
	// Clear the map to a blank state
	public void clearMap() {
		this.map = new int[this.width][this.height];
		// Arrays.fill(this.map, dunTemp.getWall(this.tempurature));
	}
	
	public void debugPrintMap() {
		for (int[] row : this.map) {
		    System.out.println(Arrays.toString(row));
		}
	}
}
