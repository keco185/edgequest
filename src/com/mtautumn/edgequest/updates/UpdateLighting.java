/* Called by BlockUpdateManager (mostly) and will update lighting when the
 * update method is called (at the position x,y)
 */
package com.mtautumn.edgequest.updates;

import com.mtautumn.edgequest.dataObjects.Location;

public class UpdateLighting {
	public UpdateRayCast urc;
	public UpdateLighting() {
		urc = new UpdateRayCast();
	}
	public void update(Location location) {
		urc.update(location);
	}
}
