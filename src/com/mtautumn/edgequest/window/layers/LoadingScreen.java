package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class LoadingScreen {
	public static void draw(Renderer r) {
			drawLoading(r, "Loading...");
	}
	private static void drawLoading(Renderer r, String text) {
		if (DataManager.settings.screenWidth > 1.6 * DataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(DataManager.settings.screenHeight - DataManager.settings.screenWidth / 1.6) / 2, DataManager.settings.screenWidth,(int)(DataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(DataManager.settings.screenWidth - DataManager.settings.screenHeight * 1.6)/2, 0, (int)(DataManager.settings.screenHeight * 1.6),DataManager.settings.screenHeight);

		}
		int textX = (DataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (DataManager.settings.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
}
