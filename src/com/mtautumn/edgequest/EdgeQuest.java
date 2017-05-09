/* The class with the main method that gets everything going.
 * It sets the OS, creates the dataManager and starts all the threads.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

/*
 * 
 * EdgeQuest class that contains the main method that runs the game
 * 
 */

public class EdgeQuest {
	public static DataManager dataManager = new DataManager();
	public static void main(String[] args) throws InterruptedException {
		// Detect the OS
		byte os = (byte) System.getProperty("os.name").toLowerCase().charAt(0);
		
		switch (os) {
		case 108:
			System.out.println("Setting OS to GNU/Linux");
			SystemData.os = 0;
			break;
		case 109:
			System.out.println("Setting OS to macOS");
			SystemData.os = 1;
			break;
		case 119:
			System.out.println("Setting OS to Windows");
			SystemData.os = 2;
			break;
		default:
			break;
		}
		
		// Start the data manager (the main game loop)
		DataManager.start();
		
		//Waits for the game to load
		while(!SystemData.gameLoaded) {
			Thread.sleep(100);
		}
		//dataManager.system.buttonActionQueue.add("fullScreen"); //Sets the game to full screen
		Thread.sleep(2000);
		SettingsData.targetBlockSize = (float) (64 * SystemData.uiZoom);
		SettingsData.blockSize = (float) (64 * SystemData.uiZoom);
		while(!SystemData.characterLocationSet || SystemData.loadingWorld) {
			Thread.sleep(100);
		}
		Thread.sleep(3000);
		SettingsData.zoomSpeed = 0.001;
	}
}
