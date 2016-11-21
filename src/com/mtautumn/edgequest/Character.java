package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class Character extends Entity {
	private static final long serialVersionUID = 1L;
	long lastUpdate;
	private double lastX = 0;
	private double lastY = 0;
	public double stamina = 10;
	public double maxStamina = 10;

	public Character(double posX, double posY, double rotation, int dungeonLevel, int[] dungeon, DataManager dm) {
		super("character",EntityType.character, posX, posY, rotation, dungeonLevel, dungeon, dm);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		stamina = 10;
		maxStamina = 10;
	}
	public Character(Entity entity) {
		super("character", EntityType.character, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel, entity.dungeon, entity.dm);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		dungeonLevel = entity.dungeonLevel;
		dungeon = entity.dungeon;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0};
		super.moveSpeed = dm.settings.moveSpeed;
		stamina = 10;
		maxStamina = 10;
	}
	public Character() {
		super();
		super.slide = true;
	}
	public void update() {
		dungeonLevel = dm.savable.dungeonLevel;
		dungeon = new int[]{dm.savable.dungeonX, dm.savable.dungeonY};
		if (super.dm.system.isKeyboardSprint) super.moveSpeed = super.dm.settings.moveSpeed * 2.0;
		else super.moveSpeed = super.dm.settings.moveSpeed;
		if (super.dm.system.isKeyboardLeft ||
				super.dm.system.isKeyboardUp ||
				super.dm.system.isKeyboardRight ||
				super.dm.system.isKeyboardDown) {
			if (super.path != null) {
				super.path.clear();
			}
			double moveInterval = Double.valueOf(System.currentTimeMillis() - lastUpdate) / 1000.0 * dm.settings.moveSpeed;
			double charYOffset = 0.0;
			double charXOffset = 0.0;
			if (dm.system.isKeyboardUp) {
				charYOffset -= moveInterval;
			}
			if (dm.system.isKeyboardRight) {
				charXOffset += moveInterval;
			}
			if (dm.system.isKeyboardDown) {
				charYOffset += moveInterval;
			}
			if (dm.system.isKeyboardLeft) {
				charXOffset -= moveInterval;
			}
			if (charXOffset != 0 && charYOffset != 0) {
				charXOffset *= 0.70710678118;
				charYOffset *= 0.70710678118;
			}
			if (dm.system.isKeyboardSprint && stamina > 0.03) {
				charXOffset *= 1.7;
				charYOffset *= 1.7;
				stamina -= 0.03;
			}
			if (dm.system.blockIDMap.get((short)dm.characterManager.getCharaterBlockInfo()[0]).isLiquid && dm.characterManager.getCharaterBlockInfo()[1] == 0.0) {
				charXOffset /= 1.7;
				charYOffset /= 1.7;
			}
			if (super.dm.system.rightMouseDown) {
				updateRotation(super.dm.system.mousePosition.getX() - (super.dm.settings.screenWidth / 2.0), super.dm.system.mousePosition.getY() - (super.dm.settings.screenHeight / 2.0));
				charXOffset /= 1.5;
				charYOffset /= 1.5;
				super.move(charXOffset, charYOffset);
			} else {
				if (charXOffset != 0.0 || charYOffset != 0.0) {
					updateRotation(charXOffset, charYOffset);
				}
				super.move(charXOffset, charYOffset);
			}

		} else {
			super.update();
			if (super.path != null) {
				if (super.path.size() == 0) {
					super.move(0, 0);
				}
			} else {
				super.move(0, 0);
			}
			if (!dm.system.characterMoving) {
				if (super.dm.system.rightMouseDown) {
					updateRotation(super.dm.system.mousePosition.getX() - (super.dm.settings.screenWidth / 2.0), super.dm.system.mousePosition.getY() - (super.dm.settings.screenHeight / 2.0));
				}
			}
		}
		if (!dm.system.isKeyboardSprint) {
			if (stamina < maxStamina) {
				stamina += 0.01;
			}
		}
		lastUpdate = System.currentTimeMillis();
		if (lastX != super.getX() || lastY != super.getY()) {
			dm.system.characterMoving = true;
			lastX = super.getX();
			lastY = super.getY();
		} else {
			dm.system.characterMoving = false;
		}
	}
	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
	public BlockItem getHeldItem(int slot) {
		switch (slot) {
		case 0:
			if (dm.savable.backpackItems[0][dm.savable.hotBarSelection].getItemCount() > 0) {
				return dm.system.blockIDMap.get(dm.savable.backpackItems[0][dm.savable.hotBarSelection].getItemID());
			}
			return null;
		case 1:
			if (dm.savable.backpackItems[0][dm.savable.hotBarSelection].getItemCount() > 0) {
				return dm.system.blockIDMap.get(dm.savable.backpackItems[1][dm.savable.hotBarSelection].getItemID());
			}
			return null;
		default:
			return null;
		}
	}
}
