package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.MenuButtonManager;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuButton;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuPane;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuToggleButton;

public class Menu {
	
	public static void draw(Renderer r) {
		
		r.fillRect(0, 0, SettingsData.screenWidth, SettingsData.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		SystemData.menuX = SettingsData.screenWidth / 2 - (int)(375 * SystemData.uiZoom);
		SystemData.menuY = SettingsData.screenHeight / 2 - (int)(250 * SystemData.uiZoom);
		r.drawTexture(r.textureManager.getTexture("menuBackground"), SystemData.menuX, SystemData.menuY, (int)(750 * SystemData.uiZoom),(int)(500 * SystemData.uiZoom));
		drawButtons(r);
	}

	private static void drawButtons(Renderer r) {
		MenuPane menu = MenuButtonManager.getCurrentMenu();
		for (int i = 0; i < menu.getCount(); i++) {
			MenuButton button = menu.getButtons().get(i);
			if (button.visible) {
				if (button.getClass() == MenuToggleButton.class) {
					if (((MenuToggleButton) button).state) {
						r.drawTexture(r.textureManager.getTexture("menuToggleOn"), button.getPosX(SystemData.menuX), button.getPosY(SystemData.menuY), button.getWidth(), button.getHeight());
					} else {
						r.drawTexture(r.textureManager.getTexture("menuToggleOff"), button.getPosX(SystemData.menuX), button.getPosY(SystemData.menuY), button.getWidth(), button.getHeight());
					}
				} else {
					r.drawTexture(r.textureManager.getTexture("menuButton"), button.getPosX(SystemData.menuX), button.getPosY(SystemData.menuY), button.getWidth(), button.getHeight());
				}
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(button.getPosX(SystemData.menuX) + (button.getWidth() - width) / 2, button.getPosY(SystemData.menuY) + (button.getHeight() * 0.85f - height) / 2, button.displayName);
			}
			if (menu.parent == null) {
				r.drawTexture(r.textureManager.getTexture("exit"), SystemData.menuX + (int)(SettingsData.BACK_BUTTON_PADDING_LEFT * SystemData.uiZoom), SystemData.menuY + (int)(SettingsData.BACK_BUTTON_PADDING_TOP * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom));
			} else {
				r.drawTexture(r.textureManager.getTexture("back"), SystemData.menuX + (int)(SettingsData.BACK_BUTTON_PADDING_LEFT * SystemData.uiZoom), SystemData.menuY + (int)(SettingsData.BACK_BUTTON_PADDING_TOP * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom), (int)(SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom));
			}
		}
	}
}
