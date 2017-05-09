package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.window.Renderer;

public class LoadingScreen {
	public static void draw(Renderer r) {
			drawLoading(r, "Loading...");
	}
	private static void drawLoading(Renderer r, String text) {
		if (SettingsData.screenWidth > 1.6 * SettingsData.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(SettingsData.screenHeight - SettingsData.screenWidth / 1.6) / 2, SettingsData.screenWidth,(int)(SettingsData.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(SettingsData.screenWidth - SettingsData.screenHeight * 1.6)/2, 0, (int)(SettingsData.screenHeight * 1.6),SettingsData.screenHeight);

		}
		int textX = (SettingsData.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (SettingsData.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
}
