package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class Troll extends Entity {
	private static final long serialVersionUID = 1L;
	private int lastX = 0;
	private int lastY = 0;

	public Troll(double posX, double posY, double rotation, DataManager dm, int dungeonLevel, int[] dungeon) {
		super("troll",EntityType.passiveCreature, posX, posY, rotation, dungeonLevel, dungeon, dm);
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.025;
		super.maxHealth = 10.0;
		super.health = 10.0;
	}
	public Troll(Entity entity) {
		super("troll",EntityType.passiveCreature, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel, entity.dungeon, entity.dm);
		dungeonLevel = entity.dungeonLevel;
		dungeon = entity.dungeon;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.025;
		super.maxHealth = 10.0;
		super.health = 10.0;
	}
	public Troll() {
		super();
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = 0.05;
	}
	public void update() {
		int tries = 0;
		while ((!checkMove(0, getX() + lastX * moveSpeed) || !checkMove(1, getY() + lastY * moveSpeed) || tries == 0) && tries < 20) {
			tries++;
			if (lastX == 0) {
				if (Math.random() < 0.95) {
					lastX = 0;
				} else if (Math.random() < 0.5) {
					lastX = -1;
				} else {
					lastX = 1;
				}
			} else {
				if (Math.random() < 0.001) {
					lastX = 0;
				} else if (Math.random() > 0.995) {
					lastX = -lastX;
				}
			}
			if (lastY == 0) {
				if (Math.random() < 0.95) {
					lastY = 0;
				} else if (Math.random() < 0.5) {
					lastY = -1;
				} else {
					lastY = 1;
				}
			} else {
				if (Math.random() < 0.001) {
					lastY = 0;
				} else if (Math.random() > 0.995) {
					lastY = -lastY;
				}
			}
		}
		
		if (tries < 20) {
			setX(getX() + lastX * moveSpeed);
			setY(getY() + lastY * moveSpeed);
		}
		super.updateRotation(lastX, lastY);
		
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
		if (dm.world.isStructBlock(this, (int) newX, (int) newY)) {
			if (!dm.system.blockIDMap.get(dm.world.getStructBlock(this, (int) newX, (int) newY)).isPassable) {
				return false;
			}
		}
		if (dm.world.isGroundBlock(this, (int) newX, (int) newY)) {
			if (dm.world.getGroundBlock(this, (int) newX, (int) newY) == dm.system.blockNameMap.get("water").getID()) {
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
