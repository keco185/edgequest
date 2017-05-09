package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.projectiles.Projectile;

public class ProjectileManager extends Thread {

	@Override
	public void run() {
		while(DataManager.system.running) {
			try {
				for(int i = 0; i < DataManager.savable.projectiles.size(); i++) {
					Projectile projectile = DataManager.savable.projectiles.get(i);
					if (projectile.advance()) {
						DataManager.savable.projectiles.remove(i);
						i--;
					} else {
						if (projectile.increment > projectile.maxIncrement || projectile.inStructure()) {
							DataManager.savable.projectiles.remove(i);
							i--;
						}
					}
				}
				Thread.sleep(DataManager.settings.tickLength / 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
