package com.mtautumn.edgequest.generator;

import java.util.Random;

/**
 * Documentation Pending
 * @author Gray
 *
 */
public class House {
	
	int[][] struct;
	
	public House(int[][] struct) {
		this.struct = struct;
	}
	
	public void rotate(int n) {
		for (int i = 0; i < n; i++) {
			rotate90();
		}
	}
	
	private void rotate90() {
		final int M = this.struct.length;
		final int N = this.struct[0].length;
		int[][] ret = new int[N][M];
		for (int r = 0; r < M; r++) {
			for (int c = 0; c < N; c++) {
				ret[c][M-1-r] = this.struct[r][c];
			}
		}
		
		this.struct = ret;
		
	}

}
