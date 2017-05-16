package com.mtautumn.edgequest.generator;

/*
 * Class to manage dungeon temperature and return tiles based on such temperatures
 * NOTE: Currently not fully implemented. Don't expect much.
 */

/**
 * Class to manage dungeon temperature and modify tiles based on it.
 * <p>
 * Currently unimplemented
 * 
 * @author Gray
 */
public class Temperature implements Overlay {

	/**
	 * Returns specific tile for the walls given the relative temperature of that
	 * tile.
	 * <p>
	 * Currently unimplemented
	 *
	 * @param  temperature the temperature of the given tile
	 * @return tile integer
	 * @see    Temperature
	 */
	public int getWall(double tempMap) {
		return Tiles.DIRT.getTile();
	}
	
	/**
	 * Returns specific tile for the floors given the relative temperature of that
	 * tile.
	 * <p>
	 * Currently unimplemented
	 *
	 * @param  tempMap the temperature of the given tile
	 * @return tile integer
	 * @see    Temperature
	 */
	public int getFloor(double temp) {
		return Tiles.FLOOR.getTile();
	}
	
	/**
	 * Returns specific tile for the liquids given the relative temperature of that
	 * tile.
	 * <p>
	 * Currently unimplemented
	 *
	 * @param  temperature the temperature of the given tile
	 * @return tile integer
	 * @see    Temperature
	 */
	public int getLiquid(double temp) {
		return Tiles.WATER.getTile();
	}
	
	@Override
	public int[][] overlay(double[][] tempMap, int[][] dunMap) {
		for (int i = 0; i < dunMap[0].length; i++) {
			
			for (int j = 0; j < dunMap.length; j++) {
				
				if (dunMap[i][j] == Tiles.FLOOR.getTile()) {
					dunMap[i][j] = getWall(tempMap[i][j]);
				} else if (i == Tiles.DIRT.getTile()) {
					dunMap[i][j] = getFloor(tempMap[i][j]);
				} else if (i == Tiles.WATER.getTile()) {
					dunMap[i][j] = getLiquid(tempMap[i][j]);
				} else {
				}
					
			}
			
		}
		
		return dunMap;
		
	}
	
}
