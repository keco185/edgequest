/*This class is in charge of adjusting the character position in the world.
 * (It also runs the place torch event for the time being but will be switched
 * to a place block event in the future)
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.updates.BlockUpdateManager;

public class CharacterManager extends Thread{
	DataManager dataManager;
	BlockUpdateManager blockUpdateManager;
	public Character characterEntity;
	public CharacterManager(DataManager dataManager) {
		this.dataManager = dataManager;
		blockUpdateManager = dataManager.blockUpdateManager;
	}
	public void charPlaceTorch() {
		int charX = (int) Math.floor(characterEntity.getX());
		int charY = (int) Math.floor(characterEntity.getY());
		if (!dataManager.world.isStructBlock(charX, charY)) {
			if ((short)getCharaterBlockInfo()[0] != dataManager.system.blockNameMap.get("water").getID()) {
				dataManager.world.setStructBlock(charX, charY, dataManager.system.blockNameMap.get("torch").getID());
				blockUpdateManager.updateBlock(charX, charY);
			}
		} else if (dataManager.world.getStructBlock(charX, charY) == dataManager.system.blockNameMap.get("torch").getID()) {
			dataManager.world.removeStructBlock(charX, charY);
			blockUpdateManager.updateBlock(charX, charY);
		}
	}
	public void run() {
		createCharacterEntity();
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					characterEntity.update();
				}
				if (!dataManager.system.characterLocationSet) {
					if (dataManager.world.isGroundBlock((int)characterEntity.getX(), (int)characterEntity.getY())) {
						BlockItem charBlock = dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock((int)characterEntity.getX(), (int)characterEntity.getY()));
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
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
			if (dataManager.world.isGroundBlock(charX, charY)) {
				blockInfo[0] = dataManager.world.getGroundBlock(charX, charY);
			}
			if (dataManager.world.isStructBlock(charX, charY)) {
				blockInfo[1] = dataManager.world.getStructBlock(charX, charY);
			}
			if (dataManager.world.isLight(charX, charY)) {
				blockInfo[3] = dataManager.world.getLight(charX, charY);
			}
		return blockInfo;
	}
	public void createCharacterEntity() {
		characterEntity = new Character(0, 0, (byte) 0, dataManager);
		dataManager.savable.entities.add(characterEntity);
	}
}
