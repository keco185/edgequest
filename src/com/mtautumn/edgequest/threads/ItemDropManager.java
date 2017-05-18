package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.entities.Entity;

public class ItemDropManager extends Thread {

	@Override
	public void run() {
		while(SystemData.running) {
			try {
				for (int i = 0; i < DataManager.savable.itemDrops.size(); i++) {
					if (i + 1 < DataManager.savable.itemDrops.size()) {
						for (int j = i + 1; j < DataManager.savable.itemDrops.size(); j++) {
							if (isNear(DataManager.savable.itemDrops.get(i), DataManager.savable.itemDrops.get(j))) {
								double x = DataManager.savable.itemDrops.get(i).x + DataManager.savable.itemDrops.get(j).x;
								x /= 2;
								double y = DataManager.savable.itemDrops.get(i).y + DataManager.savable.itemDrops.get(j).y;
								y /= 2;
								DataManager.savable.itemDrops.get(i).x = x;
								DataManager.savable.itemDrops.get(i).y = y;
								DataManager.savable.itemDrops.get(j).x = x;
								DataManager.savable.itemDrops.get(j).y = y;
								if (DataManager.savable.itemDrops.get(i).item.getItemCount() + DataManager.savable.itemDrops.get(j).item.getItemCount() <= ItemSlot.maxItemCount) {
									DataManager.savable.itemDrops.get(i).item.addItems(DataManager.savable.itemDrops.get(j).item.getItemCount());
									DataManager.savable.itemDrops.remove(j);
									j--;
								}
							}
						}
					}
					DataManager.savable.itemDrops.get(i).update();
					if (DataManager.savable.itemDrops.get(i).age > SettingsData.maxItemDropAge || DataManager.savable.itemDrops.get(i).item.getItemCount() <= 0) {
						DataManager.savable.itemDrops.remove(i);
						i--;
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private static boolean isNear(ItemDrop drop1, ItemDrop drop2) {
		return Math.sqrt(Math.pow(drop1.x - drop2.x, 2) + Math.pow(drop1.y - drop2.y, 2)) < 1.0;
	}
	public boolean isItemInRange(Entity entity) {
		for (ItemDrop drop : DataManager.savable.itemDrops) {
			if (drop.level == entity.dungeonLevel) {
				if (Math.sqrt(Math.pow(drop.x - entity.getX(), 2) + Math.pow(drop.y - entity.getY(), 2)) < 1) {
					return true;
				}
			}
		}
		return false;
	}
	public void putNearbyItemsInBackpack() {
		Entity entity = CharacterManager.characterEntity;
		for (ItemDrop drop : DataManager.savable.itemDrops) {
			if (drop.level == entity.dungeonLevel) {
				if (Math.sqrt(Math.pow(drop.x - entity.getX(), 2) + Math.pow(drop.y - entity.getY(), 2)) < 1) {
					for(int i = 0; i < drop.item.getItemCount(); i++) {
						DataManager.backpackManager.addItem(SystemData.blockIDMap.get(drop.item.getItemID()));
					}
					drop.item.setItemCount(0);
				}
			}
		}
	}
	
}

