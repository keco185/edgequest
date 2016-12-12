package com.mtautumn.edgequest;

import java.util.ArrayList;

import com.mtautumn.edgequest.PathFinder.IntCoord;
import com.mtautumn.edgequest.data.DataManager;

public class Troll extends Entity {
	private static final long serialVersionUID = 1L;
	private int lastX = 0;
	private int lastY = 0;
	private double lastPlayerLocX = Double.NaN;
	private double lastPlayerLocY = Double.NaN;
	private int checkCount = 0;
	private int attackTimer = 0;

	public Troll(double posX, double posY, double rotation, DataManager dm, int dungeonLevel) {
		super("troll",EntityType.hostileCreature, posX, posY, rotation, dungeonLevel, dm);
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.83;
		super.maxHealth = 10;
		super.health = 10;
	}
	public Troll(Entity entity) {
		super("troll",EntityType.hostileCreature, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel, entity.dm);
		dungeonLevel = entity.dungeonLevel;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.83;
		super.maxHealth = 10;
		super.health = 10;
	}
	public Troll() {
		super();
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.83;
	}
	public void update() {
		if (checkCount == 10) {
			checkCount = 0;
			if (isLineOfSightFOV(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), 2.7052603406)) {
				lastPlayerLocX = dm.characterManager.characterEntity.getX();
				lastPlayerLocY = dm.characterManager.characterEntity.getY();
			} else if (!Double.isNaN(lastPlayerLocX) && !Double.isNaN(lastPlayerLocY)) {
				if (Math.sqrt(Math.pow(lastPlayerLocX - getX(), 2)+Math.pow(lastPlayerLocY - getY(), 2)) < 5 && path.size() == 0) {
					lastPlayerLocX = Double.NaN;
					lastPlayerLocY = Double.NaN;
				}
			}
		}
		checkCount++;
		if (!Double.isNaN(lastPlayerLocX) && !Double.isNaN(lastPlayerLocY)) {
			if (distanceToPlayer() > 5 || !isLineOfSight(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY())) {

				if (checkCount == 1) {
					setDestination((int) Math.floor(lastPlayerLocX), (int) Math.floor(lastPlayerLocY));
				}
			} else if (distanceToPlayer() < 3) {
				path = new ArrayList<IntCoord>();
				double deltaX = -(lastPlayerLocX - getX());
				double deltaY = -(lastPlayerLocY - getY());
				double angle = Math.atan2(deltaY, deltaX);

				setX(getX() + Math.cos(angle) * moveSpeed * new Double(dm.settings.tickLength) / 1000.0);
				setY(getY() + Math.sin(angle) * moveSpeed * new Double(dm.settings.tickLength) / 1000.0);
				super.updateRotation(deltaX, deltaY);
				
			} else {
				path = new ArrayList<IntCoord>();
				double deltaX = lastPlayerLocX - getX();
				double deltaY = lastPlayerLocY - getY();
				super.updateRotation(deltaX, deltaY);
			}
		} else {
			int tries = 0;
			while ((!checkMove(0, getX() + lastX * moveSpeed * new Double(dm.settings.tickLength) / 1000.0) || !checkMove(1, getY() + lastY * moveSpeed * new Double(dm.settings.tickLength) / 1000.0) || tries == 0) && tries < 20) {
				tries++;
				if (lastX == 0 && lastY == 0) {
					if (Math.random() > 0.95) {
						if (Math.random() > 0.5) {
							if (Math.random() > 0.5) {
								lastX = 1;
							} else {
								lastX = -1;
							}
						} else {
							if (Math.random() > 0.5) {
								lastY = 1;
							} else {
								lastY = -1;
							}
						}
					}
				} else {
					if (Math.random() > 0.995) {
						lastX = 0;
						lastY = 0;
						if (Math.random() > 0.5) {
							if (Math.random() > 0.5) {
								lastX = 1;
							} else {
								lastX = -1;
							}
						} else {
							if (Math.random() > 0.5) {
								lastY = 1;
							} else {
								lastY = -1;
							}
						}
						if (Math.random() > 0.999) {
							lastX = 0;
							lastY = 0;
						}
					}
				}
			}

			if (tries < 20) {
				setX(getX() + lastX * moveSpeed * new Double(dm.settings.tickLength) / 1000.0);
				setY(getY() + lastY * moveSpeed * new Double(dm.settings.tickLength) / 1000.0);
			}
			super.updateRotation(lastX, lastY);
		}
		super.update();

		if (attackTimer == 60 && distanceToPlayer() <= 5 && isLineOfSightFOV(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), 2.7052603406)) {
			double deltaX = dm.characterManager.characterEntity.getX() - getX();
			double deltaY = dm.characterManager.characterEntity.getY() - getY();
			double angle = Math.atan2(-deltaY, deltaX);
			dm.attackManager.castAttack(dm.system.blockNameMap.get("dagger"), this, angle, 5, 0.3);
		}
		if (attackTimer == 60) {
			attackTimer = 0;
		}
		attackTimer++;
	}
	private boolean checkMove(int dir, double newVal) {
		double newX = getX();
		double newY = getY();
		if (dir == 0) {
			newX = newVal;
		} else {
			newY = newVal;
		}
		if (dm.world.isStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (!dm.system.blockIDMap.get(dm.world.getStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))).isPassable) {
				return false;
			}
		}
		if (dm.world.isGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (dm.world.getGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY)) == dm.system.blockNameMap.get("water").getID()) {
				return false;
			}
			return true;
		}
		return false;

	}
	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
}
