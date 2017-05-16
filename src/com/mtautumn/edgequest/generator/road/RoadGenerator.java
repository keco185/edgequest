package com.mtautumn.edgequest.generator.road;

import java.util.Random;

import com.mtautumn.edgequest.dataObjects.RoadState;

public class RoadGenerator {
	int v1X,v1Y,v2X,v2Y;
	long seed;
	RoadState[][] roadMap;
	int adjX, adjY = 0;
	public RoadGenerator(int village1X, int village1Y, int village2X, int village2Y, long gameSeed) {
		
		//Creates proper village order
		if (village1X < village2X) {
			v1X = village1X;
			v1Y = village1Y;
			v2X = village2X;
			v2Y = village2Y;
		} else if (village1X == village2X) {
			if (village1Y < village2Y) {
				v1X = village1X;
				v1Y = village1Y;
				v2X = village2X;
				v2Y = village2Y;
			} else {
				v2X = village1X;
				v2Y = village1Y;
				v1X = village2X;
				v1Y = village2Y;
			}
		} else {
			v2X = village1X;
			v2Y = village1Y;
			v1X = village2X;
			v1Y = village2Y;
		}
		seed = generateSeed(gameSeed, v1X, v1Y);
	}
	
	public void generate() {
		int minX = (v1X < v2X) ? v1X : v2X;
		int minY = (v1Y < v2Y) ? v1Y : v2Y;
		int v1XAdj = v1X - minX;
		int v2XAdj = v2X - minX;
		int v1YAdj = v1Y - minY;
		int v2YAdj = v2Y - minY;
		adjX = v1X - v1XAdj;
		adjY = v1Y - v1YAdj;
		int width = Math.abs(v2X-v1X);
		int height = Math.abs(v2Y - v1Y);
		roadMap = new RoadState[width+1][height+1];
		for (int i = 0; i < roadMap.length; i++) {
			for (int j = 0; j < roadMap[i].length; j++) {
				roadMap[i][j] = new RoadState();
			}
		}
		
		//Generation loop (code walks from village 1 to village 2
		Random rng = new Random(seed);
		int dX = v2XAdj - v1XAdj;
		int dY = v2YAdj - v1YAdj;
		
		int posX = v1XAdj;
		int posY = v1YAdj;
		while(posX != v2XAdj || posY != v2YAdj) {
			
			double randNum = rng.nextDouble();
			double distance = Math.abs(dX) + Math.abs(dY);
			double weightX = Math.abs(dX)/distance;
			
			if (randNum <= weightX) {
				// Move along x axis
				int movement = (int) Math.signum(dX);
				if (movement > 0) {
					roadMap[posX][posY].roadRight = true;
					roadMap[posX + 1][posY].roadLeft = true;
				} else {
					roadMap[posX][posY].roadLeft = true;
					roadMap[posX - 1][posY].roadRight = true;
				}
				dX -= movement;
				posX += movement;
				
			} else {
				// Move along y axis
				int movement = (int) Math.signum(dY);
				if (movement > 0) {
					roadMap[posX][posY].roadBottom = true;
					roadMap[posX][posY + 1].roadTop = true;
				} else {
					roadMap[posX][posY].roadTop = true;
					roadMap[posX][posY - 1].roadBottom = true;
				}
				dY -= movement;
				posY += movement;
			}
		}
	}
	
	public RoadState getRoads(int x, int y) {
		int posXAdj = x - adjX;
		int posYAdj = y - adjY;
		if (posXAdj >= 0 && posXAdj < roadMap.length && posYAdj >= 0 && posYAdj < roadMap[0].length) {
			return roadMap[posXAdj][posYAdj];
		}
		return new RoadState();
	}
	
	
	private static long generateSeed(long... vals) {
		long newSeed = vals[0];
		for (int i = 1; i < vals.length; i++) {
			newSeed = new Random(newSeed + vals[i]).nextLong();
		}
		return newSeed;
	}

}
