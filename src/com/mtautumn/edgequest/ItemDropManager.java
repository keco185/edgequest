package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class ItemDropManager extends Thread {
	DataManager dm;
	public ItemDropManager(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		while(dm.system.running) {
			try {
				for (int i = 0; i < dm.savable.itemDrops.size(); i++) {
					if (i + 1 < dm.savable.itemDrops.size()) {
						for (int j = i + 1; j < dm.savable.itemDrops.size(); j++) {
							if (isNear(dm.savable.itemDrops.get(i), dm.savable.itemDrops.get(j))) {
								double x = dm.savable.itemDrops.get(i).x + dm.savable.itemDrops.get(j).x;
								x /= 2;
								double y = dm.savable.itemDrops.get(i).y + dm.savable.itemDrops.get(j).y;
								y /= 2;
								dm.savable.itemDrops.get(i).x = x;
								dm.savable.itemDrops.get(i).y = y;
								dm.savable.itemDrops.get(j).x = x;
								dm.savable.itemDrops.get(j).y = y;
								if (dm.savable.itemDrops.get(i).item.getItemCount() + dm.savable.itemDrops.get(j).item.getItemCount() <= ItemSlot.maxItemCount) {
									dm.savable.itemDrops.get(i).item.addItems(dm.savable.itemDrops.get(j).item.getItemCount());
									dm.savable.itemDrops.remove(j);
									j--;
								}
							}
						}
					}
					dm.savable.itemDrops.get(i).update();
					if (dm.savable.itemDrops.get(i).age > dm.settings.maxItemDropAge) {
						dm.savable.itemDrops.remove(i);
						i--;
					}
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	private boolean isNear(ItemDrop drop1, ItemDrop drop2) {
		return Math.sqrt(Math.pow(drop1.x - drop2.x, 2) + Math.pow(drop1.y - drop2.y, 2)) < 1.0;
	}
}

