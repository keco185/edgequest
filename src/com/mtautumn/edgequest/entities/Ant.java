package com.mtautumn.edgequest.entities;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;

public class Ant extends Entity {
	private static final long serialVersionUID = 1L;
	private int lastX = 0;
	private int lastY = 0;

	public Ant(double posX, double posY, double rotation, int dungeonLevel, int[] dungeon) {
		super("ant",EntityType.passiveCreature, posX, posY, rotation, dungeonLevel);
		super.stillAnimation = new int[]{0,0,1,1};
		super.walkAnimation = new int[]{0,1,2,3,4};
		super.moveSpeed = 0.025;
		super.maxHealth = 5;
		super.health = 5;
	}
	public Ant(Entity entity) {
		super("ant",EntityType.passiveCreature, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel);
		dungeonLevel = entity.dungeonLevel;
		super.stillAnimation = new int[]{0,0,1,1};
		super.walkAnimation = new int[]{0,1,2,3,4};
		super.moveSpeed = 0.025;
		super.maxHealth = 5;
		super.health = 5;
	}
	public Ant() {
		super();
		super.stillAnimation = new int[]{0,1};
		super.walkAnimation = new int[]{0,1,2,3,4};
		super.moveSpeed = 0.05;
	}
	@Override
	public void update() {
		int tries = 0;
		while ((!checkMove(0, getX() + lastX * moveSpeed) || !checkMove(1, getY() + lastY * moveSpeed) || tries == 0) && tries < 20) {
			tries++;
		if (Math.random() > 0.99) {
			double rand = Math.random();
			if (rand < 0.33333333) {
				lastX = -1;
			} else if (rand < 0.66666667) {
				lastX = 0;
			} else {
				lastX = 1;
			}
		}
		if (Math.random() > 0.99) {
			double rand = Math.random();
			if (rand < 0.33333333) {
				lastY = -1;
			} else if (rand < 0.66666667) {
				lastY = 0;
			} else {
				lastY = 1;
			}
		}
		}
		if (tries < 20) {
			setX(getX() + lastX * moveSpeed);
			setY(getY() + lastY * moveSpeed);
		}
		super.updateRotation(lastX, lastY);
		if (DataManager.world.isStructBlock(this, (int) Math.floor(posX), (int) Math.floor(posY))) {
			if (DataManager.system.blockIDMap.get(DataManager.world.getStructBlock(this,(int) Math.floor(posX), (int) Math.floor(posY))).hardness > -1) {
				DataManager.world.removeStructBlock(this, (int) Math.floor(posX), (int) Math.floor(posY));
				DataManager.blockUpdateManager.updateBlock(new Location(this));
			}
		}
		super.update();
	}
	private boolean checkMove(int dir, double newVal) {
		double newX = getX();
		double newY = getY();
		if (dir == 0) {
			newX = newVal;
		} else {
			newY = newVal;
		}
		if (DataManager.world.isStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (DataManager.system.blockIDMap.get(DataManager.world.getStructBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))).hardness == -1) {
				return false;
			}
		}
		if (DataManager.world.isGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY))) {
			if (DataManager.world.getGroundBlock(this, (int) Math.floor(newX), (int) Math.floor(newY)) == DataManager.system.blockNameMap.get("water").getID()) {
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
