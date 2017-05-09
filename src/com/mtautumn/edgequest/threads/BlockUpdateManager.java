/* Checks to see what blocks need to be updated each tick. This includes
 * lighting, block breaks, footprints, etc.
 */
package com.mtautumn.edgequest.threads;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.updates.UpdateBlockPlace;
import com.mtautumn.edgequest.updates.UpdateFootprints;
import com.mtautumn.edgequest.updates.UpdateLighting;
import com.mtautumn.edgequest.updates.UpdateMining;

public class BlockUpdateManager extends Thread {
	public UpdateLighting lighting;
	public UpdateMining mining;
	public UpdateFootprints footprints;
	public UpdateBlockPlace blockPlace;
	ArrayList<Location> lightingQueue = new ArrayList<Location>();
	public BlockUpdateManager() {
		lighting = new UpdateLighting();
		mining = new UpdateMining();
		footprints = new UpdateFootprints();
		blockPlace = new UpdateBlockPlace();
	}
	public void updateLighting(Location location) {
		lightingQueue.add(location);
	}
	public void updateBlock(Location location) {
		if (DataManager.world.isStructBlock(location)) {
			if (DataManager.world.getStructBlock(location) == DataManager.system.blockNameMap.get("torch").getID()) {
				if (DataManager.world.getGroundBlock(location) == DataManager.system.blockNameMap.get("water").getID()) {
					DataManager.world.removeStructBlock(location);
				}
			}
		}
		updateLighting(location);
	}
	@Override
	public void run() {
		//int i = 0;
		while (DataManager.system.running) {
			try {
				if (!DataManager.system.isGameOnLaunchScreen) {
					//i++;
					//if (i % 30 == 0) melt();
					mining.update();
					blockPlace.update();
					footprints.update();
					
				}
				Thread.sleep(DataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/*
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
	*/
}
