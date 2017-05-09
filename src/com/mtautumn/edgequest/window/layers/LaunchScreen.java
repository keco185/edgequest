package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.LaunchScreenManager.LaunchScreenPane;
import com.mtautumn.edgequest.window.LaunchScreenManager.MenuButton;

public class LaunchScreen {
	public static void draw(Renderer r) {
		drawBackground(r);
		drawLogo(r);
		drawButtons(r);
	}

	private static void drawBackground(Renderer r) {
		if (r.dataManager.settings.screenWidth > 1.6 * r.dataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(r.dataManager.settings.screenHeight - r.dataManager.settings.screenWidth / 1.6) / 2, r.dataManager.settings.screenWidth,(int)(r.dataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(r.dataManager.settings.screenWidth - r.dataManager.settings.screenHeight * 1.6)/2, 0, (int)(r.dataManager.settings.screenHeight * 1.6),r.dataManager.settings.screenHeight);

		}
	}

	private static void drawLogo(Renderer r) {
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (r.dataManager.settings.screenWidth / 2 - 300 * (float)r.dataManager.system.uiZoom), 40* (float)r.dataManager.system.uiZoom, 600* (float)r.dataManager.system.uiZoom, 166* (float)r.dataManager.system.uiZoom);
	}

	private static void drawButtons(Renderer r) {
		float yOffset = 206 * (float)r.dataManager.system.uiZoom;
		LaunchScreenPane pane = r.launchScreenManager.getCurrentMenu();
		for (int i = 0; i<pane.getCount(); i++) {
			MenuButton button = pane.getButtons().get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, r.dataManager.settings.screenWidth / 2.0f + button.getPosX(), yOffset + (r.dataManager.settings.screenHeight - yOffset) / 2.0f + button.getPosY(), button.getWidth(), button.getHeight());
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(r.dataManager.settings.screenWidth / 2.0f + button.getPosX() + (button.getWidth() - width) / 2, yOffset + (r.dataManager.settings.screenHeight - yOffset) / 2.0f + button.getPosY() + (button.getHeight() * 0.85f - height) / 2, button.displayName);
			}
		}
		if (pane.parent != null) {
			r.drawTexture(r.textureManager.getTexture("back"), (int)(r.dataManager.settings.BACK_BUTTON_PADDING_LEFT * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_PADDING_TOP * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom));
		}
	}
}
