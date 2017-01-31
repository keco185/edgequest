package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class WeatherManager extends Thread {
	DataManager dm;
	public WeatherManager(DataManager dm) {
		this.dm = dm;
		targetDryness = dm.savable.dryness;
	}
	public void startRain() {
		iterator = 0;
		targetDryness = -1;
		deltaStep = 0;
	}
	int iterator = 0;
	double targetDryness;
	double deltaStep = 0;
	public void run() {
		while(dm.system.running) {
			try {
				iterator++;
				if (iterator > 700) {
					targetDryness = dm.savable.dryness + (Math.random() - 0.5) / 2.0;
					iterator = 0;
					if (targetDryness < -1) targetDryness = -1;
					if (targetDryness > 1) targetDryness = 1;
					deltaStep = (targetDryness - dm.savable.dryness) / 200.0;
				} else {
					dm.savable.dryness += deltaStep;
				}
				for (int i = 0; i < dm.savable.precipitationParticles.size(); i++) {
					if (dm.savable.precipitationParticles.get(i).update(dm)) {
						dm.savable.precipitationParticles.remove(i);
						i--;
					}
				}
				if (dm.savable.dryness < -0.2) {
					for(int i = 0; i < 5; i++) {
					if (Math.random() < -dm.savable.dryness) {
						double range = Double.valueOf(dm.system.maxTileX - dm.system.minTileX) * 1.5;
						double min = (dm.system.maxTileX + dm.system.minTileX - range) / 2.0;
						double x = Math.random() * range + min;
						double y = dm.system.minTileY - 4;
						Particle newRain = new PrecipitationParticle(x, y, -1, 0, Math.random() / 2.0 + 0.5, 0.6, 0.6);
						dm.savable.precipitationParticles.add(newRain);
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
