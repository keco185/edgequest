package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class StatsBar {
	public static final int BAR_WIDTH = 700;
	public static final int BAR_HEIGHT = 50;
	public static void draw(Renderer r) {
		Color.white.bind();
		drawHealth(r);
		drawStamina(r);
		drawBackground(r);
	}
	
	private static void drawHealth(Renderer r) {
		double health = Double.valueOf(r.dataManager.characterManager.characterEntity.health) / Double.valueOf(r.dataManager.characterManager.characterEntity.maxHealth);
		if (health > 1) health = 1.0;
		int xPos = (r.dataManager.settings.screenWidth - BAR_WIDTH)/2 + 26;
		int yPos = 8;
		int width = (int) (647.0 * health);
		int height = 14;
		r.fillRect(xPos, yPos , 647, height, 0.745f, 0.651f, 0.584f, 1.0f);
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
		int xPos = (r.dataManager.settings.screenWidth - BAR_WIDTH)/2 + 69;
		int yPos = 33;
		int width = (int) (562.0 * stamina);
		int height = 14;
		r.fillRect(xPos, yPos , 562, height, 0.745f, 0.651f, 0.584f, 1.0f);
		r.fillRect(xPos, yPos , width, height, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	private static void drawBackground(Renderer r) {
		int xPos = (r.dataManager.settings.screenWidth - BAR_WIDTH)/2;
		r.drawTexture(r.textureManager.getTexture("statsBar"), xPos, 0, BAR_WIDTH, BAR_HEIGHT);
	}
	
}
