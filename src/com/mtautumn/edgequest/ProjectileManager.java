package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class ProjectileManager extends Thread {
	DataManager dm;
	public ProjectileManager(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		while(dm.system.running) {
			try {
				for(int i = 0; i < dm.savable.projectiles.size(); i++) {
					Projectile projectile = dm.savable.projectiles.get(i);
					if (projectile.advance(dm)) {
						Entity entity = projectile.getEntityIn(dm);
						entity.health -= projectile.damage;
						double damage = projectile.damage;
						dm.savable.projectiles.remove(i);
						i--;
						if (entity.health < 0) {
							damage += entity.health;
							entity.health = 0;
						}
						dm.savable.damagePosts.add(new DamagePost(entity, (int) damage));

					} else {
						if (projectile.distance() > projectile.maxDistance || projectile.inStructure(dm)) {
							dm.savable.projectiles.remove(i);
							i--;
						}
					}
				}
				Thread.sleep(dm.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}