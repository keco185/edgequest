package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.projectiles.Projectile;

public class Weapon extends BlockItem {
	private static final long serialVersionUID = 1L;
	public String[] ammoTypes;
	public byte wearPosition = -1; //-1 (nowhere), 0 (head), 1 (torso), 2 (legs), 3 (feet)
	public double defence = 0.0; //Percentage of damage removed when worn
	public Weapon(int id, boolean isBlock, boolean isItem, String name, int[] blockAnimation, int[] itemAnimation) {
		super(id, isBlock, isItem, name, blockAnimation, itemAnimation);
	}
	public Projectile[] createProjectiles(String ammo, Entity entity, double offsetX, double offsetY, boolean hand) { //Method is called when weapon is used. SHould be overwritten
		return new Projectile[0];
	}
	public boolean isCorrectAmmo(Ammo ammo) {
		for (int i = 0; i < ammoTypes.length; i++) {
			if (ammo.isName(ammoTypes[i])) {
				return true;
			}
		}
		return false;
	}
	public static int getDamage(int maxDamage) {
		if (Math.random() > 0.8) {
			return maxDamage;
		}
		return (int) (maxDamage / 2.0 * (1 + Math.random()));
	}
}
