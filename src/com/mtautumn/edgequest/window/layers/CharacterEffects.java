package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class CharacterEffects {
	public static void draw(Renderer r) {
		drawWaterSplash(r);
	}

	private static void drawWaterSplash(Renderer r) {
		float blockSize =  DataManager.settings.blockSize;
		if (DataManager.characterManager.characterEntity.getRelativeGroundBlock(0, 0).isLiquid && DataManager.characterManager.characterEntity.getRelativeStructureBlockID(0, 0) == 0) {
			double posX = DataManager.characterManager.characterEntity.frameX;
			double posY = DataManager.characterManager.characterEntity.frameY;
			double pixelsX = (float) ((posX - (DataManager.system.screenX - (Double.valueOf(DataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
			double pixelsY = (float) ((posY - (DataManager.system.screenY - (Double.valueOf(DataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
			r.drawTexture(r.textureManager.getAnimatedTexture("waterSplash"), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), blockSize, blockSize);
		}
	}
}
