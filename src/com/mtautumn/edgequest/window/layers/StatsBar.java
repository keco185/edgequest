package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.threads.CharacterManager;
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
			int posX = (int) (SettingsData.screenWidth / 2 - 252 * SystemData.uiZoom);
			int posY = (int) (16 * SystemData.uiZoom);
			int width = (int) (56 * SystemData.uiZoom);
			r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(DataManager.savable.leftEquipt().getItemID()).getItemImg(SystemData.animationClock)), posX, posY, width, width);
		}
		if (DataManager.savable.rightEquipt().getItemCount() > 0) {
			int posX = (int) (SettingsData.screenWidth / 2 + 196 * SystemData.uiZoom);
			int posY = (int) (16 * SystemData.uiZoom);
			int width = (int) (56 * SystemData.uiZoom);
			r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(DataManager.savable.rightEquipt().getItemID()).getItemImg(SystemData.animationClock)), posX, posY, width, width);
		}
	}
	
	private static void drawHealth(Renderer r) {
		double health = Double.valueOf(CharacterManager.characterEntity.health) / Double.valueOf(CharacterManager.characterEntity.maxHealth);
		if (lastHealth == -1 || Math.abs(lastHealth - health) < 0.01) lastHealth = health;
		else if (lastHealth < health) lastHealth += 0.01;
		else if (lastHealth > health) lastHealth -= 0.01;
			
		if (lastHealth > 1) {
			lastHealth = 1.0;
		}
		if (lastHealth < 0) {
			lastHealth = 0;
		}
		int xPos = (SettingsData.screenWidth - (int)(BAR_WIDTH * SystemData.uiZoom))/2 + (int)(76 * SystemData.uiZoom);
		int yPos = (int)(16 * SystemData.uiZoom);
		int width = (int) (384.0 * lastHealth * SystemData.uiZoom);
		int height = (int)(25 * SystemData.uiZoom);
		r.fillRect(xPos, yPos , (int)(384.0 * SystemData.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		if (lastHealth > 0.66667) {
			r.fillRect(xPos, yPos , width, height, 0.0f, 1.0f, 0.0f, 1.0f);
		} else if (lastHealth > 0.33334) {
			r.fillRect(xPos, yPos , width, height, 1.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect(xPos, yPos , width, height, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	private static void drawStamina(Renderer r) {
		double stamina = CharacterManager.characterEntity.stamina / CharacterManager.characterEntity.maxStamina;
		if (stamina > 1) {
			stamina = 1.0;
		}
		if (stamina < 0) {
			stamina = 0;
		}
		int xPos = (SettingsData.screenWidth - (int)(BAR_WIDTH * SystemData.uiZoom))/2 + (int)(140 * SystemData.uiZoom);
		int yPos = (int)(44 * SystemData.uiZoom);
		int width = (int) (256.0 * SystemData.uiZoom * stamina);
		int height = (int)(20 * SystemData.uiZoom);
		r.fillRect(xPos, yPos , (int)(256 * SystemData.uiZoom), height, 0.745f, 0.651f, 0.584f, 1.0f);
		r.fillRect(xPos, yPos , width, height, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	private static void drawBackground(Renderer r) {
		int xPos = (SettingsData.screenWidth - (int)(BAR_WIDTH * SystemData.uiZoom))/2;
		r.drawTexture(r.textureManager.getTexture("HUD"), xPos, 0, (int)(BAR_WIDTH * SystemData.uiZoom), (int)(BAR_HEIGHT * SystemData.uiZoom));
	}
	
}
