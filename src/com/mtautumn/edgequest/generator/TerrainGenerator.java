/* Algorithm for generating the overworld terrain. An instance is created
 * followed by calling generate block for each block that needs to be made
 */
package com.mtautumn.edgequest.generator;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class TerrainGenerator {
	Map<String,Double> altNoiseMap = new ConcurrentHashMap<String, Double>();
	Map<String,Double> tempNoiseMap = new ConcurrentHashMap<String, Double>();

	Map<String,Double> altitudeMap = new ConcurrentHashMap<String, Double>();
	Map<String,Double> temperatureMap = new ConcurrentHashMap<String, Double>();

	Map<String,Double> altitudeMapFiltered = new ConcurrentHashMap<String, Double>();
	Map<String,Double> temperatureMapFiltered = new ConcurrentHashMap<String, Double>();
	public TerrainGenerator() {
		altSeedBase = generateSeed(DataManager.savable.seed,1);
		tempSeedBase = generateSeed(DataManager.savable.seed,22);
		rngSeedBase = generateSeed(DataManager.savable.seed,413);
	}
	public static long generateSeed(long... vals) {
		long newSeed = vals[0];
		for (int i = 1; i < vals.length; i++) {
			newSeed = new Random(newSeed + vals[i]).nextLong();
		}
		return newSeed;
	}
	private long altSeedBase = 0;
	private long tempSeedBase = 0;
	private long rngSeedBase = 0;
	public void clearCache() {
		altitudeMap.clear();
		temperatureMap.clear();
		altitudeMapFiltered.clear();
		temperatureMapFiltered.clear();
		altNoiseMap.clear();
		tempNoiseMap.clear();
		altSeedBase = generateSeed(DataManager.savable.seed,1);
		tempSeedBase = generateSeed(DataManager.savable.seed,2);
		rngSeedBase = generateSeed(DataManager.savable.seed,3);
	}
	private double getRNG(int x, int y) {
		return new Random(generateSeed(rngSeedBase,x,y)).nextDouble();
	}
	private double getAltNoise(long x, long y) {
		try {
			if (!altNoiseMap.containsKey(x + "," + y)) {
				altNoiseMap.put(x+","+y, new Random(generateSeed(altSeedBase,x,y)).nextDouble());
			}
			return altNoiseMap.get(x + "," + y);
		} catch (Exception e) {
			altNoiseMap.put(x+","+y, new Random(generateSeed(altSeedBase,x,y)).nextDouble());
			return new Random(generateSeed(altSeedBase,x,y)).nextDouble();
		}
	}
	private double getTempNoise(long x, long y) {
		try {
			if (!tempNoiseMap.containsKey(x + "," + y)) {
				tempNoiseMap.put(x+","+y, new Random(generateSeed(tempSeedBase,x,y)).nextDouble());
			}
			return tempNoiseMap.get(x+","+y);
		} catch (Exception e) {
			tempNoiseMap.put(x+","+y, new Random(generateSeed(tempSeedBase,x,y)).nextDouble());
			return new Random(generateSeed(tempSeedBase,x,y)).nextDouble();
		}
	}
	public double[] getBlockStats(int x, int y) {
		double[] stats = new double[2];
		boolean tryIt = true;
		while (tryIt) {
			try {
				if (altitudeMap.containsKey(x + "," + y)) {
					stats[0] = altitudeMap.get(x + "," + y);
				}
				if (temperatureMap.containsKey(x + "," + y)) {
					stats[1] = temperatureMap.get(x + "," + y);
				}
				tryIt = false;
			} catch (Exception e) {
			}
		}
		int chunkX = (int) Math.floor(x / SettingsData.chunkSize);
		int chunkY = (int) Math.floor(y / SettingsData.chunkSize);
		if (stats[0] == 0) {
			double chunkRNGSum = 0;
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (i != 0 || j != 0) {
						chunkRNGSum += getAltNoise(chunkX + i, chunkY + j);
					}
				}
			}
			stats[0] = chunkRNGSum + 1;
			altitudeMap.put(x+","+y, stats[0]);
		}
		if (stats[1] == 0) {
			double chunkRNGSum = 0;
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					if (i != 0 || j != 0) {
						chunkRNGSum += getTempNoise(chunkX + i, chunkY + j);
					}
				}
			}
			stats[1] = chunkRNGSum + 1;
			temperatureMap.put(x+","+y, stats[1]);
		}
		return stats;
	}
	public void generateBlock(int x, int y) {
		try {
		double averageAltitude = 0;
		double averageTemperature = 0;
		for (int i = -8; i<=8; i++) {
			for (int j = -8; j <= 8; j++) {
				double[] stats = getBlockStats(x + i, y + j);
				averageAltitude += stats[0];
				averageTemperature += stats[1];
			}
		}
		altitudeMapFiltered.put(x + "," + y, averageAltitude);
		temperatureMapFiltered.put(x + "," + y, averageTemperature);
		createBlockForStats(x, y);
		} catch (Exception e) {
			
		}
	}
	public void createBlockForStats(int x, int y) {
		double alt = altitudeMapFiltered.get(x+","+y); //between 1 and 4097
		double temp = temperatureMapFiltered.get(x+","+y);
		if (alt < 3600) {
			if (temp < 3550) {
				if (alt < 3500) {
					DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("water").getID());
				} else {
					DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("ice").getID());
				}
			} else {
				DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("water").getID());
				if (getRNG(x, y) > 0.996) {
					DataManager.world.ou.setStructBlock(x, y, SystemData.blockNameMap.get("lilyPad").getID());
				}
			}
		} else if (alt < 3650 && temp > 3550){
			DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("sand").getID());
		} else {
			if (temp < 3550) {
				DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("snow").getID());
			} else if (temp > 4180){
				DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("sand").getID());
			} else  if (alt < 4290 ){
				DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("grass").getID());
				if (getRNG(x, y) < 0.01) {
					DataManager.world.ou.setStructBlock(x, y, SystemData.blockNameMap.get("tree").getID());
				}
			} else {
				if (getRNG(x, y) < 0.9) {
					DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("stone").getID());
				} else { 
					DataManager.world.ou.setGroundBlock(x, y, SystemData.blockNameMap.get("dirt").getID());
				}
			}
		}
	}
}
