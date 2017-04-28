package com.mtautumn.edgequest.generator;

// TODO Javadoc
public abstract class Structure {
	// Holds the structure tile data
	int[][] struct;
		
	/**
	 * Structure constructor. Takes a 2D array of ints
	 * @param struct
	 * @see Structure
	 */
	public Structure(int[][] struct) {
		this.struct = struct;
	}
		
	/*
	 * Private methods
	 */
		
	/**
	 * Uses matrix magic to rotate itself 90 degrees
	 * @see Structure
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
	 * Rotates the structure 90 degrees n times
	 * @param n number of times to rotate
	 * @see Structure
	 */
	public void rotate(int n) {
			
		for (int i = 0; i < n; i++) {
			rotate90();
		}
		
	}
}
