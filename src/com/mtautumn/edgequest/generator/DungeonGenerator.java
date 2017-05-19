package com.mtautumn.edgequest.generator;

import java.util.Random;

import com.mtautumn.edgequest.generator.automata.DrunkardsWalk;
import com.mtautumn.edgequest.generator.overlay.Cave;
import com.mtautumn.edgequest.generator.overlay.ClassicDungeon;
import com.mtautumn.edgequest.generator.overlay.Temperature;
import com.mtautumn.edgequest.generator.room.Center;

/**
 * This class is used to make a 2D Array of 'Tiles' as a representation
 * of dungeons that will be converted into actual blocks in the game world.
 * 
 * @see Generator
 * @author Gray
 * 
 */
public class DungeonGenerator implements Generator {
	
	//2D array with false blocks being areas that cannot be built on
	boolean[][] avoidanceArray;
	
	// Variables asked for in the constructor
	int width;
	int height;
	int maxRooms;
	
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
	
	// classicDungeon object
	ClassicDungeon classicDungeon;
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
		this.avoidanceArray = new boolean[width][height];
		
		// TODO: Pass temp to dungeon
		this.temperatureMap = temperatureMap;
		
		// Initialize a map. Default all values are set to 0s (walls)
		this.map = new int[width][height];
		
		// Init start
		this.start = start;
		
		// Init classic dungeon overlay
		this.classicDungeon = new ClassicDungeon(width, height, maxRooms, seed, start, avoidanceArray);
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
		int ponds = rng.nextInt(3) - 1;
		
		while (ponds > 0) {
			
			int x = rng.nextInt(map.length);
			int y = rng.nextInt(map[0].length);
			
			map = drunkWalk.pondWalk(map, x, y);
			
			ponds -= 1;
			
		}
		
	}
	
	/**
	 * Apply temperature map to dungeon
	 * 
	 * @see Temperature
	 * @see DungeonGenerator
	 */
	private void applyTemperature() {
		dunTemp.overlay(temperatureMap, map);
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
		
		clearMap();
		// Make dungeon
		map = classicDungeon.overlay(map);
		// Apply cave
		map = cave.makeAndApplyCave(map, 0.1f);	
		// Add ponds
		addPonds();
		// Add structures
		map = classicDungeon.addStructures(map);
		
		applyTemperature();
		
		// Add stairs last, to avoid problems where stairs can be overwritten
		map = classicDungeon.addStairs(map);
		
		// this.debugPrintMap();
		
		return map;

	}

}
