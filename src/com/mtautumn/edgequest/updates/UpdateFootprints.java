/* Gets called by BlockUpdateManager and checks to see if a new footprint
 * should be added.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.FootPrint;

public class UpdateFootprints {
	double lastFootX = 0.0;
	double lastFootY = 0.0;
	public void update() {
		int charX = (int) Math.floor(DataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(DataManager.characterManager.characterEntity.getY());
		if (DataManager.world.isGroundBlock(DataManager.characterManager.characterEntity, charX, charY)) {
			if (SystemData.blockIDMap.get(DataManager.world.getGroundBlock(DataManager.characterManager.characterEntity, charX, charY)).canHavePrints) {
				if (Math.sqrt(Math.pow(DataManager.characterManager.characterEntity.getX() - lastFootX, 2)+Math.pow(DataManager.characterManager.characterEntity.getY() - lastFootY, 2)) > 0.7) {
					lastFootX = DataManager.characterManager.characterEntity.getX();
					lastFootY = DataManager.characterManager.characterEntity.getY();
					DataManager.savable.footPrints.add(new FootPrint(DataManager.characterManager.characterEntity.getX(), DataManager.characterManager.characterEntity.getY(), DataManager.characterManager.characterEntity.getMoveRot(), DataManager.characterManager.characterEntity.dungeonLevel));
				}
			}
		}
		for (int i = 0; i < DataManager.savable.footPrints.size(); i++) {
			DataManager.savable.footPrints.get(i).opacity -= 0.001;
			if (DataManager.savable.footPrints.get(i).opacity <= 0) {
				DataManager.savable.footPrints.remove(i);
			}

		}
	}
}
