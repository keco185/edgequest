/* Updates damage incurred on a block by mining and removes the block
 * if the damage is full.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateMining {
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (DataManager.system.leftMouseDown && !wasMouseDown || DataManager.system.miningX != DataManager.system.mouseX || DataManager.system.miningY != DataManager.system.mouseY) {
			wasStructBlock = DataManager.world.isStructBlock(DataManager.system.mouseX, DataManager.system.mouseY, DataManager.savable.dungeonLevel);
		}
		if (!DataManager.system.isKeyboardBackpack && !DataManager.system.isKeyboardMenu) {
			if (DataManager.system.leftMouseDown && wasMouseDown && !DataManager.system.rightMouseDown && !DataManager.system.isMouseFar) {
				if (DataManager.system.miningX != DataManager.system.mouseX || DataManager.system.miningY != DataManager.system.mouseY) {
					DataManager.system.miningX = DataManager.system.mouseX;
					DataManager.system.miningY = DataManager.system.mouseY;
					DataManager.system.blockDamage = 0;
				}
				if (getBlockAt(DataManager.system.mouseX, DataManager.system.mouseY,DataManager.savable.dungeonLevel) != null && (wasStructBlock || (!wasStructBlock && DataManager.world.isStructBlock(DataManager.system.mouseX, DataManager.system.mouseY, DataManager.savable.dungeonLevel)))) {
					DataManager.system.blockDamage += 1.0/getBlockAt(DataManager.system.mouseX, DataManager.system.mouseY, DataManager.savable.dungeonLevel).hardness/DataManager.settings.tickLength;
					if (DataManager.system.blockDamage < 0) {
						DataManager.system.blockDamage = 0;
					}
					if (DataManager.system.blockDamage >= 10) {
						DataManager.system.blockDamage = 0;
						breakBlock(DataManager.system.mouseX, DataManager.system.mouseY, DataManager.savable.dungeonLevel);
						Location checkLocation = new Location(DataManager.characterManager.characterEntity);
						checkLocation.x = DataManager.system.mouseX;
						checkLocation.y = DataManager.system.mouseY;
						DataManager.blockUpdateManager.lighting.update(checkLocation);
					}
				} else {
					DataManager.system.blockDamage = 0;
				}
			} else {
				DataManager.system.blockDamage = 0;
			}
		}
		wasMouseDown = DataManager.system.leftMouseDown;
	}
	private static BlockItem getBlockAt(int x, int y, int level) {
		if (DataManager.world.isStructBlock(x, y, level)) {
			return DataManager.system.blockIDMap.get(DataManager.world.getStructBlock(x, y, level));
		}
		return null;

	}
	private static void breakBlock(int x, int y, int level) {
		BlockItem item = null;
		if (DataManager.world.isStructBlock(x, y, level)) {
			item = DataManager.system.blockIDMap.get(DataManager.world.getStructBlock(x, y, level));
			DataManager.world.removeStructBlock(x, y, level);

		}
		if (item != null) {
			BlockItem result = DataManager.system.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				DataManager.backpackManager.addItem(result);
			}
		}
	}
}
