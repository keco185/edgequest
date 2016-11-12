package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class Ant extends Entity {
	private static final long serialVersionUID = 1L;
	private int lastX = 0;
	private int lastY = 0;

	public Ant(double posX, double posY, byte rotation, DataManager dm, int dungeonLevel, int[] dungeon) {
		super("ant",EntityType.passiveCreature, posX, posY, rotation, dm);
		this.dungeonLevel = dungeonLevel;
		this.dungeon = dungeon;
		super.stillAnimation = new int[]{0,0,1,1};
		super.walkAnimation = new int[]{0,1,2,3,4};
		super.moveSpeed = 0.05;
	}
	public Ant() {
		super();
		super.stillAnimation = new int[]{0,1};
		super.walkAnimation = new int[]{0,1,2,3,4};
		super.moveSpeed = 0.05;
	}
	public void update() {
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
		setX(getX() + lastX * moveSpeed);
		setY(getY() + lastY * moveSpeed);
		super.updateRotation(lastX, lastY);
		if (dm.world.isStructBlock(this, (int) getX(), (int) getY())) {
			if (dm.system.blockIDMap.get(dm.world.getStructBlock(this, (int) getX(), (int) getY())).hardness > -1) {
				dm.world.removeStructBlock(this, (int) getX(), (int) getY());
				dm.blockUpdateManager.updateBlock((int) getX(), (int) getY());
			}
		}
		super.update();
	}
	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
}
