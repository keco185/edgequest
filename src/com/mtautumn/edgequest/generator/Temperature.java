package com.mtautumn.edgequest.generator;

/*
 * Class to manage dungeon temperature and return tiles based on such temperatures
 * NOTE: Currently not fully implemented. Don't expect much.
 */

/**
 * Class to manage dungeon temperature and modify tiles based on it.
 * <p>
 * Currently unimplemented
 */
public class Temperature {

	/**
	 * Returns specific tile for the walls given the relative temperature of that
	 * tile.
	 * <p>
	 * Currently unimplemented
	 *
	 * @param  temperature the temperature of the given tile
	 * @return             tile integer
	 * @see                Temperature
	 */
	public int getWall(double temperature) {
		return Tile.DIRT;
	}
	
}
