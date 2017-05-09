package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class DamagePostManager extends Thread{
	@Override
	public void run() {
		while (SystemData.running) {
			try {
				for (int i = 0; i < DataManager.savable.damagePosts.size(); i++) {
					if (DataManager.savable.damagePosts.get(i).postTime < System.currentTimeMillis() - 1000) {
						DataManager.savable.damagePosts.remove(i);
						i--;
					}
				}
				Thread.sleep(SettingsData.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
