/* Gets called by BlockUpdateManager and checks to see if a new footprint
 * should be added.
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.FootPrint;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.WorldUtils;

public class UpdateFootprints {
	double lastFootX = 0.0;
	double lastFootY = 0.0;
	public void update() {
		int charX = (int) Math.floor(CharacterManager.characterEntity.getX());
		int charY = (int) Math.floor(CharacterManager.characterEntity.getY());
		if (WorldUtils.isGroundBlock(CharacterManager.characterEntity, charX, charY)) {
			if (SystemData.blockIDMap.get(WorldUtils.getGroundBlock(CharacterManager.characterEntity, charX, charY)).canHavePrints) {
				if (Math.sqrt(Math.pow(CharacterManager.characterEntity.getX() - lastFootX, 2)+Math.pow(CharacterManager.characterEntity.getY() - lastFootY, 2)) > 0.7) {
					lastFootX = CharacterManager.characterEntity.getX();
					lastFootY = CharacterManager.characterEntity.getY();
					DataManager.savable.footPrints.add(new FootPrint(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), CharacterManager.characterEntity.getMoveRot(), CharacterManager.characterEntity.dungeonLevel));
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
