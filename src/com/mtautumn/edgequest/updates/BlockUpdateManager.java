/* Checks to see what blocks need to be updated each tick. This includes
 * lighting, block breaks, footprints, etc.
 */
package com.mtautumn.edgequest.updates;

import java.util.ArrayList;

import com.mtautumn.edgequest.Location;
import com.mtautumn.edgequest.data.DataManager;

public class BlockUpdateManager extends Thread {
	DataManager dataManager;
	public UpdateLighting lighting;
	public UpdateMining mining;
	public UpdateFootprints footprints;
	public UpdateBlockPlace blockPlace;
	ArrayList<Location> lightingQueue = new ArrayList<Location>();
	public BlockUpdateManager(DataManager dataManager) {
		this.dataManager = dataManager;
		lighting = new UpdateLighting(dataManager);
		mining = new UpdateMining(dataManager);
		footprints = new UpdateFootprints(dataManager);
		blockPlace = new UpdateBlockPlace(dataManager);
	}
	public void updateLighting(Location location) {
		lightingQueue.add(location);
	}
	public void updateBlock(Location location) {
		if (dataManager.world.isStructBlock(location)) {
			if (dataManager.world.getStructBlock(location) == dataManager.system.blockNameMap.get("torch").getID()) {
				if (dataManager.world.getGroundBlock(location) == dataManager.system.blockNameMap.get("water").getID()) {
					dataManager.world.removeStructBlock(location);
				}
			}
		}
		updateLighting(location);
	}
	private boolean updateCharLighting = true;
	private boolean didUpdateLighting = false;
	private int lastCharX = 0;
	private int lastCharY = 0;
	private Location lastCharLocation = new Location(0,0);
	public void run() {
		int i = 0;
		while (dataManager.system.running) {
			try {
				if (!dataManager.system.isGameOnLaunchScreen) {
					i++;
					if (i % 30 == 0) melt();
					mining.update();
					blockPlace.update();
					footprints.update();
					if (updateCharLighting) {
						if (!lastCharLocation.isEqual(new Location(dataManager.characterManager.characterEntity)) || !didUpdateLighting) {
							updateLighting(lastCharLocation);
							lastCharLocation = new Location(dataManager.characterManager.characterEntity);
							updateLighting(lastCharLocation);
							lastCharX = lastCharLocation.x;
							lastCharY = lastCharLocation.y;
						}
						didUpdateLighting = true;
					} else if (didUpdateLighting) {
						didUpdateLighting = false;
						updateLighting(new Location(dataManager.characterManager.characterEntity));
					}
					updateCharLighting = (dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[0].getItemID()).isName("lantern") || dataManager.system.blockIDMap.get(dataManager.backpackManager.getCurrentSelection()[1].getItemID()).isName("lantern"));
				}
				Thread.sleep(dataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void melt() {
		Location updateLocation = new Location(dataManager.characterManager.characterEntity);
		for(int x = dataManager.system.minTileX; x <= dataManager.system.maxTileX; x++) {
			for(int y = dataManager.system.minTileY; y <= dataManager.system.maxTileY; y++) {
				updateLocation.x = x;
				updateLocation.y = y;
				if (dataManager.world.isGroundBlock(updateLocation)) {
					if (dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(updateLocation)).melts) {
						double brightness = 0;
						if (dataManager.world.isLight(updateLocation)) {
							brightness = Double.valueOf((dataManager.world.getLight(updateLocation) + 128)) / 255.0;
						}
						if (brightness > 0.7) {
							if (1 - Math.random() < (brightness - 0.7) / 50.0) {
								dataManager.world.setGroundBlock(updateLocation, dataManager.system.blockNameMap.get(dataManager.system.blockIDMap.get(dataManager.world.getGroundBlock(updateLocation)).meltsInto).getID());
								updateBlock(updateLocation);
							}
						}
					}
				}
			}	
		}
	}
}
