/* Just updates a variable in dataManager which is used when rendering
 * animations. The animation step is found by using modulus on this variable.
 * 
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class AnimationClock extends Thread {
	@Override
	public void run() {
		while (SystemData.running) {
			try {
				if (!SystemData.isGameOnLaunchScreen) {
					SystemData.animationClock++;
				}
				Thread.sleep(SettingsData.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
