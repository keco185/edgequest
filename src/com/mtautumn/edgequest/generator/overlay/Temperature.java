package com.mtautumn.edgequest.generator.overlay;

import com.mtautumn.edgequest.generator.tile.Tiles;

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
	public int getWall(double temp) {
		if (temp < 3550) {
			return Tiles.SNOW_WALL.getTile();
		} else if (temp > 4180) {
			return Tiles.SAND_WALL.getTile();
		} else {
			return Tiles.DIRT_WALL.getTile();
		}
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
		if (temp < 3550) {
			return Tiles.SNOW_FLOOR.getTile();
		} else if (temp > 4180) {
			return Tiles.SAND_FLOOR.getTile();
		} else {
			return Tiles.STONE_FLOOR.getTile();
		}
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
		if (temp < 3550) {
			return Tiles.ICE.getTile();
		} else {
			return Tiles.WATER.getTile();
		}
		
	}
	
	@Override
	public int[][] overlay(double[][] tempMap, int[][] dunMap) {
		for (int i = 0; i < dunMap[0].length; i++) {
			
			for (int j = 0; j < dunMap.length; j++) {
				
				if (dunMap[i][j] == Tiles.DIRT_WALL.getTile()) {
					dunMap[i][j] = getWall(tempMap[i][j]);
				} else if (dunMap[i][j] == Tiles.WATER.getTile()) {
					dunMap[i][j] = getLiquid(tempMap[i][j]);
				} else if (dunMap[i][j] == Tiles.STONE_FLOOR.getTile()) {
					dunMap[i][j] = getFloor(tempMap[i][j]);
				} else {
				}
					
			}
			
		}
		
		/*
		for (int i = 0; i < tempMap[0].length; i++) {
			
			for (int j = 0; j < tempMap.length; j++) {
				
				System.out.print(tempMap[i][j] + " ");
					
			}
			
			System.out.print("\n");
			
		}*/
		
		return dunMap;
		
	}
	
}
