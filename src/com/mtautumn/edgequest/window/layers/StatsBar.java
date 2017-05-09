package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class StatsBar {
	public static final int BAR_WIDTH = 536;
	public static final int BAR_HEIGHT = 88;
	public static double lastHealth = -1;
	public static void draw(Renderer r) {
		drawHealth(r);
		drawStamina(r);
		drawBackground(r);
		if (DataManager.savable.leftEquipt().getItemCount() > 0) {
			int posX = (int) (DataManager.settings.screenWidth / 2 - 252 * DataManager.system.uiZoom);
			int posY = (int) (16 * DataManager.system.uiZoom);
			int width = (int) (56 * DataManager.system.uiZoom);
			r.drawTexture(r.textureManager.getTexture(DataManager.system.blockIDMap.get(DataManager.savable.leftEquipt().getItemID()).getItemImg(DataManager.system.animationClock)), posX, posY, width, width);
		}
		if (DataManager.savable.rightEquipt().getItemCount() > 0) {
			int posX = (int) (DataManager.settings.screenWidth / 2 + 196 * DataManager.system.uiZoom);
			int posY = (int) (16 * DataManager.system.uiZoom);
			int width = (int) (56 * DataManager.system.uiZoom);
			r.drawTexture(r.textureManager.getTexture(DataManager.system.blockIDMap.get(DataManager.savable.rightEquipt().getItemID()).getItemImg(DataManager.system.animationClock)), posX, posY, width, width);
		}
	}
	
	private static void drawHealth(Renderer r) {
		double health = Double.valueOf(DataManager.characterManager.characterEntity.health) / Double.valueOf(DataManager.characterManager.characterEntity.maxHealth);
		if (lastHealth == -1 || Math.abs(lastHealth - health) < 0.01) lastHealth = health;
		else if (lastHealth < health) lastHealth += 0.01;
		else if (lastHealth > health) lastHealth -= 0.01;
			
		if (lastHealth > 1) {
			lastHealth = 1.0;
		}
		if (lastHealth < 0) {
			lastHealth = 0;
		}
		int xPos = (DataManager.settings.screenWidth - (int)(BAR_WIDTH * DataManager.system.uiZoom))/2 + (int)(76 * DataManager.system.uiZoom);
		int yPos = (int)(16 * DataManager.system.uiZoom);
		int width = (int) (384.0 * lastHealth * DataManager.system.uiZoom);
		int height = (int)(25 * DataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(384.0 * DataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		if (lastHealth > 0.66667) {
			r.fillRect(xPos, yPos , width, height, 0.0f, 1.0f, 0.0f, 1.0f);
		} else if (lastHealth > 0.33334) {
			r.fillRect(xPos, yPos , width, height, 1.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect(xPos, yPos , width, height, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	private static void drawStamina(Renderer r) {
		double stamina = DataManager.characterManager.characterEntity.stamina / DataManager.characterManager.characterEntity.maxStamina;
		if (stamina > 1) {
			stamina = 1.0;
		}
		if (stamina < 0) {
			stamina = 0;
		}
		int xPos = (DataManager.settings.screenWidth - (int)(BAR_WIDTH * DataManager.system.uiZoom))/2 + (int)(140 * DataManager.system.uiZoom);
		int yPos = (int)(44 * DataManager.system.uiZoom);
		int width = (int) (256.0 * DataManager.system.uiZoom * stamina);
		int height = (int)(20 * DataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(256 * DataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		r.fillRect(xPos, yPos , width, height, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	private static void drawBackground(Renderer r) {
		int xPos = (DataManager.settings.screenWidth - (int)(BAR_WIDTH * DataManager.system.uiZoom))/2;
		r.drawTexture(r.textureManager.getTexture("HUD"), xPos, 0, (int)(BAR_WIDTH * DataManager.system.uiZoom), (int)(BAR_HEIGHT * DataManager.system.uiZoom));
	}
	
}
