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
		if (isWeapon(0)) {
			attackWeapon = dm.characterManager.characterEntity.getHeldItem(0);
			dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(0);
			}
		} else {
			attackWeapon = dm.characterManager.characterEntity.getHeldItem(1);
			dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(1);
			}
		}
		double maxRange = 1.0;
		double minAngle = -dm.characterManager.characterEntity.getRot() - 0.6;
		double maxAngle = -dm.characterManager.characterEntity.getRot() + 0.6;
		double maxDamage = 1.0;
		if (attackWeapon != null) {
			maxRange = attackWeapon.range;
			minAngle = -dm.characterManager.characterEntity.getRot() - attackWeapon.weaponSpread/2.0;
			maxAngle = -dm.characterManager.characterEntity.getRot() + attackWeapon.weaponSpread/2.0;
			maxDamage = attackWeapon.maxDamage;
		}
		if (minAngle < - 6.28) {
			for (int i = 0; i < dm.savable.entities.size(); i++) {
				Entity entity = dm.savable.entities.get(i);
				if (entity.getType() != Entity.EntityType.character) {
					if (getRange(entity) < maxRange) {
						double entityAngle = getAngle(entity);
						if (entityAngle < maxAngle || entityAngle > minAngle + 6.28) {
							damageEntity(i, maxDamage);
						}
					}
				}
			}
		} else if (maxAngle > 6.28) {
			for (int i = 0; i < dm.savable.entities.size(); i++) {
				Entity entity = dm.savable.entities.get(i);
				if (entity.getType() != Entity.EntityType.character) {
					if (getRange(entity) < maxRange) {

						double entityAngle = getAngle(entity);
						if (entityAngle > minAngle || entityAngle < maxAngle - 6.28) {
							damageEntity(i, maxDamage);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < dm.savable.entities.size(); i++) {
				Entity entity = dm.savable.entities.get(i);
				if (entity.getType() != Entity.EntityType.character) {
					if (getRange(entity) < maxRange) {

						double entityAngle = getAngle(entity);
						if (entityAngle < maxAngle && entityAngle > minAngle) {
							damageEntity(i, maxDamage);
						}
					}
				}
			}
		}
	}
	private static double getRange(Entity entity) {
		return entity.distanceToPlayer();
	}
	private double getAngle(Entity entity) {
		return Math.atan2(dm.characterManager.characterEntity.getY() - entity.getY(), entity.getX() - dm.characterManager.characterEntity.getX());
	}
	public void damageEntity(int location, double maxDamage) {
		if (Math.random() > 0.8) {
			dm.savable.entities.get(location).health -= maxDamage;
		} else {
			dm.savable.entities.get(location).health -= maxDamage / 2.0 * (1 + Math.random());
		}
		if (dm.savable.entities.get(location).health < 0) dm.savable.entities.get(location).health = 0;
	}
}
