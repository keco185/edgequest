/*This class creates a new player (character) entity when the game starts
 * It also handles torch places
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Character;

public class CharacterManager extends Thread{
	BlockUpdateManager blockUpdateManager;
	public Character characterEntity;
	public CharacterManager() {
		blockUpdateManager = DataManager.blockUpdateManager;
	}
	public void charPlaceTorch() {
		Location location = new Location(characterEntity);
		if (!DataManager.world.isStructBlock(location)) {
			if (!characterEntity.getRelativeGroundBlock(0, 0).isLiquid) {
				DataManager.world.setStructBlock(location, DataManager.system.blockNameMap.get("torch").getID());
				DataManager.world.addLightSource(location);
				blockUpdateManager.updateBlock(location);
			}
		} else if (DataManager.world.getStructBlock(location) == DataManager.system.blockNameMap.get("torch").getID()) {
			DataManager.world.removeStructBlock(location);
			DataManager.world.removeLightSource(location);
			blockUpdateManager.updateBlock(location);
		}
	}
	@Override
	public void run() {
		createCharacterEntity();
		while (DataManager.system.running) {
			try {
				if (!DataManager.system.characterLocationSet) {
					Location location = new Location(characterEntity);
					if (DataManager.world.isGroundBlock(location)) {
						BlockItem charBlock = DataManager.system.blockIDMap.get(DataManager.world.getGroundBlock(location));
						if (charBlock.isName("water") || charBlock.isName("ice")) {
							characterEntity.setPos(characterEntity.getX()+1, characterEntity.getY());
						} else {
							DataManager.system.characterLocationSet = true;
							characterEntity.move(0.5, 0.5);
						}
					}
				}
				Thread.sleep(DataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome
		int charX = (int) Math.floor(DataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(DataManager.characterManager.characterEntity.getY());
			if (DataManager.world.isGroundBlock(DataManager.characterManager.characterEntity, charX, charY)) {
				blockInfo[0] = DataManager.world.getGroundBlock(DataManager.characterManager.characterEntity, charX, charY);
			}
			if (DataManager.world.isStructBlock(DataManager.characterManager.characterEntity, charX, charY)) {
				blockInfo[1] = DataManager.world.getStructBlock(DataManager.characterManager.characterEntity, charX, charY);
			}
		return blockInfo;
	}
	public void createCharacterEntity() {
		characterEntity = new Character(0, 0, 0, -1);
		DataManager.savable.entities.add(characterEntity);
	}
}
