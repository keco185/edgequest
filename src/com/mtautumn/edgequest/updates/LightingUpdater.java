package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;

public class LightingUpdater extends Thread {
	DataManager dm;
	public LightingUpdater(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		while(dm.system.running) {
			try {
				for (int i = 0; i < dm.blockUpdateManager.lightingQueue.size(); i++) {
					dm.blockUpdateManager.lighting.update(dm.blockUpdateManager.lightingQueue.get(i));
					dm.blockUpdateManager.lightingQueue.remove(i);
					i--;
				}
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
