package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.projectiles.Projectile;

public class ProjectileManager extends Thread {
	DataManager dm;
	public ProjectileManager(DataManager dm) {
		this.dm = dm;
	}
	@Override
	public void run() {
		while(dm.system.running) {
			try {
				for(int i = 0; i < dm.savable.projectiles.size(); i++) {
					Projectile projectile = dm.savable.projectiles.get(i);
					if (projectile.advance(dm)) {
						dm.savable.projectiles.remove(i);
						i--;
					} else {
						if (projectile.increment > projectile.maxIncrement || projectile.inStructure(dm)) {
							dm.savable.projectiles.remove(i);
							i--;
						}
					}
				}
				Thread.sleep(dm.settings.tickLength / 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
