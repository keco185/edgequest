package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.window.Renderer;

public class Entities {
	public static void draw(Renderer r) {
		for( int i = 0; i < DataManager.savable.entities.size(); i++) {
			Entity entity = DataManager.savable.entities.get(i);
			if (entity.dungeonLevel == DataManager.savable.dungeonLevel) {
					if (entity != DataManager.characterManager.characterEntity) {
						drawEntity(entity.getTexture(), entity.frameX, entity.frameY, entity.getRot(), r, Double.valueOf(entity.health)/Double.valueOf(entity.maxHealth));
					}
			}
		}
		Entity entity = DataManager.characterManager.characterEntity;
		drawEntity(entity.getTexture(), entity.frameX, entity.frameY, entity.getRot(), r);
	}
	private static void drawEntity(String texture, double posX, double posY, double rotation, Renderer r, double health) {
		double blockSize = SettingsData.blockSize;
		double pixelsX = (float) ((posX - (SystemData.screenX - (Double.valueOf(SettingsData.screenWidth)/2.0)/blockSize))*blockSize);
		double pixelsY = (float) ((posY - (SystemData.screenY - (Double.valueOf(SettingsData.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(r.textureManager.getTexture("entities." + texture), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), (float) blockSize, (float) blockSize, (float) rotation);
		drawHealth(r, pixelsX, pixelsY, health);
	}
	private static void drawEntity(String texture, double posX, double posY, double rotation, Renderer r) {
		double blockSize = SettingsData.blockSize;
		double pixelsX = (float) ((posX - (SystemData.screenX - (Double.valueOf(SettingsData.screenWidth)/2.0)/blockSize))*blockSize);
		double pixelsY = (float) ((posY - (SystemData.screenY - (Double.valueOf(SettingsData.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(r.textureManager.getTexture("entities." + texture), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), (float) blockSize, (float) blockSize, (float) rotation);
	}
	private static void drawHealth(Renderer r, double x, double y, double health) {
		if (health > 0.33334) {
			r.fillRect((float) x - SettingsData.blockSize / 2f, (float) y - 9f * SettingsData.blockSize/16f , (float) (SettingsData.blockSize * (health)), SettingsData.blockSize/6f, 0.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect((float) x - SettingsData.blockSize / 2f, (float) y - 9f * SettingsData.blockSize/16f , (float) (SettingsData.blockSize * (health)), SettingsData.blockSize/6f, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	r.drawTexture(r.textureManager.getTexture("blockHealthBar"), (float) x - SettingsData.blockSize / 2f, (float) y - SettingsData.blockSize , SettingsData.blockSize, SettingsData.blockSize);
	}
}
