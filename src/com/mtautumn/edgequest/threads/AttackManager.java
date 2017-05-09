package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.combat.Hands;
import com.mtautumn.edgequest.blockitems.combat.Weapon;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.projectiles.Projectile;

public class AttackManager extends Thread {

	@Override
	public void run() {
		boolean wasLeftMouseDown = false;
		while (DataManager.system.running) {
			try {
				if (DataManager.system.isAiming) { //Is player aiming
					if (DataManager.system.leftMouseDown && !wasLeftMouseDown) { //Is player attacking
						performAttack();
					}
					wasLeftMouseDown = DataManager.system.leftMouseDown;
				}
				Thread.sleep(DataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static boolean isWeapon(int slot) {
		if (DataManager.characterManager.characterEntity.getHeldItem(slot) != null) {
			if (DataManager.characterManager.characterEntity.getHeldItem(slot) instanceof Weapon) {
				return true;
			}
		}
		return false;
	}
	private static boolean doesBackpackContainAmmo(Weapon weapon) {
		for (int i = 0; i < DataManager.savable.backpackItems.length; i++) {
			for (int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				ItemSlot slot = DataManager.savable.backpackItems[i][j];
				for (int k = 0; k < weapon.ammoTypes.length; k++) {
					if (DataManager.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private static ItemSlot getAmmoSlot(Weapon weapon) {
		if (doesBackpackContainAmmo(weapon)) {
			for (int i = 0; i < DataManager.savable.backpackItems.length; i++) {
				for (int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
					ItemSlot slot = DataManager.savable.backpackItems[i][j];
					for (int k = 0; k < weapon.ammoTypes.length; k++) {
						if (DataManager.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
							return slot;
						}
					}
				}
			}
		}
		return null;
	}
	private static void removeAmmoFromSlot(Weapon weapon) {
		if (doesBackpackContainAmmo(weapon)) {
			for (int i = 0; i < DataManager.savable.backpackItems.length; i++) {
				for (int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
					ItemSlot slot = DataManager.savable.backpackItems[i][j];
					for (int k = 0; k < weapon.ammoTypes.length; k++) {
						if (DataManager.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
							DataManager.savable.backpackItems[i][j].subtractOne();
							if (DataManager.savable.backpackItems[i][j].getItemCount() <= 0) {
								DataManager.savable.backpackItems[i][j] = new ItemSlot();
							}
						}
					}
				}
			}
		}
	}
	private boolean lastHand = false;
	private void performAttack() {
		Weapon attackWeapon = null;
		double offsetX = 0;
		double offsetY = 0;
		boolean handUsed = false; //left hand is false;
		if (isWeapon(0)) {
			attackWeapon = (Weapon) DataManager.characterManager.characterEntity.getHeldItem(0);
			offsetX = Math.cos(-DataManager.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-DataManager.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			DataManager.characterManager.characterEntity.getHeldItemSlot(0).itemHealth -= 1;
			if (DataManager.characterManager.characterEntity.getHeldItemSlot(0).itemHealth <= 0) {
				DataManager.characterManager.characterEntity.removeHeldItem(0);
			}
		} else if (isWeapon(1)) {
			attackWeapon = (Weapon) DataManager.characterManager.characterEntity.getHeldItem(1);
			offsetX = Math.cos(-DataManager.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-DataManager.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			handUsed = true;
			DataManager.characterManager.characterEntity.getHeldItemSlot(1).itemHealth -= 1;
			if (DataManager.characterManager.characterEntity.getHeldItemSlot(1).itemHealth <= 0) {
				DataManager.characterManager.characterEntity.removeHeldItem(1);
			}
		}
		Projectile[] projectiles;
		if (attackWeapon != null) {
			if (doesBackpackContainAmmo(attackWeapon)) {
				ItemSlot slot = getAmmoSlot(attackWeapon);
				removeAmmoFromSlot(attackWeapon);
				projectiles = attackWeapon.createProjectiles(DataManager.system.blockIDMap.get(slot.getItemID()).getName(), DataManager.characterManager.characterEntity, offsetX, offsetY, handUsed);
			} else if (attackWeapon.ammoTypes.length == 0) {
				projectiles = attackWeapon.createProjectiles(null, DataManager.characterManager.characterEntity, offsetX, offsetY, handUsed);
			} else {
				projectiles = new Projectile[0];
			}
		} else {
			if (lastHand) {
				offsetX = Math.cos(-DataManager.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-DataManager.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			} else {
				offsetX = Math.cos(-DataManager.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-DataManager.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			}
			lastHand = !lastHand;
			projectiles = Hands.createProjectiles(DataManager.characterManager.characterEntity, offsetX, offsetY);
		}
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i].x += offsetX;
			projectiles[i].y += offsetY;
			DataManager.savable.projectiles.add(projectiles[i]);
		}
	}

}
