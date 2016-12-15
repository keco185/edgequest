package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class CharacterEffects {
	public static void draw(Renderer r) {
		drawWaterSplash(r);
	}

	private static void drawWaterSplash(Renderer r) {
		float blockSize =  r.dataManager.settings.blockSize;
		if (r.dataManager.characterManager.characterEntity.getRelativeGroundBlock(0, 0).isLiquid && r.dataManager.characterManager.characterEntity.getRelativeStructureBlockID(0, 0) == 0) {
			double posX = r.dataManager.characterManager.characterEntity.frameX;
			double posY = r.dataManager.characterManager.characterEntity.frameY;
			double pixelsX = (float) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
			double pixelsY = (float) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
			r.drawTexture(r.textureManager.getAnimatedTexture("waterSplash", r.dataManager), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), blockSize, blockSize);
		}
	}
}
