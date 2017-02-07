/*This class creates a new player (character) entity when the game starts
 * It also handles torch places
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Character;

public class CharacterManager extends Thread{
	DataManager dataManager;
	BlockUpdateManager blockUpdateManager;
	public Character characterEntity;
	public CharacterManager(DataManager dataManager) {
		this.dataManager = dataManager;
		blockUpdateManager = dataManager.blockUpdateManager;
	}
	public void charPlaceTorch() {
		Location location = new Location(characterEntity);
		if (!dataManager.world.isStructBlock(location)) {
			if (!characterEntity.getRelativeGroundBlock(0, 0).isLiquid) {
				dataManager.world.setStructBlock(location, dataManager.system.blockNameMap.get("torch").getID());
				dataManager.world.addLightSource(location);
				blockUpdateManager.updateBlock(location);
			}
		} else if (dataManager.world.getStructBlock(location) == dataManager.system.blockNameMap.get("torch").getID()) {
			dataManager.world.removeStructBlock(location);
			dataManager.world.removeLightSource(location);
			blockUpdateManager.updateBlock(location);
		}
	}
	public void run() {
		createCharacterEntity();
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.characterLocationSet) {
					Location location = new Location(characterEntity);
					if (dataManager.world.isGroundBlock(location)) {
						BlockItem charBlock = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(location));
						if (charBlock.isName("water") || charBlock.isName("ice")) {
							characterEntity.setPos(characterEntity.getX()+1, characterEntity.getY());
						} else {
							dataManager.system.characterLocationSet = true;
							characterEntity.move(0.5, 0.5);
						}
					}
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
			if (dataManager.world.isGroundBlock(dataManager.characterManager.characterEntity, charX, charY)) {
				blockInfo[0] = dataManager.world.getGroundBlock(dataManager.characterManager.characterEntity, charX, charY);
			}
			if (dataManager.world.isStructBlock(dataManager.characterManager.characterEntity, charX, charY)) {
				blockInfo[1] = dataManager.world.getStructBlock(dataManager.characterManager.characterEntity, charX, charY);
			}
		return blockInfo;
	}
	public void createCharacterEntity() {
		characterEntity = new Character(0, 0, 0, -1, dataManager);
		dataManager.savable.entities.add(characterEntity);
	}
}