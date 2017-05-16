/*This class creates a new player (character) entity when the game starts
 * It also handles torch places
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Character;
import com.mtautumn.edgequest.utils.WorldUtils;

public class CharacterManager extends Thread {
	BlockUpdateManager blockUpdateManager;
	public static Character characterEntity;
	public CharacterManager() {
		blockUpdateManager = DataManager.blockUpdateManager;
	}
	public void charPlaceTorch() {
		Location location = new Location(characterEntity);
		if (!WorldUtils.isStructBlock(location)) {
			if (!characterEntity.getRelativeGroundBlock(0, 0).isLiquid) {
				WorldUtils.setStructBlock(location, SystemData.blockNameMap.get("torch").getID());
				WorldUtils.addLightSource(location);
				blockUpdateManager.updateBlock(location);
			}
		} else if (WorldUtils.getStructBlock(location) == SystemData.blockNameMap.get("torch").getID()) {
			WorldUtils.removeStructBlock(location);
			WorldUtils.removeLightSource(location);
			blockUpdateManager.updateBlock(location);
		}
	}
	@Override
	public void run() {
		createCharacterEntity();
		while (SystemData.running) {
			try {
				if (!SystemData.characterLocationSet && !SystemData.isGameOnLaunchScreen) {
					Location location = new Location(characterEntity);
					if (WorldUtils.isGroundBlock(location)) {
						BlockItem charBlock = SystemData.blockIDMap.get(WorldUtils.getGroundBlock(location));
						if (charBlock.isName("water") || charBlock.isName("ice")) {
							characterEntity.setPos(characterEntity.getX()+1, characterEntity.getY());
						} else {
							SystemData.characterLocationSet = true;
							characterEntity.move(0.5, 0.5);
						}
					}
				} 
				Thread.sleep(SettingsData.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome
		int charX = (int) Math.floor(CharacterManager.characterEntity.getX());
		int charY = (int) Math.floor(CharacterManager.characterEntity.getY());
			if (WorldUtils.isGroundBlock(CharacterManager.characterEntity, charX, charY)) {
				blockInfo[0] = WorldUtils.getGroundBlock(CharacterManager.characterEntity, charX, charY);
			}
			if (WorldUtils.isStructBlock(CharacterManager.characterEntity, charX, charY)) {
				blockInfo[1] = WorldUtils.getStructBlock(CharacterManager.characterEntity, charX, charY);
			}
		return blockInfo;
	}
	public void createCharacterEntity() {
		characterEntity = new Character(0, 0, 0, -1);
		DataManager.savable.entities.add(characterEntity);
	}
}
