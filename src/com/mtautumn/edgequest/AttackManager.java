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
		Entity attackedEntity = null;
		if (isWeapon(0)) {
			attackWeapon = dm.characterManager.characterEntity.getHeldItem(0);
			dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(0);
			}
		} else if (isWeapon(1)) {
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
							if (attackedEntity == null) {
								attackedEntity = entity;
							} else if (attackedEntity.distanceToPlayer() > entity.distanceToPlayer()) {
								attackedEntity = entity;
							}
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
							if (attackedEntity == null) {
								attackedEntity = entity;
							} else if (attackedEntity.distanceToPlayer() > entity.distanceToPlayer()) {
								attackedEntity = entity;
							}
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
							if (attackedEntity == null) {
								attackedEntity = entity;
							} else if (attackedEntity.distanceToPlayer() > entity.distanceToPlayer()) {
								attackedEntity = entity;
							}
						}
					}
				}
			}
		}
		damageEntity(attackedEntity, maxDamage);
	}
	private static double getRange(Entity entity) {
		return entity.distanceToPlayer();
	}
	private double getAngle(Entity entity) {
		return Math.atan2(dm.characterManager.characterEntity.getY() - entity.getY(), entity.getX() - dm.characterManager.characterEntity.getX());
	}
	public void damageEntity(Entity entity, double maxDamage) {
		if (entity != null) {
			if (Math.random() > 0.8) {
				entity.health -= maxDamage;
			} else {
				entity.health -= maxDamage / 2.0 * (1 + Math.random());
			}
			if (entity.health < 0) entity.health = 0;
		}
	}
}
