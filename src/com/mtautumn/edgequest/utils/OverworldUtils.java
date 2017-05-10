/* A series of methods for setting, getting, or checking blocks, structures, or
 * lighting for each tile in the overworld.(Normally accessed using
 * world utils @ dataManager.world)
 */
package com.mtautumn.edgequest.utils;

import com.mtautumn.edgequest.data.DataManager;

public class OverworldUtils {

	public void setStructBlock(int x, int y, short id) {
		DataManager.world.setStructBlock(x, y, -1, id);
	}
	public short getStructBlock(int x, int y) {
			return DataManager.world.getStructBlock(x, y, -1);
	}
	public boolean isStructBlock(int x, int y) {
		return DataManager.world.isStructBlock(x, y, -1);
	}
	public void removeStructBlock(int x, int y) {
		DataManager.world.removeStructBlock(x, y, -1);
	}

	public void setGroundBlock(int x, int y, short id) {
		DataManager.world.setGroundBlock(x, y, -1, id);
	}
	public short getGroundBlock(int x, int y) {
		return DataManager.world.getGroundBlock(x, y, -1);
	}
	public boolean isGroundBlock(int x, int y) {
		return DataManager.world.isGroundBlock(x, y, -1);
	}
	public void removeGroundBlock(int x, int y) {
		DataManager.world.removeGroundBlock(x, y, -1);
	}
	public void addLightSource(int x, int y) {
		DataManager.world.addLightSource(x, y, -1);
	}
	public void removeLightSource(int x, int y) {
		DataManager.world.removeLightSource(x, y, -1);
	}
	public boolean isLightSource(int x, int y) {
		return DataManager.world.isLightSource(x, y, -1);
	}
}
