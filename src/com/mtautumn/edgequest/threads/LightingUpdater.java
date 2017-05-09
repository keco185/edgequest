package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.LightSource;

public class LightingUpdater extends Thread {
	@Override
	public void run() {
		while(SystemData.running) {
			try {
				for (int i = 0; i < DataManager.blockUpdateManager.lightingQueue.size(); i++) {
					DataManager.blockUpdateManager.lighting.update(DataManager.blockUpdateManager.lightingQueue.get(i));
					DataManager.blockUpdateManager.lightingQueue.remove(i);
					i--;
				}
				for (LightSource light : DataManager.savable.lightSources) {
					light.update();
				}
				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}