package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.projectiles.BulletProjectile;
import com.mtautumn.edgequest.projectiles.Projectile;

public class PistolWeapon extends Weapon{
	private static final long serialVersionUID = 1L;

	public PistolWeapon(int id, DataManager dm) {
		super(id, false, true, "pistol", null, new int[]{0}, dm);
		ammoTypes = new String[]{"bullet"};
		maxHealth = 50;
	}
	public Projectile[] createProjectiles(String ammo, Entity entity, double offsetX, double offsetY, boolean hand) { //Method is called when weapon is used. SHould be overwritten
		entity.lastSpeedX -= Math.cos(-entity.getRot()) * 0.07;
		entity.lastSpeedY += Math.sin(-entity.getRot()) * 0.07;
		return new Projectile[]{new BulletProjectile(30, 15, entity, getDamage(5), offsetX, offsetY)};
	}
}
