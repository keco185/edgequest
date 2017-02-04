package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuButton;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuPane;

public class Menu {
	public static void draw(Renderer r) {
		
		r.fillRect(0, 0, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		r.dataManager.system.menuX = r.dataManager.settings.screenWidth / 2 - (int)(375 * r.dataManager.system.uiZoom);
		r.dataManager.system.menuY = r.dataManager.settings.screenHeight / 2 - (int)(250 * r.dataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("menuBackground"), r.dataManager.system.menuX, r.dataManager.system.menuY, (int)(750 * r.dataManager.system.uiZoom),(int)(500 * r.dataManager.system.uiZoom));
		drawButtons(r);
	}

	private static void drawButtons(Renderer r) {
		MenuPane menu = r.dataManager.menuButtonManager.getCurrentMenu();
		for (int i = 0; i < menu.getCount(); i++) {
			MenuButton button = menu.getButtons().get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, button.getPosX(r.dataManager.system.menuX), button.getPosY(r.dataManager.system.menuY), button.getWidth(), button.getHeight());
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(button.getPosX(r.dataManager.system.menuX) + (button.getWidth() - width) / 2, button.getPosY(r.dataManager.system.menuY) + (button.getHeight() - height) / 2, button.displayName);
			}
			if (menu.parent == null) {
				r.drawTexture(r.textureManager.getTexture("exit"), r.dataManager.system.menuX + (int)(r.dataManager.settings.BACK_BUTTON_PADDING * r.dataManager.system.uiZoom), r.dataManager.system.menuY + (int)(r.dataManager.settings.BACK_BUTTON_PADDING * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom));
			} else {
				r.drawTexture(r.textureManager.getTexture("back"), r.dataManager.system.menuX + (int)(r.dataManager.settings.BACK_BUTTON_PADDING * r.dataManager.system.uiZoom), r.dataManager.system.menuY + (int)(r.dataManager.settings.BACK_BUTTON_PADDING * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom), (int)(r.dataManager.settings.BACK_BUTTON_SIZE * r.dataManager.system.uiZoom));
			}
		}
	}
}
