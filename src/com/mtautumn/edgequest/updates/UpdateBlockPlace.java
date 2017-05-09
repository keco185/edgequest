package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateBlockPlace {
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (SystemData.leftMouseDown && !wasMouseDown || SystemData.miningX != SystemData.mouseX || SystemData.miningY != SystemData.mouseY) {
			wasStructBlock = DataManager.world.isStructBlock(SystemData.mouseX, SystemData.mouseY, DataManager.savable.dungeonLevel);
		}
		if (!SystemData.isKeyboardBackpack && !SystemData.isKeyboardMenu) {
			if (!SystemData.leftMouseDown && wasMouseDown && !SystemData.rightMouseDown && !SystemData.isMouseFar && !wasStructBlock) {
				if (SystemData.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[0].getItemID()).getIsBlock() && DataManager.backpackManager.getCurrentSelection()[0].getItemID() > 0) {
					placeBlock(SystemData.mouseX, SystemData.mouseY, 0);
				} else if (SystemData.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[1].getItemID()).getIsBlock()) {
					placeBlock(SystemData.mouseX, SystemData.mouseY, 1);
				}
			}
		}
		wasMouseDown = SystemData.leftMouseDown;
	}
	private static void placeBlock(int x, int y, int click) {
		Location checkLocation = new Location(DataManager.characterManager.characterEntity);
		checkLocation.x = x;
		checkLocation.y = y;
		if (!DataManager.world.isStructBlock(checkLocation) && DataManager.savable.backpackItems[6][click].getItemCount() > 0) {
			BlockItem item = SystemData.blockIDMap.get(DataManager.world.getGroundBlock(checkLocation));
			BlockItem slotItem = SystemData.blockIDMap.get(DataManager.backpackManager.getCurrentSelection()[click].getItemID());
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


