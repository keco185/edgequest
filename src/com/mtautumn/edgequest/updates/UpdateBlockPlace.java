package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateBlockPlace {
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (DataManager.system.leftMouseDown && !wasMouseDown || DataManager.system.miningX != DataManager.system.mouseX || DataManager.system.miningY != DataManager.system.mouseY) {
			wasStructBlock = DataManager.world.isStructBlock(DataManager.system.mouseX, DataManager.system.mouseY, DataManager.savable.dungeonLevel);
		}
		if (!DataManager.system.isKeyboardBackpack && !DataManager.system.isKeyboardMenu) {
			if (!DataManager.system.leftMouseDown && wasMouseDown && !DataManager.system.rightMouseDown && !DataManager.system.isMouseFar && !wasStructBlock) {
				if (DataManager.system.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[0].getItemID()).getIsBlock() && DataManager.backpackManager.getCurrentSelection()[0].getItemID() > 0) {
					placeBlock(DataManager.system.mouseX, DataManager.system.mouseY, 0);
				} else if (DataManager.system.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[1].getItemID()).getIsBlock()) {
					placeBlock(DataManager.system.mouseX, DataManager.system.mouseY, 1);
				}
			}
		}
		wasMouseDown = DataManager.system.leftMouseDown;
	}
	private static void placeBlock(int x, int y, int click) {
		Location checkLocation = new Location(DataManager.characterManager.characterEntity);
		checkLocation.x = x;
		checkLocation.y = y;
		if (!DataManager.world.isStructBlock(checkLocation) && DataManager.savable.backpackItems[6][click].getItemCount() > 0) {
			BlockItem item = DataManager.system.blockIDMap.get(DataManager.world.getGroundBlock(checkLocation));
			BlockItem slotItem = DataManager.system.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[click].getItemID());
			if ((item.isName("water") || item.isName("ground")) && slotItem.isSolid) {
				DataManager.world.setGroundBlock(checkLocation, slotItem.getID());
				DataManager.savable.backpackItems[6][click].subtractOne();
			} else if ((item.isName("grass") || item.isName("dirt")) && slotItem.isName("snow")) {
				DataManager.world.setGroundBlock(checkLocation, slotItem.getID());
				DataManager.savable.backpackItems[6][click].subtractOne();
			} else {
				DataManager.world.setStructBlock(checkLocation, slotItem.getID());
				DataManager.savable.backpackItems[6][click].subtractOne();
			}
			DataManager.blockUpdateManager.updateBlock(checkLocation);
			if (DataManager.savable.backpackItems[6][click].getItemCount() <= 0) {
				DataManager.savable.backpackItems[6][click] = new ItemSlot();
			}
		}
	}
}


