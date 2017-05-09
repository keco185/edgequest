package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class StatsBar {
	public static final int BAR_WIDTH = 536;
	public static final int BAR_HEIGHT = 88;
	public static double lastHealth = -1;
	public static void draw(Renderer r) {
		drawHealth(r);
		drawStamina(r);
		drawBackground(r);
		if (r.dataManager.savable.leftEquipt().getItemCount() > 0) {
			int posX = (int) (r.dataManager.settings.screenWidth / 2 - 252 * r.dataManager.system.uiZoom);
			int posY = (int) (16 * r.dataManager.system.uiZoom);
			int width = (int) (56 * r.dataManager.system.uiZoom);
			r.drawTexture(r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.leftEquipt().getItemID()).getItemImg(r.dataManager.system.animationClock)), posX, posY, width, width);
		}
		if (r.dataManager.savable.rightEquipt().getItemCount() > 0) {
			int posX = (int) (r.dataManager.settings.screenWidth / 2 + 196 * r.dataManager.system.uiZoom);
			int posY = (int) (16 * r.dataManager.system.uiZoom);
			int width = (int) (56 * r.dataManager.system.uiZoom);
			r.drawTexture(r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.rightEquipt().getItemID()).getItemImg(r.dataManager.system.animationClock)), posX, posY, width, width);
		}
	}
	
	private static void drawHealth(Renderer r) {
		double health = Double.valueOf(r.dataManager.characterManager.characterEntity.health) / Double.valueOf(r.dataManager.characterManager.characterEntity.maxHealth);
		if (lastHealth == -1 || Math.abs(lastHealth - health) < 0.01) lastHealth = health;
		else if (lastHealth < health) lastHealth += 0.01;
		else if (lastHealth > health) lastHealth -= 0.01;
			
		if (lastHealth > 1) {
			lastHealth = 1.0;
		}
		if (lastHealth < 0) {
			lastHealth = 0;
		}
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2 + (int)(76 * r.dataManager.system.uiZoom);
		int yPos = (int)(16 * r.dataManager.system.uiZoom);
		int width = (int) (384.0 * lastHealth * r.dataManager.system.uiZoom);
		int height = (int)(25 * r.dataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(384.0 * r.dataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		if (lastHealth > 0.66667) {
			r.fillRect(xPos, yPos , width, height, 0.0f, 1.0f, 0.0f, 1.0f);
		} else if (lastHealth > 0.33334) {
			r.fillRect(xPos, yPos , width, height, 1.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect(xPos, yPos , width, height, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	private static void drawStamina(Renderer r) {
		double stamina = r.dataManager.characterManager.characterEntity.stamina / r.dataManager.characterManager.characterEntity.maxStamina;
		if (stamina > 1) {
			stamina = 1.0;
		}
		if (stamina < 0) {
			stamina = 0;
		}
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2 + (int)(140 * r.dataManager.system.uiZoom);
		int yPos = (int)(44 * r.dataManager.system.uiZoom);
		int width = (int) (256.0 * r.dataManager.system.uiZoom * stamina);
		int height = (int)(20 * r.dataManager.system.uiZoom);
		r.fillRect(xPos, yPos , (int)(256 * r.dataManager.system.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		r.fillRect(xPos, yPos , width, height, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	private static void drawBackground(Renderer r) {
		int xPos = (r.dataManager.settings.screenWidth - (int)(BAR_WIDTH * r.dataManager.system.uiZoom))/2;
		r.drawTexture(r.textureManager.getTexture("HUD"), xPos, 0, (int)(BAR_WIDTH * r.dataManager.system.uiZoom), (int)(BAR_HEIGHT * r.dataManager.system.uiZoom));
	}
	
}
