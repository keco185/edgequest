package com.mtautumn.edgequest.entities;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.projectiles.HandProjectile;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.WorldUtils;

public class Pet extends Entity {
	private static final long serialVersionUID = 1L;
	private double lastPlayerX = 0;
	private double lastPlayerY = 0;
	private Entity attackEntity;

	public Pet(double posX, double posY, double rotation, int dungeonLevel, int[] dungeon) {
		super("pet",EntityType.pet, posX, posY, rotation, dungeonLevel);
		super.stillAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7};
		super.moveSpeed = 1.2;
		super.maxHealth = 5;
		super.health = 5;
	}
	public Pet(Entity entity) {
		super("pet",EntityType.pet, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel);
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
		if (entity != this) {
			attackEntity = entity;
		}
	}
	private int attackStage = 0;
	@Override
	public void update() {
		if (attackEntity != null) {
			if (attackEntity.health <= 0) {
				attackEntity = null;
			}
		}
		if (attackEntity != null) {
			if (attackEntity.dungeonLevel == dungeonLevel) {
				double deltaX = attackEntity.getX() - getX();
				double deltaY = attackEntity.getY() - getY();
				updateRotation(deltaX, deltaY);
				if (Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < 1.2 && attackStage == 0) {
					DataManager.savable.projectiles.add(new HandProjectile(this, 2));
					attackStage = 1;
				} else if (attackStage == 0){
					double moveX = Math.cos(rotation) * moveSpeed / 15.0;
					double moveY = Math.sin(rotation) * moveSpeed / 15.0;
					move(moveX, moveY);
				} else if (attackStage == 1 && Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) < 1.5) {
					double moveX = -Math.cos(rotation) * moveSpeed / 30.0;
					double moveY = -Math.sin(rotation) * moveSpeed / 30.0;
					move(moveX, moveY);
					if (WorldUtils.isStructBlock(this, (int)(getX() + moveX), (int)(getY() + moveY))) {
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
			if (isLineOfSight(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY())) {
				double deltaX = CharacterManager.characterEntity.getX() - getX();
				double deltaY = CharacterManager.characterEntity.getY() - getY();
				updateRotation(deltaX, deltaY);
				if (distanceToPlayer() > 2 && CharacterManager.characterEntity.dungeonLevel == dungeonLevel) {
					double moveX = Math.cos(rotation) * moveSpeed / 30.0;
					double moveY = Math.sin(rotation) * moveSpeed / 30.0;
					move(moveX, moveY);
				} else {
					move(0,0);
				}
				super.path = null;
			} else if ((distanceToPlayer() > 2 || !isLineOfSight(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY())) && CharacterManager.characterEntity.dungeonLevel == dungeonLevel) {
				if (Math.abs(lastPlayerPosUpdate - System.currentTimeMillis()) > 2000) {
					if (lastPlayerX != CharacterManager.characterEntity.getX() || lastPlayerY != CharacterManager.characterEntity.getY()) {
						lastPlayerX = CharacterManager.characterEntity.getX();
						lastPlayerY = CharacterManager.characterEntity.getY();
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

	@Override
	public void initializeClass() {
		super.initializeClass();
	}
}
