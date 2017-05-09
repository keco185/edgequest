package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;

public class ParticleManager extends Thread {
	@Override
	public void run() {
		while(DataManager.system.running) {
			try {
				for (int i = 0; i < DataManager.savable.particles.size(); i++) {
					if (DataManager.savable.particles.get(i).update()) {
						DataManager.savable.particles.remove(i);
						i--;
					}
				}
				Thread.sleep(DataManager.settings.tickLength / 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
