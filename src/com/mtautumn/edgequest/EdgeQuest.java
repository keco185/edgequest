/* The class with the main method that gets everything going.
 * It sets the OS, creates the dataManager and starts all the threads.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

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
			DataManager.system.os = 0;
			break;
		case 109:
			System.out.println("Setting OS to macOS");
			DataManager.system.os = 1;
			break;
		case 119:
			System.out.println("Setting OS to Windows");
			DataManager.system.os = 2;
			break;
		default:
			break;
		}
		
		// Start the data manager (the main game loop)
		DataManager.start();
		
		//Waits for the game to load
		while(!DataManager.system.gameLoaded) {
			Thread.sleep(100);
		}
		//dataManager.system.buttonActionQueue.add("fullScreen"); //Sets the game to full screen
		Thread.sleep(2000);
		DataManager.settings.targetBlockSize = (float) (64 * DataManager.system.uiZoom);
		DataManager.settings.blockSize = (float) (64 * DataManager.system.uiZoom);
		while(!DataManager.system.characterLocationSet || DataManager.system.loadingWorld) {
			Thread.sleep(100);
		}
		Thread.sleep(3000);
		DataManager.settings.zoomSpeed = 0.001;
	}
}
