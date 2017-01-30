package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.projectiles.HandProjectile;

public class Pet extends Entity {
	private static final long serialVersionUID = 1L;
	private double lastPlayerX = 0;
	private double lastPlayerY = 0;
	private Entity attackEntity;

	public Pet(double posX, double posY, double rotation, DataManager dm, int dungeonLevel, int[] dungeon) {
		super("pet",EntityType.pet, posX, posY, rotation, dungeonLevel, dm);
		super.stillAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.moveSpeed = 1.2;
		super.maxHealth = 5;
		super.health = 5;
	}
	public Pet(Entity entity) {
		super("pet",EntityType.pet, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel, entity.dm);
		dungeonLevel = entity.dungeonLevel;
		super.stillAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.moveSpeed = 1.2;
		super.maxHealth = 5;
		super.health = 5;
	}
	public Pet() {
		super();
		super.stillAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.moveSpeed = 1.2;
	}
	long lastPlayerPosUpdate = 0;
	public void attackEntity(Entity entity) {
		attackEntity = entity;
	}
	private int attackStage = 0;
	public void update() {
		if (attackEntity != null) {
			if (attackEntity.dungeonLevel == dungeonLevel) {
				double deltaX = attackEntity.getX() - getX();
				double deltaY = attackEntity.getY() - getY();
				updateRotation(deltaX, deltaY);
				if (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < 1.2 && attackStage == 0) {
					dm.savable.projectiles.add(new HandProjectile(this, 2));
					attackStage = 1;
				} else if (attackStage == 0){
					double moveX = Math.cos(rotation) * moveSpeed / 15.0;
					double moveY = Math.sin(rotation) * moveSpeed / 15.0;
					move(moveX, moveY);
				} else if (attackStage == 1 && Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < 1.5) {
					double moveX = -Math.cos(rotation) * moveSpeed / 30.0;
					double moveY = -Math.sin(rotation) * moveSpeed / 30.0;
					move(moveX, moveY);
					if (dm.world.isStructBlock(this, (int)(getX() + moveX), (int)(getY() + moveY))) {
						attackEntity = null;
						attackStage = 0;
					}
				} else {
					attackEntity = null;
					attackStage = 0;
				}
			} else {
				attackEntity = null;
				attackStage = 0;
			}
		} else {
			if (isLineOfSight(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY())) {
				double deltaX = dm.characterManager.characterEntity.getX() - getX();
				double deltaY = dm.characterManager.characterEntity.getY() - getY();
				updateRotation(deltaX, deltaY);
				if (distanceToPlayer() > 2 && dm.characterManager.characterEntity.dungeonLevel == dungeonLevel) {
					double moveX = Math.cos(rotation) * moveSpeed / 30.0;
					double moveY = Math.sin(rotation) * moveSpeed / 30.0;
					move(moveX, moveY);
				} else {
					move(0,0);
				}
				super.path = null;
			} else if ((distanceToPlayer() > 2 || !isLineOfSight(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY())) && dm.characterManager.characterEntity.dungeonLevel == dungeonLevel) {
				if (Math.abs(lastPlayerPosUpdate - System.currentTimeMillis()) > 2000) {
					if (lastPlayerX != dm.characterManager.characterEntity.getX() || lastPlayerY != dm.characterManager.characterEntity.getY()) {
						lastPlayerX = dm.characterManager.characterEntity.getX();
						lastPlayerY = dm.characterManager.characterEntity.getY();
						super.setDestination((int)lastPlayerX, (int) lastPlayerY);
						lastPlayerPosUpdate = System.currentTimeMillis();
					}
				}
				super.update();
			} else {
				super.path = null;
				move(0,0);
			}
		}
	}

	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
}
