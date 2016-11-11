/* Updates damage incurred on a block by mining and removes the block
 * if the damage is full.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.BlockItem;
import com.mtautumn.edgequest.data.DataManager;

public class UpdateMining {
	DataManager dataManager;
	public UpdateMining(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (dataManager.system.leftMouseDown && !wasMouseDown || dataManager.system.miningX != dataManager.system.mouseX || dataManager.system.miningY != dataManager.system.mouseY) {
			wasStructBlock = dataManager.world.isStructBlock(dataManager.system.mouseX, dataManager.system.mouseY);
		}
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (dataManager.system.leftMouseDown && wasMouseDown && !dataManager.system.rightMouseDown && !dataManager.system.isMouseFar) {
				if (dataManager.system.miningX != dataManager.system.mouseX || dataManager.system.miningY != dataManager.system.mouseY) {
					dataManager.system.miningX = dataManager.system.mouseX;
					dataManager.system.miningY = dataManager.system.mouseY;
					dataManager.system.blockDamage = 0;
				}
				if (getBlockAt(dataManager.system.mouseX, dataManager.system.mouseY) != null && (wasStructBlock || (!wasStructBlock && dataManager.world.isStructBlock(dataManager.system.mouseX, dataManager.system.mouseY)))) {
					dataManager.system.blockDamage += 1.0/getBlockAt(dataManager.system.mouseX, dataManager.system.mouseY).hardness/dataManager.settings.tickLength;
					if (dataManager.system.blockDamage < 0) dataManager.system.blockDamage = 0;
					if (dataManager.system.blockDamage >= 10) {
						dataManager.system.blockDamage = 0;
						breakBlock(dataManager.system.mouseX, dataManager.system.mouseY);
						dataManager.blockUpdateManager.lighting.update(dataManager.system.mouseX, dataManager.system.mouseY);
					}
				} else {
					dataManager.system.blockDamage = 0;
				}
			} else {
				dataManager.system.blockDamage = 0;
			}
		}
		wasMouseDown = dataManager.system.leftMouseDown;
	}
	private BlockItem getBlockAt(int x, int y) {
		if (dataManager.world.isStructBlock(x, y)) {
			return dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y));
		}
		return null;

	}
	private void breakBlock(int x, int y) {
		BlockItem item = null;
		if (dataManager.world.isStructBlock(x, y)) {
			item = dataManager.system.blockIDMap.get(dataManager.world.getStructBlock(x, y));
			dataManager.world.removeStructBlock(x, y);

		}
		if (item != null) {
			BlockItem result = dataManager.system.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				dataManager.backpackManager.addItem(result);
			}
		}
	}
}
