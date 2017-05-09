package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;

public class DamagePostManager extends Thread{
	@Override
	public void run() {
		while (DataManager.system.running) {
			try {
				for (int i = 0; i < DataManager.savable.damagePosts.size(); i++) {
					if (DataManager.savable.damagePosts.get(i).postTime < System.currentTimeMillis() - 1000) {
						DataManager.savable.damagePosts.remove(i);
						i--;
					}
				}
				Thread.sleep(DataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
