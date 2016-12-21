package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.projectiles.HandProjectile;
import com.mtautumn.edgequest.projectiles.Projectile;

public class Hands{
	public static Projectile[] createProjectiles(Entity entity) {
		return new Projectile[0];
	}
	public static Projectile[] createProjectiles(Entity entity, double offsetX, double offsetY) {
		return new Projectile[]{new HandProjectile(entity, 1, offsetX, offsetY)};
	}
}
