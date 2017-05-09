package com.mtautumn.edgequest.entities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.entities.Entity.EntityType;
import com.mtautumn.edgequest.utils.PathFinder.IntCoord;

public class Character extends Entity {
	private static final long serialVersionUID = 1L;
	long lastUpdate;
	private double lastX = 0;
	private double lastY = 0;
	public double health = 20;
	public double maxHealth = 20;
	public double stamina = 10;
	public double maxStamina = 10;
	public LightSource light;
	
	private double regenTimerSet = 60; // 1 second if 60 fps; need to change this if we don't have a frame cap.
	private double timeToRegen = 60;
	private double staminaRegenBase = 0.2;
	private double staminaRegenFactor = 1;
	private double staminaConsumeRunning = 0.03;
	private double staminaConsumeFactor = 1;
	private double defaultFactor = 1; // Reset the factor. there's probably a smarter way to do that
	

	public Character(double posX, double posY, double rotation, int dungeonLevel, DataManager dm) {
		super("character",EntityType.character, posX, posY, rotation, dungeonLevel, dm);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		light = new LightSource(getX(), getY(), 8, -1); //light use to be 8, you can't see enemies until they're already on top of you...
		light.onEntity = true;
		dm.savable.lightSources.add(light);
	}
	public Character(Entity entity) {
		super("character", EntityType.character, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel, entity.dm);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		dungeonLevel = entity.dungeonLevel;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = dm.settings.moveSpeed; // why is move speed not just a var of the player or entity class
		light = new LightSource(getX(), getY(), 8, -1);
		light.onEntity = true;
		dm.savable.lightSources.add(light);
	}
	public Character() {
		super();
		super.slide = true;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
	}
	@Override
	public String getTexture() {
		if (dm.system.isKeyboardLeft ||
				dm.system.isKeyboardUp ||
				dm.system.isKeyboardRight ||
				dm.system.isKeyboardDown) {
			return entityTexture + "." + entityTexture + "walk" + walkAnimation[dm.system.animationClock % walkAnimation.length];
		}
		return entityTexture + "." + entityTexture + "still" + stillAnimation[dm.system.animationClock % stillAnimation.length];
	}
	@Override
	public void update() {
		light.posX = getX();
		light.posY = getY();
		light.level = dungeonLevel;
		dungeonLevel = dm.savable.dungeonLevel;
		if (super.dm.system.isKeyboardSprint) {
			super.moveSpeed = super.dm.settings.moveSpeed * 2.0;
		} else {
			super.moveSpeed = super.dm.settings.moveSpeed;
		}
		if (super.dm.system.isKeyboardLeft ||
				super.dm.system.isKeyboardUp ||
				super.dm.system.isKeyboardRight ||
				super.dm.system.isKeyboardDown) {
			if (super.path != null) {
				super.path.clear();
			}
			double moveInterval = 30 / 1000.0 * dm.settings.moveSpeed;
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
			}
			if (dm.system.blockIDMap.get((short)dm.characterManager.getCharaterBlockInfo()[0]).isLiquid && dm.characterManager.getCharaterBlockInfo()[1] == 0.0) {
				charXOffset /= 1.7;
				charYOffset /= 1.7;
			}
			if (super.dm.system.isAiming) {
				updateRotation(super.dm.system.mousePosition.getX() - (super.dm.settings.screenWidth / 2.0), super.dm.system.mousePosition.getY() - (super.dm.settings.screenHeight / 2.0));
				charXOffset /= 1.5;
				charYOffset /= 1.5;
			} else {
				if (charXOffset != 0.0 || charYOffset != 0.0) {
					updateRotation(charXOffset, charYOffset);
				}
			}
			super.move(charXOffset, charYOffset);


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
				if (super.dm.system.isAiming) {
					updateRotation(super.dm.system.mousePosition.getX() - (super.dm.settings.screenWidth / 2.0), super.dm.system.mousePosition.getY() - (super.dm.settings.screenHeight / 2.0));
				}
			}
		}
		
		
		// -----------------------------------------
		// Check if player moving
		// -----------------------------------------
		lastUpdate = System.currentTimeMillis();
		if (lastX != super.getX() || lastY != super.getY()) {
			dm.system.characterMoving = true;
			lastX = super.getX();
			lastY = super.getY();
		} else {
			dm.system.characterMoving = false;
		}
		
		// -----------------------------------------
		// Stamina Consumption and Regeneration
		// -----------------------------------------
		if (dm.system.isKeyboardSprint && dm.system.characterMoving && dm.system.isMoveInput){
			stamina -= (staminaConsumeRunning * staminaConsumeFactor);
			timeToRegen = regenTimerSet;
		} else if (!dm.system.isKeyboardSprint || !dm.system.characterMoving || !dm.system.isMoveInput) {
			if (stamina < maxStamina) {
				if (timeToRegen <= 0){
					timeToRegen = 0;
					stamina += (staminaRegenBase * staminaRegenFactor);
				} else {
					--timeToRegen;
				}
			}
		}
	}
	
	
	@Override
	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
	
	
	public BlockItem getHeldItem(int slot) {
		switch (slot) {
		case 0:
			if (dm.savable.leftEquipt().getItemCount() > 0) {
				return dm.system.blockIDMap.get(dm.savable.leftEquipt().getItemID());
			}
			return null;
		case 1:
			if (dm.savable.rightEquipt().getItemCount() > 0) {
				return dm.system.blockIDMap.get(dm.savable.rightEquipt().getItemID());
			}
			return null;
		default:
			return null;
		}
	}
	public void removeHeldItem(int slot) {
		if (slot == 0) {
			dm.savable.backpackItems[6][0] = new ItemSlot();
		} else if (slot == 1) {
			dm.savable.backpackItems[6][1] = new ItemSlot();
		}
	}
	public ItemSlot getHeldItemSlot(int slot) {
		switch (slot) {
		case 0:
			if (dm.savable.leftEquipt().getItemCount() > 0) {
				return dm.savable.leftEquipt();
			}
			return null;
		case 1:
			if (dm.savable.rightEquipt().getItemCount() > 0) {
				return dm.savable.rightEquipt();
			}
			return null;
		default:
			return null;
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeObject(light);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		light = (LightSource) in.readObject();
	}
}
