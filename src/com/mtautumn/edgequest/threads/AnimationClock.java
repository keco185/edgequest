/* Just updates a variable in dataManager which is used when rendering
 * animations. The animation step is found by using modulus on this variable.
 * 
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;

public class AnimationClock extends Thread {
	@Override
	public void run() {
		while (DataManager.system.running) {
			try {
				if (!DataManager.system.isGameOnLaunchScreen) {
					DataManager.system.animationClock++;
				}
				Thread.sleep(DataManager.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
