package com.mtautumn.edgequest.generator.overlay;

/**
 * This interface is for overlays, or classes that add two maps together
 * @author Gray
 *
 */
@FunctionalInterface
public interface Overlay {
	
	/**
	 * This is the main method that combines two maps
	 * @param map    map to overlay
	 * @param dunMap dungeon map that will have the overlay applied to
	 * @return       returns dungeon map, modified
	 */
	public int[][] overlay(double[][] map, int[][] dunMap);

}
