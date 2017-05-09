/*A simple class that contains an array of ItemSlot classes.
 * Essentially this is a datatype used to keep track of what's
 * in the player's backpack.
 * 
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;

public class BackpackManager extends Thread {
	DataManager dataManager;
	public BackpackManager(DataManager dataManager) {
		this.dataManager = dataManager;
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				dataManager.savable.backpackItems[i][j] = new ItemSlot();
			}
		}
	}
	@Override
	public void run() {
		while(dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					checkMouseSelection();
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public ItemSlot[] getCurrentSelection() {
		return new ItemSlot[] {dataManager.savable.leftEquipt(), dataManager.savable.rightEquipt()};
	}
	private boolean wasMouseDown = false;
	private boolean isItemGrabbed = false;
	private int[] mouseItemLocation = {-1,-1};
	private void checkMouseSelection() {
		if (!wasMouseDown && dataManager.system.leftMouseDown) {
			if (!isItemGrabbed) {
				if (!(getMouseLocation()[0] == -1)) {
					mouseItemLocation = getMouseLocation();
					dataManager.savable.mouseItem = dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]];
					dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = new ItemSlot();
					isItemGrabbed = true;
				}
			} else {
				int[] mouseLocation = getMouseLocation();
				if (!(mouseLocation[0] == -1)) {
					if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount() == 0) {
						dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = dataManager.savable.mouseItem;
						dataManager.savable.mouseItem = new ItemSlot();
						isItemGrabbed = false;
					} else {
						if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemID().equals(dataManager.savable.mouseItem.getItemID()) && dataManager.system.blockIDMap.get(dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemID()).isStackable) {
							if (dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].isSlotFull()) {
								int slotCount = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].getItemCount();
								dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].setItemCount(dataManager.savable.mouseItem.getItemCount());
								dataManager.savable.mouseItem.setItemCount(slotCount);
							} else {
								int added = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]].addItems(dataManager.savable.mouseItem.getItemCount());
								dataManager.savable.mouseItem.removeItems(added);
								if (dataManager.savable.mouseItem.getItemCount() <= 0) {
									isItemGrabbed = false;
								}
							}
						} else {
							dataManager.savable.backpackItems[mouseItemLocation[0]][mouseItemLocation[1]] = dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]];
							dataManager.savable.backpackItems[mouseLocation[0]][mouseLocation[1]] = dataManager.savable.mouseItem;
							dataManager.savable.mouseItem = new ItemSlot();
							isItemGrabbed = false;
						}

					}

				} else {
					dataManager.savable.itemDrops.add(new ItemDrop(dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), dataManager.characterManager.characterEntity.dungeonLevel, dataManager.savable.mouseItem, dataManager));
					dataManager.savable.mouseItem = new ItemSlot();
					isItemGrabbed = false;
				}
			}
			wasMouseDown = true;
		} else if (wasMouseDown && !dataManager.system.leftMouseDown) {
			wasMouseDown = false;
		}
	}
	private int[] getMouseLocation() {
		int maxX = 0;
		if (dataManager.system.isKeyboardBackpack) {
			maxX = dataManager.savable.backpackItems.length - 1;
		}
		for (int x = 0; x < maxX; x++) {
			for (int y = 0; y < dataManager.savable.backpackItems[x].length; y++) {
				int[] itemCoords = getItemSlotCoords(x, y);
				if (itemCoords[0] <= dataManager.system.mousePosition.getX() && itemCoords[1] <= dataManager.system.mousePosition.getY() && itemCoords[2] >= dataManager.system.mousePosition.getX() && itemCoords[3] >= dataManager.system.mousePosition.getY()) {
					return new int[] {x,y};
				}
			}
		}
		
		int minY = (int) (14 * dataManager.system.uiZoom);
		int maxY = (int) (72 * dataManager.system.uiZoom);
		int minXL = (int) (dataManager.settings.screenWidth / 2 - 256 * dataManager.system.uiZoom);
		int maxXL = (int) (dataManager.settings.screenWidth / 2 - 196 * dataManager.system.uiZoom);
		int minXR = (int) (dataManager.settings.screenWidth / 2 + 196 * dataManager.system.uiZoom);
		int maxXR = (int) (dataManager.settings.screenWidth / 2 + 256 * dataManager.system.uiZoom);
		int x = (int) dataManager.system.mousePosition.getX();
		int y = (int) dataManager.system.mousePosition.getY();
		if (maxY > y && minY < y) {
			if ( maxXL > x && minXL < x) {
				return new int[]{dataManager.savable.backpackItems.length - 1,0};

			} else if (maxXR > x && minXR < x) {
				return new int[]{dataManager.savable.backpackItems.length - 1,1};

			}
		}
		
		
		return new int[] {-1,-1};
	}
	private int[] getItemSlotCoords(int x, int y) {
		int[] coords = {-1,-1,0,0};
		//if (x < 2) {
		//	int xPosMin = (dataManager.settings.screenWidth - (int)(125* dataManager.system.uiZoom)) + (int)((x * 53 + 20)* dataManager.system.uiZoom);
		//	int yPosMin = (int) ((dataManager.settings.screenHeight - (int)(403 * dataManager.system.uiZoom)) / 2.0 + (int)((y * 53.5 + 66) * dataManager.system.uiZoom));
		//	int xPosMax = xPosMin + (int)(38 * dataManager.system.uiZoom);
		//	int yPosMax = yPosMin + (int)(38 * dataManager.system.uiZoom);
		//	coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		//} else {
		int xPosMin = dataManager.system.menuX + (int)(((x) * 64 + 37) * dataManager.system.uiZoom);
		int yPosMin = dataManager.system.menuY + (int)((y * 65 + 94) * dataManager.system.uiZoom);
		int xPosMax = xPosMin + (int)(48 * dataManager.system.uiZoom);
		int yPosMax = yPosMin + (int)(48 * dataManager.system.uiZoom);
		coords = new int[] {xPosMin, yPosMin, xPosMax, yPosMax};
		//}
		return coords;
	}
	public void addItem(BlockItem item) {
		if (isItemInBackpack(item) && item.isStackable) {
			boolean foundSpot = false;
			for(int i = 0; i < dataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < dataManager.savable.backpackItems[i].length && !foundSpot; j++) {
					if (dataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !dataManager.savable.backpackItems[i][j].isSlotFull()) {
						ItemSlot slot = dataManager.savable.backpackItems[i][j];
						slot.addOne();
						foundSpot = true;
					}
				}
			}
		} else {
			boolean foundSpot = false;
			for(int i = 0; i < dataManager.savable.backpackItems.length && !foundSpot; i++) {
				for(int j = 0; j < dataManager.savable.backpackItems[i].length && !foundSpot; j++) {

					ItemSlot slot = dataManager.savable.backpackItems[i][j];
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
						slot = dataManager.savable.leftEquipt();
					} else {
						slot = dataManager.savable.rightEquipt();
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
	private boolean isItemInBackpack(BlockItem item) {
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				if (dataManager.savable.backpackItems[i][j].getItemID().equals(item.getID()) && !dataManager.savable.backpackItems[i][j].isSlotFull()) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean removeItemFromBackpack(BlockItem item) {
		for(int i = 0; i < dataManager.savable.backpackItems.length; i++) {
			for(int j = 0; j < dataManager.savable.backpackItems[i].length; j++) {
				if (dataManager.savable.backpackItems[i][j].getItemID().equals(item.getID())) {
					if (dataManager.savable.backpackItems[i][j].getItemCount() > 0) {
						dataManager.savable.backpackItems[i][j].subtractOne();
						if (dataManager.savable.backpackItems[i][j].getItemCount() == 0) {
							dataManager.savable.backpackItems[i][j] = new ItemSlot();
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
