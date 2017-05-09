//Basically just sets the in-game time and updates itself periodically
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;

public class GameClock extends Thread {
	@Override
	public void run() {
		while(SystemData.running) {
			try {
				if (!SystemData.isGameOnLaunchScreen) {
					if (DataManager.savable.time < 2399) {
						DataManager.savable.time++;
					} else {
						DataManager.savable.time = 0;
					}
					int hours = (int) Math.floor(DataManager.savable.time / 100) % 12;
					int minutes = (int) Math.round(Double.valueOf(DataManager.savable.time % 100) * 0.05);
					if (hours == 0) {
						hours = 12;
					}

					if (DataManager.savable.time < 1200) {
						SystemData.timeReadable = "" + hours + ":" + minutes + "0 AM";
					} else {
						SystemData.timeReadable = "" + hours + ":" + minutes + "0 PM";
					}
				}
				Thread.sleep(250);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
