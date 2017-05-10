/* Updates damage incurred on a block by mining and removes the block
 * if the damage is full.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.threads.CharacterManager;

public class UpdateMining {
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (SystemData.leftMouseDown && !wasMouseDown || SystemData.miningX != SystemData.mouseX || SystemData.miningY != SystemData.mouseY) {
			wasStructBlock = DataManager.world.isStructBlock(SystemData.mouseX, SystemData.mouseY, DataManager.savable.dungeonLevel);
		}
		if (!SystemData.isKeyboardBackpack && !SystemData.isKeyboardMenu) {
			if (SystemData.leftMouseDown && wasMouseDown && !SystemData.rightMouseDown && !SystemData.isMouseFar) {
				if (SystemData.miningX != SystemData.mouseX || SystemData.miningY != SystemData.mouseY) {
					SystemData.miningX = SystemData.mouseX;
					SystemData.miningY = SystemData.mouseY;
					SystemData.blockDamage = 0;
				}
				if (getBlockAt(SystemData.mouseX, SystemData.mouseY,DataManager.savable.dungeonLevel) != null && (wasStructBlock || (!wasStructBlock && DataManager.world.isStructBlock(SystemData.mouseX, SystemData.mouseY, DataManager.savable.dungeonLevel)))) {
					SystemData.blockDamage += 1.0/getBlockAt(SystemData.mouseX, SystemData.mouseY, DataManager.savable.dungeonLevel).hardness/SettingsData.tickLength;
					if (SystemData.blockDamage < 0) {
						SystemData.blockDamage = 0;
					}
					if (SystemData.blockDamage >= 10) {
						SystemData.blockDamage = 0;
						breakBlock(SystemData.mouseX, SystemData.mouseY, DataManager.savable.dungeonLevel);
						Location checkLocation = new Location(CharacterManager.characterEntity);
						checkLocation.x = SystemData.mouseX;
						checkLocation.y = SystemData.mouseY;
						DataManager.blockUpdateManager.lighting.update(checkLocation);
					}
				} else {
					SystemData.blockDamage = 0;
				}
			} else {
				SystemData.blockDamage = 0;
			}
		}
		wasMouseDown = SystemData.leftMouseDown;
	}
	private static BlockItem getBlockAt(int x, int y, int level) {
		if (DataManager.world.isStructBlock(x, y, level)) {
			return SystemData.blockIDMap.get(DataManager.world.getStructBlock(x, y, level));
		}
		return null;

	}
	private static void breakBlock(int x, int y, int level) {
		BlockItem item = null;
		if (DataManager.world.isStructBlock(x, y, level)) {
			item = SystemData.blockIDMap.get(DataManager.world.getStructBlock(x, y, level));
			DataManager.world.removeStructBlock(x, y, level);

		}
		if (item != null) {
			BlockItem result = SystemData.blockNameMap.get(item.breaksInto);
			if (result.getIsItem()) {
				DataManager.backpackManager.addItem(result);
			}
		}
	}
}
