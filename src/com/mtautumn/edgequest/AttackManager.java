package com.mtautumn.edgequest;

import com.mtautumn.edgequest.blockitems.combat.Hands;
import com.mtautumn.edgequest.blockitems.combat.Weapon;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.projectiles.Projectile;

public class AttackManager extends Thread{
	DataManager dm;
	public AttackManager(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		boolean wasLeftMouseDown = false;
		while (dm.system.running) {
			try {
				if (dm.system.isAiming) { //Is player aiming
					if (dm.system.leftMouseDown && !wasLeftMouseDown) { //Is player attacking
						performAttack();
					}
					wasLeftMouseDown = dm.system.leftMouseDown;
				}
				Thread.sleep(dm.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private boolean isWeapon(int slot) {
		if (dm.characterManager.characterEntity.getHeldItem(slot) != null) {
			if (dm.characterManager.characterEntity.getHeldItem(slot) instanceof Weapon) {
				return true;
			}
		}
		return false;
	}
	private boolean doesBackpackContainAmmo(Weapon weapon) {
		for (int i = 0; i < dm.savable.backpackItems.length; i++) {
			for (int j = 0; j < dm.savable.backpackItems[i].length; j++) {
				ItemSlot slot = dm.savable.backpackItems[i][j];
				for (int k = 0; k < weapon.ammoTypes.length; k++) {
					if (dm.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
						return true;
					}
				}
			}
		}
		return false;
	}
	private ItemSlot getAmmoSlot(Weapon weapon) {
		if (doesBackpackContainAmmo(weapon)) {
			for (int i = 0; i < dm.savable.backpackItems.length; i++) {
				for (int j = 0; j < dm.savable.backpackItems[i].length; j++) {
					ItemSlot slot = dm.savable.backpackItems[i][j];
					for (int k = 0; k < weapon.ammoTypes.length; k++) {
						if (dm.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
							return slot;
						}
					}
				}
			}
		}
		return null;
	}
	private void removeAmmoFromSlot(Weapon weapon) {
		if (doesBackpackContainAmmo(weapon)) {
			for (int i = 0; i < dm.savable.backpackItems.length; i++) {
				for (int j = 0; j < dm.savable.backpackItems[i].length; j++) {
					ItemSlot slot = dm.savable.backpackItems[i][j];
					for (int k = 0; k < weapon.ammoTypes.length; k++) {
						if (dm.system.blockIDMap.get(slot.getItemID()).isName(weapon.ammoTypes[k])) {
							dm.savable.backpackItems[i][j].subtractOne();
							if (dm.savable.backpackItems[i][j].getItemCount() <= 0) {
								dm.savable.backpackItems[i][j] = new ItemSlot();
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
			attackWeapon = (Weapon) dm.characterManager.characterEntity.getHeldItem(0);
			offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(0).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(0);
			}
		} else if (isWeapon(1)) {
			attackWeapon = (Weapon) dm.characterManager.characterEntity.getHeldItem(1);
			offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			handUsed = true;
			dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth -= 1;
			if (dm.characterManager.characterEntity.getHeldItemSlot(1).itemHealth <= 0) {
				dm.characterManager.characterEntity.removeHeldItem(1);
			}
		}
		Projectile[] projectiles;
		if (attackWeapon != null) {
			if (doesBackpackContainAmmo(attackWeapon)) {
				ItemSlot slot = getAmmoSlot(attackWeapon);
				removeAmmoFromSlot(attackWeapon);
				projectiles = attackWeapon.createProjectiles(dm.system.blockIDMap.get(slot.getItemID()).getName(), dm.characterManager.characterEntity, offsetX, offsetY, handUsed);
			} else if (attackWeapon.ammoTypes.length == 0) {
				projectiles = attackWeapon.createProjectiles(null, dm.characterManager.characterEntity, offsetX, offsetY, handUsed);
			} else {
				projectiles = new Projectile[0];
			}
		} else {
			if (lastHand) {
				offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() - Math.PI / 4.0) * 0.4;
			} else {
				offsetX = Math.cos(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
				offsetY = -Math.sin(-dm.characterManager.characterEntity.getRot() + Math.PI / 4.0) * 0.4;
			}
			lastHand = !lastHand;
			projectiles = Hands.createProjectiles(dm.characterManager.characterEntity, offsetX, offsetY);
		}
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i].x += offsetX;
			projectiles[i].y += offsetY;
			dm.savable.projectiles.add(projectiles[i]);
		}
	}

}
