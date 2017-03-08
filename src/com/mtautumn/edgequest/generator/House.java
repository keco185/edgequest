package com.mtautumn.edgequest.generator;

/**
 * This class essentially wraps a 2D array of ints for express use of 
 * preparing to make houses in villages
 * <p>
 * Note: this class may be possibly renamed.
 * @author Gray
 *
 */
public class House {
	
	// Holds the house tile data
	int[][] struct;
	
	/**
	 * House constructor. Takes a 2D array of ints
	 * @param struct
	 * @see House
	 */
	public House(int[][] struct) {
		this.struct = struct;
	}
	
	/*
	 * Private methods
	 */
	
	/**
	 * Uses matrix magic to rotate itself 90 degrees
	 * @see House
	 */
	private void rotate90() {
		
		// Swap length and width
		final int M = this.struct.length;
		final int N = this.struct[0].length;
		
		// Create new array with new length and width
		int[][] ret = new int[N][M];
		
		// Fill new array
		for (int r = 0; r < M; r++) {
			for (int c = 0; c < N; c++) {
				ret[c][M-1-r] = this.struct[r][c];
			}
		}
		
		// Set
		this.struct = ret;
		
	}
	
	/*
	 * Public Methods
	 */
	
	/**
	 * Rotates the house 90 degrees n times
	 * @param n number of times to rotate
	 * @see House
	 */
	public void rotate(int n) {
		
		for (int i = 0; i < n; i++) {
			rotate90();
		}
		
	}
}
