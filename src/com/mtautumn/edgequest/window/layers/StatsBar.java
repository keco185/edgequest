package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class StatsBar {
	public static final int BAR_WIDTH = 700;
	public static final int BAR_HEIGHT = 50;
	public static void draw(Renderer r) {
		drawHealth(r);
		drawStamina(r);
		drawBackground(r);
	}
	
	private static void drawHealth(Renderer r) {
		double health = Double.valueOf(r.dataManager.characterManager.characterEntity.health) / Double.valueOf(r.dataManager.characterManager.characterEntity.maxHealth);
		if (health > 1) health = 1.0;
		if (health < 0) health = 0;
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2 + (int)(26 * r.dataManager.system.uiZoom);
		int yPos = (int)(8 * r.dataManager.system.uiZoom);
		int width = (int) (647.0 * health * r.dataManager.system.uiZoom);
		int height = (int)(14 * r.dataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(647 * r.dataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		if (health > 0.66667) {
			r.fillRect(xPos, yPos , width, height, 0.0f, 1.0f, 0.0f, 1.0f);
		} else if (health > 0.33334) {
			r.fillRect(xPos, yPos , width, height, 1.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect(xPos, yPos , width, height, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	private static void drawStamina(Renderer r) {
		double stamina = r.dataManager.characterManager.characterEntity.stamina / r.dataManager.characterManager.characterEntity.maxStamina;
		if (stamina > 1) stamina = 1.0;
		if (stamina < 0) stamina = 0;
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2 + (int)(69 * r.dataManager.system.uiZoom);
		int yPos = (int)(33 * r.dataManager.system.uiZoom);
		int width = (int) (562.0 * r.dataManager.system.uiZoom * stamina);
		int height = (int)(14 * r.dataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(562 * r.dataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		r.fillRect(xPos, yPos , width, height, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	private static void drawBackground(Renderer r) {
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2;
		r.drawTexture(r.textureManager.getTexture("statsBar"), xPos, 0, (int)(BAR_WIDTH * r.dataManager.system.uiZoom), (int)(BAR_HEIGHT * r.dataManager.system.uiZoom));
	}
	
}
