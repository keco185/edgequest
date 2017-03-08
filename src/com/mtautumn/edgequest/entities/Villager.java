package com.mtautumn.edgequest.entities;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Villager extends Entity {
	private static final long serialVersionUID = 1L;
	private static final double MOVE_SPEED = 1.2;
	private static final int MAX_HEALTH = 10;
	
	public boolean isWalkingSomewhere = false;
	public int destinationX = 0;
	public int destinationY = 0;
	
	private ArrayList<int[]> houseBlocks = new ArrayList<int[]>();
	private ArrayList<int[]> workBlocks = new ArrayList<int[]>();
	
	public static enum VillagerType {

		citizen,
		farmer,
		mayor,
		shopkeeper,
		blacksmith,

	}
	public VillagerType villagerType = VillagerType.citizen;
	public Villager(double posX, double posY, double rotation, DataManager dm, int dungeonLevel) {
		super("villager",EntityType.villager, posX, posY, rotation, dungeonLevel, dm);
		moveSpeed = MOVE_SPEED;
		maxHealth = MAX_HEALTH;
		health = MAX_HEALTH;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		slide = true;
	}
	
	public Villager(Entity entity) {
		super("villager",EntityType.villager, entity.getX(), entity.getY(),entity.getRot(), entity.dungeonLevel, entity.dm);
		moveSpeed = MOVE_SPEED;
		maxHealth = MAX_HEALTH;
		health = MAX_HEALTH;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		slide = true;
	}
	
	public Villager() {
		super();
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		slide = true;
	}
	
	public void setVillagerType(VillagerType type) {
		villagerType = type;
	}
	
	public void update() {
		if (path != null) {
			if (path.size() > 0) {
				isWalkingSomewhere = true;
			} else {
				isWalkingSomewhere = false;
			}
		} else {
			isWalkingSomewhere = false;
		}
		
		if (isWalkingSomewhere) {
				update();
		} else {
			double random = Math.random();
			if (random > 0.995) {
				//Walk somewhere
				if (dm.savable.time > 800 && dm.savable.time < 2000) { //If daytime
					if (workBlocks.size() > 0) {
						random = (int) (Math.random() * workBlocks.size());
						setDestination(workBlocks.get((int)random)[0],workBlocks.get((int)random)[1]);
					}
				} else {
					if (houseBlocks.size() > 0) {
						random = (int) (Math.random() * houseBlocks.size());
						setDestination(houseBlocks.get((int)random)[0],houseBlocks.get((int)random)[1]);
					}
				}
			} else if (random > 0.98) {
				//Stay put
				move(0,0);
				updateRotation(Math.random() - 0.5, Math.random() - 0.5);
			} else {
				move(0,0);
			}
		}
	}
}
