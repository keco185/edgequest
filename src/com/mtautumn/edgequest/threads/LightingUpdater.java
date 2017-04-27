package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.LightSource;

public class LightingUpdater extends Thread {
	DataManager dm;
	public LightingUpdater(DataManager dm) {
		this.dm = dm;
	}
	@Override
	public void run() {
		while(dm.system.running) {
			try {
				for (int i = 0; i < dm.blockUpdateManager.lightingQueue.size(); i++) {
					dm.blockUpdateManager.lighting.update(dm.blockUpdateManager.lightingQueue.get(i));
					dm.blockUpdateManager.lightingQueue.remove(i);
					i--;
				}
				for (LightSource light : dm.savable.lightSources) {
					light.update();
				}
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}