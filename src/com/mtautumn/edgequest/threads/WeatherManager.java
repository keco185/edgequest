package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.particles.Particle;
import com.mtautumn.edgequest.particles.PrecipitationParticle;

public class WeatherManager extends Thread {
	public WeatherManager() {
		targetDryness = DataManager.savable.dryness;
	}
	public void startRain() {
		iterator = 0;
		targetDryness = -1;
		deltaStep = 0;
		DataManager.savable.dryness = -1;
	}
	public void endRain() {
		iterator = 0;
		targetDryness = 0.8;
		deltaStep = 0;
		DataManager.savable.dryness = 0.8;
	}
	int iterator = 0;
	double targetDryness;
	double deltaStep = 0;
	@Override
	public void run() {
		while(SystemData.running) {
			try {
				iterator++;
				if (iterator > 700) {
					targetDryness = DataManager.savable.dryness + (Math.random() - 0.5) / 2.0;
					iterator = 0;
					if (targetDryness < -1) {
						targetDryness = -1;
					}
					if (targetDryness > 1) {
						targetDryness = 1;
					}
					deltaStep = (targetDryness - DataManager.savable.dryness) / 700.0;
				} else {
					DataManager.savable.dryness += deltaStep;
				}
				for (int i = 0; i < DataManager.savable.precipitationParticles.size(); i++) {
					if (DataManager.savable.precipitationParticles.get(i).update()) {
						DataManager.savable.precipitationParticles.remove(i);
						i--;
					}
				}
				if (DataManager.savable.dryness < -0.2) {
					for(int i = 0; i < 5; i++) {
					if (Math.random() < -DataManager.savable.dryness) {
						double range = Double.valueOf(SystemData.maxTileX - SystemData.minTileX) * 1.5;
						double min = (SystemData.maxTileX + SystemData.minTileX - range) / 2.0;
						double x = Math.random() * range + min;
						double y = SystemData.minTileY - 4;
						Particle newRain = new PrecipitationParticle(x, y, -1, 0, Math.random() / 2.0 + 0.5, 0.6, 0.6);
						DataManager.savable.precipitationParticles.add(newRain);
					}
					}
				}
				Thread.sleep(17);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
