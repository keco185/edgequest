package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class DamagePostManager extends Thread{
	DataManager dm;
	public DamagePostManager(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		while (dm.system.running) {
			try {
				for (int i = 0; i < dm.savable.damagePosts.size(); i++) {
					if (dm.savable.damagePosts.get(i).postTime < System.currentTimeMillis() - 1000) {
						dm.savable.damagePosts.remove(i);
						i--;
					}
				}
				Thread.sleep(dm.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
