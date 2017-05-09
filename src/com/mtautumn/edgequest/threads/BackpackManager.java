/*A simple class that contains an array of ItemSlot classes.
 * Essentially this is a datatype used to keep track of what's
 * in the player's backpack.
 * 
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;

public class BackpackManager extends Thread {
	public BackpackManager() {
		for(int i = 0; i < DataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				DataManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
	@Override
	public void run() {
		while(SystemData.running) {
			try {
				if (!SystemData.isGameOnLaunchScreen) {
					checkMouseSelection();
				}
				Thread.sleep(SettingsData.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public ItemSlot[] getCurrentSelection() {
		return new ItemSlot[] {DataManager.savable.leftEquipt(), DataManager.savable.rightEquipt()};
	}
	private boolean wasMouseDown = false;
	private boolean isItemGrabbed = false;
	private int[] mouseItemLocation = {-1,-1};
	private void checkMouseSelection() {
		if (!wasMouseDown && SystemData.leftMouseDown) {
			if (!isItemGrabbed) {
				if (!(getMouseLocation()[0] == -1)) {
					mouseItemLocation = getMouseLocation();
					DataManager.savable.mouseItem = DataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]];
					DataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = new ItemSlot();
					isItemGrabbed = true;
				}
			} else {
				int[] mouseLocation = getMouseLocation();
				if (!(mouseLocation[0] == -1)) {
					if (DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount() == 0) {
						DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = DataManager.savable.mouseItem;
						DataManager.savable.mouseItem = new ItemSlot();
						isItemGrabbed = false;
					} else {
						if (DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemID().equals(DataManager.savable.mouseItem.getItemID()) && SystemData.blockIDMap.get(DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemID()).isStackable) {
							if (DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].isSlotFull()) {
								int slotCount = DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount();
								DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].setItemCount(DataManager.savable.mouseItem.getItemCount());
								DataManager.savable.mouseItem.setItemCount(slotCount);
							} else {
								int added = DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].addItems(DataManager.savable.mouseItem.getItemCount());
								DataManager.savable.mouseItem.removeItems(added);
								if (DataManager.savable.mouseItem.getItemCount() <= 0) {
									isItemGrabbed = false;
								}
							}
						} else {
							DataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]];
							DataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = DataManager.savable.mouseItem;
							DataManager.savable.mouseItem = new ItemSlot();
							isItemGrabbed = false;
						}

					}

				} else {
					DataManager.savable.itemDrops.add(new ItemDrop(DataManager.characterManager.characterEntity.getX(), DataManager.characterManager.characterEntity.getY(), DataManager.characterManager.characterEntity.dungeonLevel, DataManager.savable.mouseItem));
					DataManager.savable.mouseItem = new ItemSlot();
					isItemGrabbed = false;
				}
			}
			wasMouseDown = true;
		} else if (wasMouseDown && !SystemData.leftMouseDown) {
			wasMouseDown = false;
		}
	}
	private static int[] getMouseLocation() {
		int maxX = 0;
		if (SystemData.isKeyboardBackpack) {
			maxX = DataManager.savable.backpackItems.length - 1;
		}
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < DataManager.savable.backpackItems[x].length; y++) {
				int[] itemCoords = getItemSlotCoords(x, y);
				if (itemCoords[0] <= SystemData.mousePosition.getX() && itemCoords[1] <= SystemData.mousePosition.getY() && itemCoords[2] >= SystemData.mousePosition.getX() && itemCoords[3] >= SystemData.mousePosition.getY()) {
					return new int[] {x,y};
				}
			}
		}
		
		int minY = (int) (14 * SystemData.uiZoom);
		int maxY = (int) (72 * SystemData.uiZoom);
		int minXL = (int) (SettingsData.screenWidth / 2 - 256 * SystemData.uiZoom);
		int maxXL = (int) (SettingsData.screenWidth / 2 - 196 * SystemData.uiZoom);
		int minXR = (int) (SettingsData.screenWidth / 2 + 196 * SystemData.uiZoom);
		int maxXR = (int) (SettingsData.screenWidth / 2 + 256 * SystemData.uiZoom);
		int x = (int) SystemData.mousePosition.getX();
		int y = (int) SystemData.mousePosition.getY();
		if (maxY > y && minY < y) {
			if ( maxXL > x && minXL < x) {
				return new int[]{DataManager.savable.backpackItems.length - 1,0};

			} else if (maxXR > x && minXR < x) {
				return new int[]{DataManager.savable.backpackItems.length - 1,1};

			}
		}
		
		
		return new int[] {-1,-1};
	}
	private static int[] getItemSlotCoords(int x, int y) {
		int[] coords = {-1,-1,0,0};
		//if (x < 2) {
		//	int xPosMin = (dataManager.settings.screenWidth - (int)(125* dataManager.system.uiZoom)) + (int)((x * 53 + 20)* dataManager.system.uiZoom);
		//	int yPosMin = (int) ((dataManager.settings.screenHeight - (int)(403 * dataManager.system.uiZoom)) / 2.0 + (int)((y * 53.5 + 66) * dataManager.system.uiZoom));
		//	int xPosMax = xPosMin + (int)(38 * dataManager.system.uiZoom);
		//	int yPosMax = yPosMin + (int)(38 * dataManager.system.uiZoom);
		//	coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		//} else {
		int xPosMin = SystemData.menuX + (int)(((x) * 64 + 37) * SystemData.uiZoom);
		int yPosMin = SystemData.menuY + (int)((y * 65 + 94) * SystemData.uiZoom);
		int xPosMax = xPosMin + (int)(48 * SystemData.uiZoom);
		int yPosMax = yPosMin + (int)(48 * SystemData.uiZoom);
		coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		//}
		return coords;
	}
	public void addItem(BlockItem item) {
		if (isItemInBackpack(item) && item.isStackable) {
			boolean foundSpot = false;
			for(int i = 0; i < DataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < DataManager.savable.backpackItems[i].length && !foundSpot; j++) {
					if (DataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !DataManager.savable.backpackItems[i][j].isSlotFull()) {
						ItemSlot slot = DataManager.savable.backpackItems[i][j];
						slot.addOne();
						foundSpot = true;
					}
				}
			}
		} else {
			boolean foundSpot = false;
			for(int i = 0; i < DataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < DataManager.savable.backpackItems[i].length && !foundSpot; j++) {

					ItemSlot slot = DataManager.savable.backpackItems[i][j];
					if (slot.getItemCount() == 0) {
						slot.setItem(item.getID());
						slot.setItemCount(1);
						slot.itemHealth = item.maxHealth;
						foundSpot = true;
					} else if (slot.getItemID().equals(item.getID()) && !slot.isSlotFull() && item.isStackable) {
						slot.addOne();
						foundSpot = true;
					}
				}
			}
			if (!foundSpot) {
				for (int i = 0; i < 2 && !foundSpot; i++) {
					ItemSlot slot;
					if (i == 0) {
						slot = DataManager.savable.leftEquipt();
					} else {
						slot = DataManager.savable.rightEquipt();
					}
					if (slot.getItemCount() == 0) {
						slot.setItem(item.getID());
						slot.setItemCount(1);
						slot.itemHealth = item.maxHealth;
						foundSpot = true;
					} else if (slot.getItemID().equals(item.getID()) && !slot.isSlotFull() && item.isStackable) {
						slot.addOne();
						foundSpot = true;
					}
				}
			}
		}
	}
	private static boolean isItemInBackpack(BlockItem item) {
		for(int i = 0; i < DataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				if (DataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !DataManager.savable.backpackItems[i][j].isSlotFull()) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean removeItemFromBackpack(BlockItem item) {
		for(int i = 0; i < DataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				if (DataManager.savable.backpackItems[i][j].getItemID().equals(item.getID())) {
					if (DataManager.savable.backpackItems[i][j].getItemCount() > 0) {
						DataManager.savable.backpackItems[i][j].subtractOne();
						if (DataManager.savable.backpackItems[i][j].getItemCount() == 0) {
							DataManager.savable.backpackItems[i][j] = new ItemSlot();
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
