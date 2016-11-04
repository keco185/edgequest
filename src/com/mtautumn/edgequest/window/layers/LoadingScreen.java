package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class LoadingScreen {
	public static void draw(Renderer r) {
			drawLoading(r, "Loading...");
	}
	private static void drawLoading(Renderer r, String text) {
		if (r.dataManager.settings.screenWidth > 1.6 * r.dataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.dataManager.settings.screenHeight - r.dataManager.settings.screenWidth / 1.6) / 2, r.dataManager.settings.screenWidth,(int)(r.dataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.dataManager.settings.screenWidth - r.dataManager.settings.screenHeight * 1.6)/2, 0, (int)(r.dataManager.settings.screenHeight * 1.6),r.dataManager.settings.screenHeight);

		}
		int textX = (r.dataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (r.dataManager.settings.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
}
