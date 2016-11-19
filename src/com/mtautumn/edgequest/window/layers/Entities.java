package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.window.Renderer;

public class Entities {
	public static void draw(Renderer r) {
		Color.white.bind();
		for( int i = 0; i < r.dataManager.savable.entities.size(); i++) {
			Entity entity = r.dataManager.savable.entities.get(i);
			if (entity.dungeonLevel == r.dataManager.savable.dungeonLevel) {
				if (entity.dungeonLevel == - 1 || (entity.dungeon[0] == r.dataManager.savable.dungeonX && entity.dungeon[1] == r.dataManager.savable.dungeonY)) {
					if (entity.getType() != Entity.EntityType.character) {
						drawEntity(entity.getTexture(), entity.frameX, entity.frameY, entity.getRot(), r, entity.health/entity.maxHealth);
					}
				}
			}
		}
		Entity entity = r.dataManager.characterManager.characterEntity;
		drawEntity(entity.getTexture(), entity.frameX, entity.frameY, entity.getRot(), r);
	}
	private static void drawEntity(String texture, double posX, double posY, double rotation, Renderer r, double health) {
		double blockSize = r.dataManager.settings.blockSize;
		double pixelsX = (float) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		double pixelsY = (float) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(r.textureManager.getTexture(texture), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), (float) blockSize, (float) blockSize, (float) rotation);
		drawHealth(r, pixelsX, pixelsY, health);
	}
	private static void drawEntity(String texture, double posX, double posY, double rotation, Renderer r) {
		double blockSize = r.dataManager.settings.blockSize;
		double pixelsX = (float) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		double pixelsY = (float) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(r.textureManager.getTexture(texture), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), (float) blockSize, (float) blockSize, (float) rotation);
	}
	private static void drawHealth(Renderer r, double x, double y, double health) {
		r.drawTexture(r.textureManager.getTexture("selectFar"), 0, 0, 0, 0); //Somehow this fixes lighting bug
		if (health > 0.33334) {
			r.fillRect((float) x - r.dataManager.settings.blockSize / 2f, (float) y - 9f * r.dataManager.settings.blockSize/16f , (float) (r.dataManager.settings.blockSize * (health)), r.dataManager.settings.blockSize/6f, 0.0f, 1.0f, 0.0f, 1.0f);
		} else {
			r.fillRect((float) x - r.dataManager.settings.blockSize / 2f, (float) y - 9f * r.dataManager.settings.blockSize/16f , (float) (r.dataManager.settings.blockSize * (health)), r.dataManager.settings.blockSize/6f, 1.0f, 0.0f, 0.0f, 1.0f);
		}
	r.drawTexture(r.textureManager.getTexture("blockHealthBar"), (float) x - r.dataManager.settings.blockSize / 2f, (float) y - r.dataManager.settings.blockSize , r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
	}
}
