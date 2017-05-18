package com.mtautumn.edgequest.generator.overlay;

import java.util.Random;

import com.mtautumn.edgequest.generator.VillageGenerator;
import com.mtautumn.edgequest.generator.room.Center;
import com.mtautumn.edgequest.generator.room.Room;
import com.mtautumn.edgequest.generator.structure.Houses;
import com.mtautumn.edgequest.generator.structure.VillageFeature;

public class Village implements RoomOverlay {
	
	/*
	 * Derived from constructor inputs
	 */
	
	int width;
	int height;
	int maxHouses;
	long seed;
	Center start;
	Random rng;
	boolean[][] avoidanceMap;
	
	/*
	 * Derived in constructor
	 */
	
	int currentMaxRooms;
	public Room[] houses;
	
	
	public Village(int width, int height, int maxHouses, long seed, Center start, boolean[][] avoidanceMap) {
		
		this.width = width;
		this.height = height;
		this.maxHouses = maxHouses;
		this.seed = seed;
		this.start = start;
		this.rng = new Random(seed);
		
		// Init start
		this.start = start;

		// Get a current number of houses based on a random value
		this.currentMaxRooms = rng.nextInt(maxHouses) + (int) Math.floor(maxHouses / 2) + 2;

		// Initialize the array of houses as rooms (Temporary)
		this.houses = new Room[currentMaxRooms];
		
		// Init start house (Temp)
		this.houses[0] = new Room(Houses.townhall, start);
		this.avoidanceMap = addRoomAvoid(houses[0], avoidanceMap);
		
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
		
		int failed = 0;
		
		// Fill the array of rooms with rooms of a random location and size (reasonably based on map size)
		for (int i = 1; i < currentMaxRooms; i++ ) {
			
			int tries = 0;
			int k = i - failed;
			
			do {
				VillageFeature h = getRandomHouse();
				h.rotate(rng.nextInt(4));
				houses[k] = new Room(h, rng.nextInt(width), rng.nextInt(height));
				tries++;
			} while(!roomOk(houses[k], width, height, avoidanceMap) && tries < 100);
			
			if (tries >= 1000) {
				currentMaxRooms--;
				failed++;
			} else {
				avoidanceMap = addRoomAvoid(houses[k], avoidanceMap);
			}
		}
	}
	
	/**
	 * Get a random house from the list of houses to use
	 * @return a random house object
	 * @see VillageFeature
	 * @see Houses
	 * @see VillageGenerator
	 */
	private VillageFeature getRandomHouse() {
		int h = rng.nextInt(Houses.HouseList.length);
	    return Houses.HouseList[h];
	}

	@Override
	public int[][] overlay(int[][] map) {
		prepareTestHouses();
		for (int i = 0; i < currentMaxRooms; i++ ) {
			
			// Get current room
			Room room = houses[i];
			
			for (int w = 0; w < room.width; w++) {
				for (int h = 0; h < room.height; h++) {
					boolean bounds = (w + room.xLoc < this.width) && (h + room.yLoc < height) && (w + room.xLoc >= 0) && (h + room.yLoc >= 0);
					if (bounds) {
						map[w + room.xLoc][h + room.yLoc] = room.room[h][w];
					}
				}
			}
				
		}
		
		return map;
	}

}
