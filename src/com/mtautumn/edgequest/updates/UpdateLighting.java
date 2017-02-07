/* Called by BlockUpdateManager (mostly) and will update lighting when the
 * update method is called (at the position x,y)
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateLighting {
	DataManager dataManager;
	public UpdateRayCast urc;
	public UpdateLighting(DataManager dataManager) {
		this.dataManager = dataManager;
		urc = new UpdateRayCast(dataManager);
	}
	public void update(Location location) {
		urc.update(location);
	}
}
