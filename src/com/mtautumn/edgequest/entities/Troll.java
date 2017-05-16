package com.mtautumn.edgequest.entities;

import java.util.ArrayList;

import com.mtautumn.edgequest.blockitems.combat.Weapon;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.projectiles.DaggerProjectile;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.PathFinder.IntCoord;
import com.mtautumn.edgequest.utils.WorldUtils;

public class Troll extends Entity {
	private static final long serialVersionUID = 1L;
	private int lastX = 0;
	private int lastY = 0;
	private double lastPlayerLocX = Double.NaN;
	private double lastPlayerLocY = Double.NaN;
	private int checkCount = 0;
	private int attackTimer = 0;
	private boolean lastHand = false;

	public Troll(double posX, double posY, double rotation, int dungeonLevel) {
		super("troll",EntityType.hostileCreature, posX, posY, rotation, dungeonLevel);
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.83;
		super.maxHealth = 10;
		super.health = 10;
	}
	public Troll(Entity entity) {
		super("troll",EntityType.hostileCreature, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel);
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
	@Override
	public void update() {
		if (checkCount == 10) {
			checkCount = 0;
			if (isLineOfSightFOV(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), 2.7052603406)) {
				lastPlayerLocX = CharacterManager.characterEntity.getX();
				lastPlayerLocY = CharacterManager.characterEntity.getY();
			} else if (!Double.isNaN(lastPlayerLocX) && !Double.isNaN(lastPlayerLocY)) {
				if (Math.sqrt(Math.pow(lastPlayerLocX - getX(), 2)+Math.pow(lastPlayerLocY - getY(), 2)) < 5 && path.size() == 0) {
					lastPlayerLocX = Double.NaN;
					lastPlayerLocY = Double.NaN;
				}
			}
		}
		checkCount++;
		if (!Double.isNaN(lastPlayerLocX) && !Double.isNaN(lastPlayerLocY)) {
			if (distanceToPlayer() > 5 || !isLineOfSight(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY())) {

				if (checkCount == 1) {
					setDestination((int) Math.floor(lastPlayerLocX), (int) Math.floor(lastPlayerLocY));
				}
			} else if (distanceToPlayer() < 3) {
				path = new ArrayList<IntCoord>();
				double deltaX = -(lastPlayerLocX - getX());
				double deltaY = -(lastPlayerLocY - getY());
				double angle = Math.atan2(deltaY, deltaX);

				setX(getX() + Math.cos(angle) * moveSpeed * new Double(SettingsData.tickLength) / 1000.0);
				setY(getY() + Math.sin(angle) * moveSpeed * new Double(SettingsData.tickLength) / 1000.0);
				super.updateRotation(deltaX, deltaY);
				
			} else {
				path = new ArrayList<IntCoord>();
				double deltaX = lastPlayerLocX - getX();
				double deltaY = lastPlayerLocY - getY();
				super.updateRotation(deltaX, deltaY);
			}
		} else {
			int tries = 0;
			while ((!checkMove(0, getX() + lastX * moveSpeed * new Double(SettingsData.tickLength) / 1000.0) || !checkMove(1, getY() + lastY * moveSpeed * new Double(SettingsData.tickLength) / 1000.0) || tries == 0) && tries < 20) {
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
				setX(getX() + lastX * moveSpeed * new Double(SettingsData.tickLength) / 1000.0);
				setY(getY() + lastY * moveSpeed * new Double(SettingsData.tickLength) / 1000.0);
			}
			super.updateRotation(lastX, lastY);
		}
		super.update();

		if (attackTimer == 60 && distanceToPlayer() <= 5 && isLineOfSightFOV(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), 2.7052603406)) {
			double deltaX = CharacterManager.characterEntity.getX() - getX();
			double deltaY = CharacterManager.characterEntity.getY() - getY();
			double angle = Math.atan2(-deltaY, deltaX);
			double offsetX;
			double offsetY;
			if (lastHand) {
				offsetX = Math.cos(-CharacterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-CharacterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			} else {
				offsetX = Math.cos(-CharacterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-CharacterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			}
			lastHand = !lastHand;
			DataManager.savable.projectiles.add(new DaggerProjectile(10, 5, this, Weapon.getDamage(3), offsetX, offsetY, false, 1, angle));
		}
		if (attackTimer == 60) {
			attackTimer = 0;
		}
		attackTimer++;
	}
	@Override
	public void death() {
		if (Math.random() > 0.8) {
			ItemSlot drop = new ItemSlot();
			drop.setItem(SystemData.blockNameMap.get("dagger").getID());
			drop.setItemCount(1);
			DataManager.savable.itemDrops.add(new ItemDrop(posX, posY, dungeonLevel, drop));
		}
	}
	private boolean checkMove(int dir, double newVal) {
		double newX = getX();
		double newY = getY();
		if (dir == 0) {
			newX = newVal;
		} else {
			newY = newVal;
		}
		if (WorldUtils.isStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (!SystemData.blockIDMap.get(WorldUtils.getStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))).isPassable) {
				return false;
			}
		}
		if (WorldUtils.isGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (WorldUtils.getGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY)) == SystemData.blockNameMap.get("water").getID()) {
				return false;
			}
			return true;
		}
		return false;

	}
	@Override
	public void initializeClass() {
		super.initializeClass();
	}
}
