package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.projectiles.DaggerProjectile;
import com.mtautumn.edgequest.projectiles.Projectile;

public class DaggerWeapon extends Weapon{
	private static final long serialVersionUID = 1L;

	public DaggerWeapon(int id, DataManager dm) {
		super(id, false, true, "dagger", null, new int[]{0}, dm);
		ammoTypes = new String[0];
		maxHealth = 50;
	}
	@Override
	public Projectile[] createProjectiles(String ammo, Entity entity, double offsetX, double offsetY, boolean hand) { //Method is called when weapon is used. SHould be overwritten
		return new Projectile[]{new DaggerProjectile(15, 0.8, entity, getDamage(4), offsetX, offsetY, true, 50)};
	}
}
