package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.window.layers.Lighting;

public class UpdatePlayerLighting extends Thread {
	public void run() {
		Lighting.updatePlayerLight();
	}
}
