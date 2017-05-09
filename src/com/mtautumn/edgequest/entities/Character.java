package com.mtautumn.edgequest.entities;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.LightSource;

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
	

	public Character(double posX, double posY, double rotation, int dungeonLevel) {
		super("character",EntityType.character, posX, posY, rotation, dungeonLevel);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		light = new LightSource(getX(), getY(), 8, -1); //light use to be 8, you can't see enemies until they're already on top of you...
		light.onEntity = true;
		DataManager.savable.lightSources.add(light);
	}
	public Character(Entity entity) {
		super("character", EntityType.character, entity.getX(), entity.getY(), entity.getRot(), entity.dungeonLevel);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
		dungeonLevel = entity.dungeonLevel;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
		super.moveSpeed = SettingsData.moveSpeed; // why is move speed not just a var of the player or entity class
		light = new LightSource(getX(), getY(), 8, -1);
		light.onEntity = true;
		DataManager.savable.lightSources.add(light);
	}
	public Character() {
		super();
		super.slide = true;
		super.stillAnimation = new int[]{0};
		super.walkAnimation = new int[]{0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11};
	}
	@Override
	public String getTexture() {
		if (SystemData.isKeyboardLeft ||
				SystemData.isKeyboardUp ||
				SystemData.isKeyboardRight ||
				SystemData.isKeyboardDown) {
			return entityTexture + "." + entityTexture + "walk" + walkAnimation[SystemData.animationClock % walkAnimation.length];
		}
		return entityTexture + "." + entityTexture + "still" + stillAnimation[SystemData.animationClock % stillAnimation.length];
	}
	@Override
	public void update() {
		light.posX = getX();
		light.posY = getY();
		light.level = dungeonLevel;
		dungeonLevel = DataManager.savable.dungeonLevel;
		if (SystemData.isKeyboardSprint) {
			super.moveSpeed = SettingsData.moveSpeed * 2.0;
		} else {
			super.moveSpeed = SettingsData.moveSpeed;
		}
		if (SystemData.isKeyboardLeft ||
				SystemData.isKeyboardUp ||
				SystemData.isKeyboardRight ||
				SystemData.isKeyboardDown) {
			if (super.path != null) {
				super.path.clear();
			}
			double moveInterval = 30 / 1000.0 * SettingsData.moveSpeed;
			double charYOffset = 0.0;
			double charXOffset = 0.0;
			if (SystemData.isKeyboardUp) {
				charYOffset -= moveInterval;
			}
			if (SystemData.isKeyboardRight) {
				charXOffset += moveInterval;
			}
			if (SystemData.isKeyboardDown) {
				charYOffset += moveInterval;
			}
			if (SystemData.isKeyboardLeft) {
				charXOffset -= moveInterval;
			}
			if (charXOffset != 0 && charYOffset != 0) {
				charXOffset *= 0.70710678118;
				charYOffset *= 0.70710678118;
			}
			if (SystemData.isKeyboardSprint && stamina > 0.03) {
				charXOffset *= 1.7;
				charYOffset *= 1.7;
			}
			if (SystemData.blockIDMap.get((short)DataManager.characterManager.getCharaterBlockInfo()[0]).isLiquid && DataManager.characterManager.getCharaterBlockInfo()[1] == 0.0) {
				charXOffset /= 1.7;
				charYOffset /= 1.7;
			}
			if (SystemData.isAiming) {
				updateRotation(SystemData.mousePosition.getX() - (SettingsData.screenWidth / 2.0), SystemData.mousePosition.getY() - (SettingsData.screenHeight / 2.0));
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
			if (!SystemData.characterMoving) {
				if (SystemData.isAiming) {
					updateRotation(SystemData.mousePosition.getX() - (SettingsData.screenWidth / 2.0), SystemData.mousePosition.getY() - (SettingsData.screenHeight / 2.0));
				}
			}
		}
		
		
		// -----------------------------------------
		// Check if player moving
		// -----------------------------------------
		lastUpdate = System.currentTimeMillis();
		if (lastX != super.getX() || lastY != super.getY()) {
			SystemData.characterMoving = true;
			lastX = super.getX();
			lastY = super.getY();
		} else {
			SystemData.characterMoving = false;
		}
		
		// -----------------------------------------
		// Stamina Consumption and Regeneration
		// -----------------------------------------
		if (SystemData.isKeyboardSprint && SystemData.characterMoving && SystemData.isMoveInput){
			stamina -= (staminaConsumeRunning * staminaConsumeFactor);
			timeToRegen = regenTimerSet;
		} else if (!SystemData.isKeyboardSprint || !SystemData.characterMoving || !SystemData.isMoveInput) {
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
	public void initializeClass() {
		super.initializeClass();
	}
	
	
	public BlockItem getHeldItem(int slot) {
		switch (slot) {
		case 0:
			if (DataManager.savable.leftEquipt().getItemCount() > 0) {
				return SystemData.blockIDMap.get(DataManager.savable.leftEquipt().getItemID());
			}
			return null;
		case 1:
			if (DataManager.savable.rightEquipt().getItemCount() > 0) {
				return SystemData.blockIDMap.get(DataManager.savable.rightEquipt().getItemID());
			}
			return null;
		default:
			return null;
		}
	}
	public void removeHeldItem(int slot) {
		if (slot == 0) {
			DataManager.savable.backpackItems[6][0] = new ItemSlot();
		} else if (slot == 1) {
			DataManager.savable.backpackItems[6][1] = new ItemSlot();
		}
	}
	public ItemSlot getHeldItemSlot(int slot) {
		switch (slot) {
		case 0:
			if (DataManager.savable.leftEquipt().getItemCount() > 0) {
				return DataManager.savable.leftEquipt();
			}
			return null;
		case 1:
			if (DataManager.savable.rightEquipt().getItemCount() > 0) {
				return DataManager.savable.rightEquipt();
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
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		light = (LightSource) in.readObject();
	}
}
