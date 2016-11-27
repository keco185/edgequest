package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class AttackManager extends Thread{
	DataManager dm;
	public AttackManager(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		boolean wasLeftMouseDown = false;
		while (dm.system.running) {
			try {
				if (dm.system.rightMouseDown) { //Is player aiming
					if (dm.system.leftMouseDown && !wasLeftMouseDown) { //Is player attacking
						performAttack();
					}
					wasLeftMouseDown = dm.system.leftMouseDown;
				}
				Thread.sleep(dm.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private boolean isWeapon(int slot) {
		if (dm.characterManager.characterEntity.getHeldItem(slot) != null) {
			return dm.characterManager.characterEntity.getHeldItem(slot).isWeapon;
		}
		return false;
	}
	private void performAttack() {
		BlockItem attackWeapon = null;
		String projectile = "";
		double offsetX = 0;
		double offsetY = 0;
		if (isWeapon(0)) {
			attackWeapon = dm.characterManager.characterEntity.getHeldItem(0);
			projectile = attackWeapon.projectile;
			offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(0);
			}
		} else if (isWeapon(1)) {
			attackWeapon = dm.characterManager.characterEntity.getHeldItem(1);
			offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			projectile = attackWeapon.projectile;
			dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(1);
			}
		}
		double maxRange = 0.5;
		double maxDamage = 1.0;
		double speed = 1.0;
		boolean ammoGood = true;
		if (attackWeapon != null) {
			maxRange = attackWeapon.range;
			maxDamage = attackWeapon.maxDamage;
			speed = attackWeapon.speed;
			if (!attackWeapon.ammo.equals("")) {
				ammoGood = dm.backpackManager.removeItemFromBackpack(dm.system.blockNameMap.get(attackWeapon.ammo));
			}
		}
		if (ammoGood) {
		double damage = getDamage(maxDamage);
		dm.savable.projectiles.add(new Projectile(speed, -dm.characterManager.characterEntity.getRot(), maxRange, damage, projectile, dm.characterManager.characterEntity.getX() + offsetX, dm.characterManager.characterEntity.getY() + offsetY, dm.characterManager.characterEntity.dungeon[0], dm.characterManager.characterEntity.dungeon[1], dm.characterManager.characterEntity.dungeonLevel));
		}
	}
	public double getDamage(double maxDamage) {
		if (Math.random() > 0.8) {
			return maxDamage;
		}
		return maxDamage / 2.0 * (1 + Math.random());
	}
}
