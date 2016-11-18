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
	private void performAttack() {
		double maxRange = 1.0;
		double minAngle = -dm.characterManager.characterEntity.getRot() - 0.3;
		double maxAngle = -dm.characterManager.characterEntity.getRot() + 0.3;
		if (minAngle < - 6.28) {
			for (int i = 0; i < dm.savable.entities.size(); i++) {
				Entity entity = dm.savable.entities.get(i);
				if (entity.getType() != Entity.EntityType.character) {
					if (getRange(entity) < maxRange) {
						double entityAngle = getAngle(entity);
						if (entityAngle < maxAngle || entityAngle > minAngle + 6.28) {
							damageEntity(entity, i);
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
							damageEntity(entity, i);
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
							damageEntity(entity, i);
						}
					}
				}
			}
		}
	}
	private double getRange(Entity entity) {
		return Math.sqrt(Math.pow(entity.getX() - dm.characterManager.characterEntity.getX(), 2) + Math.pow(entity.getY() - dm.characterManager.characterEntity.getY(), 2));
	}
	private double getAngle(Entity entity) {
		return Math.atan2(dm.characterManager.characterEntity.getY() - entity.getY(), entity.getX() - dm.characterManager.characterEntity.getX());
	}
	public void damageEntity(Entity entity, int location) {
		dm.savable.entities.remove(location);
	}
}
