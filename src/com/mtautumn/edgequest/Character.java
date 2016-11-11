package com.mtautumn.edgequest;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.mtautumn.edgequest.data.DataManager;

public class Character extends Entity {
	private static final long serialVersionUID = 1L;
	long lastUpdate;
	private double lastX = 0;
	private double lastY = 0;

	public Character(double posX, double posY, byte rotation, DataManager dm) {
		super("character",EntityType.character, posX, posY, rotation, dm);
		lastUpdate = System.currentTimeMillis();
		super.slide = true;
	}
	public Character() {
		super();
		super.slide = true;
	}
	public void update() {
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
			if (dm.system.isKeyboardSprint) {
				charXOffset *= 1.5;
				charYOffset *= 1.5;
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
		lastUpdate = System.currentTimeMillis();
		if (lastX != super.getX() || lastY != super.getY()) {
			dm.system.characterMoving = true;
			lastX = super.getX();
			lastY = super.getY();
		} else {
			dm.system.characterMoving = false;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
	}
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
	}
	public void initializeClass(DataManager dm) {
		super.initializeClass(dm);
	}
}
