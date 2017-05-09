package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.LaunchScreenManager.LaunchScreenPane;
import com.mtautumn.edgequest.window.LaunchScreenManager.MenuButton;

public class LaunchScreen {
	public static void draw(Renderer r) {
		drawBackground(r);
		drawLogo(r);
		drawButtons(r);
	}

	private static void drawBackground(Renderer r) {
		if (DataManager.settings.screenWidth > 1.6 * DataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(DataManager.settings.screenHeight - DataManager.settings.screenWidth / 1.6) / 2, DataManager.settings.screenWidth,(int)(DataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(DataManager.settings.screenWidth - DataManager.settings.screenHeight * 1.6)/2, 0, (int)(DataManager.settings.screenHeight * 1.6),DataManager.settings.screenHeight);

		}
	}

	private static void drawLogo(Renderer r) {
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (DataManager.settings.screenWidth / 2 - 300 * (float)DataManager.system.uiZoom), 40* (float)DataManager.system.uiZoom, 600* (float)DataManager.system.uiZoom, 166* (float)DataManager.system.uiZoom);
	}

	private static void drawButtons(Renderer r) {
		float yOffset = 206 * (float)DataManager.system.uiZoom;
		LaunchScreenPane pane = r.launchScreenManager.getCurrentMenu();
		for (int i = 0; i<pane.getCount(); i++) {
			MenuButton button = pane.getButtons().get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, DataManager.settings.screenWidth / 2.0f + button.getPosX(), yOffset + (DataManager.settings.screenHeight - yOffset) / 2.0f + button.getPosY(), button.getWidth(), button.getHeight());
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(DataManager.settings.screenWidth / 2.0f + button.getPosX() + (button.getWidth() - width) / 2, yOffset + (DataManager.settings.screenHeight - yOffset) / 2.0f + button.getPosY() + (button.getHeight() * 0.85f - height) / 2, button.displayName);
			}
		}
		if (pane.parent != null) {
			r.drawTexture(r.textureManager.getTexture("back"), (int)(DataManager.settings.BACK_BUTTON_PADDING_LEFT * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_PADDING_TOP * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom));
		}
	}
}
