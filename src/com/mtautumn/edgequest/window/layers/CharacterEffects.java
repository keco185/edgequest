package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.window.Renderer;

public class CharacterEffects {
	public static void draw(Renderer r) {
		drawWaterSplash(r);
	}

	private static void drawWaterSplash(Renderer r) {
		float blockSize =  SettingsData.blockSize;
		if (CharacterManager.characterEntity.getRelativeGroundBlock(0, 0).isLiquid && CharacterManager.characterEntity.getRelativeStructureBlockID(0, 0) == 0) {
			double posX = CharacterManager.characterEntity.frameX;
			double posY = CharacterManager.characterEntity.frameY;
			double pixelsX = (float) ((posX - (SystemData.screenX - (Double.valueOf(SettingsData.screenWidth)/2.0)/blockSize))*blockSize);
			double pixelsY = (float) ((posY - (SystemData.screenY - (Double.valueOf(SettingsData.screenHeight)/2.0)/blockSize))*blockSize);
			r.drawTexture(r.textureManager.getAnimatedTexture("waterSplash"), (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), blockSize, blockSize);
		}
	}
}
