package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuButton;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuPane;

public class Menu {
	
	public static void draw(Renderer r) {
		
		r.fillRect(0, 0, DataManager.settings.screenWidth, DataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		DataManager.system.menuX = DataManager.settings.screenWidth / 2 - (int)(375 * DataManager.system.uiZoom);
		DataManager.system.menuY = DataManager.settings.screenHeight / 2 - (int)(250 * DataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("menuBackground"), DataManager.system.menuX, DataManager.system.menuY, (int)(750 * DataManager.system.uiZoom),(int)(500 * DataManager.system.uiZoom));
		drawButtons(r);
	}

	private static void drawButtons(Renderer r) {
		MenuPane menu = DataManager.menuButtonManager.getCurrentMenu();
		for (int i = 0; i < menu.getCount(); i++) {
			MenuButton button = menu.getButtons().get(i);
			if (button.visible) {
				r.drawTexture(button.buttonImage, button.getPosX(DataManager.system.menuX), button.getPosY(DataManager.system.menuY), button.getWidth(), button.getHeight());
				int height = r.buttonFont.getHeight(button.displayName);
				int width = r.buttonFont.getWidth(button.displayName);
				r.buttonFont.drawString(button.getPosX(DataManager.system.menuX) + (button.getWidth() - width) / 2, button.getPosY(DataManager.system.menuY) + (button.getHeight() * 0.85f - height) / 2, button.displayName);
			}
			if (menu.parent == null) {
				r.drawTexture(r.textureManager.getTexture("exit"), DataManager.system.menuX + (int)(DataManager.settings.BACK_BUTTON_PADDING_LEFT * DataManager.system.uiZoom), DataManager.system.menuY + (int)(DataManager.settings.BACK_BUTTON_PADDING_TOP * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom));
			} else {
				r.drawTexture(r.textureManager.getTexture("back"), DataManager.system.menuX + (int)(DataManager.settings.BACK_BUTTON_PADDING_LEFT * DataManager.system.uiZoom), DataManager.system.menuY + (int)(DataManager.settings.BACK_BUTTON_PADDING_TOP * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom), (int)(DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom));
			}
		}
	}
}
