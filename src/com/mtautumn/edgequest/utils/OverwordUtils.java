/* A series of methods for setting, getting, or checking blocks, structures, or
 * lighting for each tile in the overworld.(Normally accessed using
 * world utils @ dataManager.world)
 */
package com.mtautumn.edgequest.utils;

import com.mtautumn.edgequest.data.DataManager;

public class OverwordUtils {
	DataManager dm;
	public OverwordUtils(DataManager dm) {
		this.dm = dm;
	}


	public void setStructBlock(int x, int y, short id) {
		dm.savable.playerStructuresMap.put(x+","+y+","+-1, id);
	}
	public short getStructBlock(int x, int y) {
		if (isStructBlock(x, y)) {
			return dm.savable.playerStructuresMap.get(x+","+y+","+-1);
		}
		return 0;
	}
	public boolean isStructBlock(int x, int y) {
		return dm.savable.playerStructuresMap.containsKey(x+","+y+","+-1);
	}
	public void removeStructBlock(int x, int y) {
		dm.savable.playerStructuresMap.remove(x+","+y+","+-1);
	}

	public void setGroundBlock(int x, int y, short id) {
		dm.savable.map.put(x+","+y+","+-1, id);
	}
	public short getGroundBlock(int x, int y) {
		if (isGroundBlock(x, y)) {
			return  dm.savable.map.get(x+","+y+","+-1);
		}
		return 0;
	}
	public boolean isGroundBlock(int x, int y) {
		return dm.savable.map.containsKey(x+","+y+","+-1);
	}
	public void removeGroundBlock(int x, int y) {
		dm.savable.map.remove(x+","+y+","+-1);
	}

	public void setLight(int x, int y, byte val) {
		dm.savable.lightMap.put(x+","+y+","+-1, val);
	}
	public byte getLight(int x, int y) {
		if (isLight(x, y)) {
			return dm.savable.lightMap.get(x+","+y+","+-1);
		}
		return Byte.MIN_VALUE;
	}
	public boolean isLight(int x, int y) {
		return dm.savable.lightMap.containsKey(x+","+y+","+-1);
	}
	public void removeLight(int x, int y) {
		dm.savable.lightMap.remove(x+","+y+","+-1);
	}
}
