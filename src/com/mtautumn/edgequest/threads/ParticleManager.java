package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;

public class ParticleManager extends Thread {
	DataManager dm;
	public ParticleManager(DataManager dm) {
		this.dm = dm;
	}
	@Override
	public void run() {
		while(dm.system.running) {
			try {
				for (int i = 0; i < dm.savable.particles.size(); i++) {
					if (dm.savable.particles.get(i).update(dm)) {
						dm.savable.particles.remove(i);
						i--;
					}
				}
				Thread.sleep(dm.settings.tickLength / 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
