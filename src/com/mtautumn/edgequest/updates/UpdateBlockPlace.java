package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateBlockPlace {
	DataManager dataManager;
	public UpdateBlockPlace(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	private boolean wasMouseDown = false;
	private boolean wasStructBlock = false;
	public void update() {
		if (dataManager.system.leftMouseDown && !wasMouseDown || dataManager.system.miningX != dataManager.system.mouseX || dataManager.system.miningY != dataManager.system.mouseY) {
			wasStructBlock = dataManager.world.isStructBlock(dataManager.system.mouseX, dataManager.system.mouseY, dataManager.savable.dungeonLevel);
		}
		if (!dataManager.system.isKeyboardBackpack && !dataManager.system.isKeyboardMenu) {
			if (!dataManager.system.leftMouseDown && wasMouseDown && !dataManager.system.rightMouseDown && !dataManager.system.isMouseFar && !wasStructBlock) {
				if (dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[0].getItemID()).getIsBlock() && dataManager.backpackManager.getCurrentSelection()[0].getItemID() > 0) {
					placeBlock(dataManager.system.mouseX, dataManager.system.mouseY, 0);
				} else if (dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[1].getItemID()).getIsBlock()) {
					placeBlock(dataManager.system.mouseX, dataManager.system.mouseY, 1);
				}
			}
		}
		wasMouseDown = dataManager.system.leftMouseDown;
	}
	private void placeBlock(int x, int y, int click) {
		Location checkLocation = new Location(dataManager.characterManager.characterEntity);
		checkLocation.x = x;
		checkLocation.y = y;
		if (!dataManager.world.isStructBlock(checkLocation) && dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].getItemCount() > 0) {
			BlockItem item = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(checkLocation));
			BlockItem slotItem = dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[click].getItemID());
			if ((item.isName("water") || item.isName("ground")) && slotItem.isSolid) {
				dataManager.world.setGroundBlock(checkLocation, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			} else if ((item.isName("grass") || item.isName("dirt")) && slotItem.isName("snow")) {
				dataManager.world.setGroundBlock(checkLocation, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			} else {
				dataManager.world.setStructBlock(checkLocation, slotItem.getID());
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].subtractOne();
			}
			dataManager.blockUpdateManager.updateBlock(checkLocation);
			if (dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection].getItemCount() <= 0) {
				dataManager.savable.backpackItems[click][dataManager.savable.hotBarSelection] = new ItemSlot();
			}
		}
	}
}


