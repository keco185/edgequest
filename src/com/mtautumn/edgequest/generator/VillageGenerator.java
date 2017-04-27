package com.mtautumn.edgequest.generator;

import java.util.Arrays;
import java.util.Random;

import org.json.*;

/**
 * This class is used to make a 2D Array of 'Tiles' as a representation
 * of villages that will be converted into actual blocks in the game world.
 * 
 * @see Generator
 * @author Gray
 */
public class VillageGenerator implements Generator {
	
	// Variables asked for in the constructor
	int width;
	int height;
	int maxHouses;

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
	
	/**
	 * This is the main constructor for the village generator
	 * 
	 * @param width     width of map
	 * @param height    height of map
	 * @param maxHouses maximum number of houses to create
	 * @param seed      seed for the random number generator
	 * @param start     center object to define the coordinate where the first house spawns
	 * @see             Center
	 */
	public VillageGenerator(int width, int height, int maxHouses, long seed, Center start, boolean[][] avoidanceMap) {
		this.seed = seed;
		this.rng = new Random(seed);
		this.width = width;
		this.height = height;
		this.maxHouses = maxHouses;
		
		this.avoidanceArray = avoidanceMap;
		
		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[width][height];
		
		// Init start
		this.start = start;

		// Get a current number of houses based on a random value
		this.currentMaxRooms = rng.nextInt(maxHouses) + (int) Math.floor(maxHouses / 2);

		// Initialize the array of houses as rooms (Temporary)
		this.rooms = new Room[currentMaxRooms];
		
		// Init start house (Temp)
		this.rooms[0] = new Room(Houses.townhall, start);

	}
	
	/*
	 * Private methods
	 */
	
	/**
	 * Get a random value around some value n
	 * 
	 * @param n number to get a value around
	 * @see     Random
	 * @see VillageGenerator
	 */
	private int getValueAround(int n) {
		return (int) (this.rng.nextInt(n) * (4/3));
	}
	
	/**
	 * Get a random house from the list of houses to use
	 * @return a random house object
	 * @see House
	 * @see Houses
	 * @see VillageGenerator
	 */
	private House getRandomHouse() {
		int h = this.rng.nextInt(Houses.HouseList.length);
	    return Houses.HouseList[h];
	}
	
	/**
	 * Prepare a set of test houses
	 * <p>
	 * This will be removed
	 * 
	 * @see Room
	 * @see VillageGenerator
	 */
	private void prepareTestHouses() {
		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		for (int i = 1; i < currentMaxRooms; i++ ) {
			
			int tries = 0;
			
			do {
				House h = this.getRandomHouse();
				h.rotate(this.rng.nextInt(4));
				this.rooms[i] = new Room(h, this.rng.nextInt(width), this.rng.nextInt(height));
				tries++;
			} while(!roomOk(this.rooms[i]) && tries < 1000);
			
			if (roomOk(this.rooms[i])) {
				this.addRoomAvoid(this.rooms[i]);
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
	
	/**
	 * Construct test houses by putting them on the map
	 * <p>
	 * This will be removed
	 * 
	 * @see Room
	 * @see VillageGenerator
	 */
	private void buildTestHouses() {
		for (int i = 0; i < currentMaxRooms; i++ ) {
			
			// Get current room
			Room room = this.rooms[i];
			
			for (int w = 0; w < room.width; w++) {
				for (int h = 0; h < room.height; h++) {
					boolean bounds = (w + room.xLoc < this.width) && (h + room.yLoc < height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0);
					if (bounds) {
						this.map[w + room.xLoc][h + room.yLoc] = room.room[h][w];
					}
				}
			}
				
		}
		
	}
	
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
	private boolean roomOk(Room room) {
		if (room.width > 3 && room.height > 3 && room.center.x + (int) room.width / 2 + 1 < this.width && room.center.y + (int) room.height/2 + 1 < height && room.center.x - (int) room.width/2 + 1> 0 && room.center.y - (int) room.height/2 + 1 > 0) {
			
			for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2; i++) {
				
				for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2; j++) {
					
					if (!this.avoidanceArray[i][j]) {
						return false;
					}
					
				}
				
			}
			
			return true;
			
		}
		
		return false;
		
	}
	
	/**
	 * Take a room and mark it as off limits for future rooms
	 * @param room Room to avoid
	 * @see   VillageGenerator
	 */
	private void addRoomAvoid(Room room) {
		for (int i = room.center.x - room.width / 2; i < room.center.x + room.width/2 + 1; i++) {
			
			for (int j = room.center.y - room.height / 2; j < room.center.y + room.height/2 + 1; j++) {
				
				this.avoidanceArray[i][j] = false;
				
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
	public void clearMap() {
		this.map = new int[this.width][this.height];
		// Arrays.fill(this.map, dunTemp.getWall(this.tempurature));
	}
	
	/**
	 * Prints the map object to the console as integers
	 * 
	 * @see         Generator
	 */
	public void debugPrintMap() {
		for (int[] row : this.map) {
		    System.out.println(Arrays.toString(row));
		}
	}
	
	/**
	 * Build the feature and return it
	 *
	 * @return      2D array of ints that represent the feature as tiles
	 * @see         Generator
	 */
	public int[][] build() {
		
		this.prepareTestHouses();
		this.buildTestHouses();
		this.jsonTest();
		
		return this.map;
		
	}
	
	public void jsonTest() {
		
		JSONObject obj = new JSONObject("{\"error\": \"message\" }");
		System.out.println(obj.get("error"));
		
	}
	
}