package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.LaunchScreenManager.LaunchScreenPane;
import com.mtautumn.edgequest.window.LaunchScreenManager.MenuButton;
import com.mtautumn.edgequest.window.LaunchScreenManager.MenuToggleButton;

public class LaunchScreen {
	public static void draw(Renderer r) {
		drawBackground(r);
		drawLogo(r);
		drawButtons(r);
	}

	private static void drawBackground(Renderer r) {
		if (SettingsData.screenWidth > 1.6 * SettingsData.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(SettingsData.screenHeight - SettingsData.screenWidth / 1.6) / 2, SettingsData.screenWidth,(int)(SettingsData.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(SettingsData.screenWidth - SettingsData.screenHeight * 1.6)/2, 0, (int)(SettingsData.screenHeight * 1.6),SettingsData.screenHeight);

		}
	}

	private static void drawLogo(Renderer r) {
		r.drawTexture(r.textureManager.getTexture("launchScreenLogo"), (SettingsData.screenWidth / 2 - 300 * (float)SystemData.uiZoom), 40* (float)SystemData.uiZoom, 600* (float)SystemData.uiZoom, 166* (float)SystemData.uiZoom);
	}

	private static void drawButtons(Renderer r) {
		float yOffset = 206 * (float)SystemData.uiZoom;
		LaunchScreenPane pane = r.launchScreenManager.getCurrentMenu();
		for (int i = 0; i<pane.getCount(); i++) {
			MenuButton button = pane.getButtons().get(i);
			if (button.visible) {
				if (button.getClass() == MenuToggleButton.class) {
					if (((MenuToggleButton) button).state) {
						r.drawTexture(r.textureManager.getTexture("menuToggleOn"), SettingsData.screenWidth / 2.0f + button.getPosX(), yOffset + (SettingsData.screenHeight - yOffset) / 2.0f + button.getPosY(), button.getWidth(), button.getHeight());
					} else {
						r.drawTexture(r.textureManager.getTexture("menuToggleOff"), SettingsData.screenWidth / 2.0f + button.getPosX(), yOffset + (SettingsData.screenHeight - yOffset) / 2.0f + button.getPosY(), button.getWidth(), button.getHeight());
					}
				} else {
					r.drawTexture(r.textureManager.getTexture("menuButton"), SettingsData.screenWidth / 2.0f + button.getPosX(), yOffset + (SettingsData.screenHeight - yOffset) / 2.0f + button.getPosY(), button.getWidth(), button.getHeight());
				}
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(SettingsData.screenWidth / 2.0f + button.getPosX() + (button.getWidth() - width) / 2, yOffset + (SettingsData.screenHeight - yOffset) / 2.0f + button.getPosY() + (button.getHeight() * 0.85f - height) / 2, button.displayName);
			}
		}
		if (pane.parent != null) {
			r.drawTexture(r.textureManager.getTexture("back"), (int)(SettingsData.BACK_BUTTON_PADDING_LEFT * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_PADDING_TOP * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom));
		}
	}
}
