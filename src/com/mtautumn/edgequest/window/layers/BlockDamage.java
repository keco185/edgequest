package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class BlockDamage {
	public static void draw(Renderer r) {		
		float posX = getMousePosX(r);
		float posY = getMousePosY(r);
		float blockSize = SettingsData.blockSize;
		
		if (!SystemData.isMouseFar) {
			drawBlockHealth(r, posX, posY, blockSize);
		}		
	}
	
	
	private static float getMousePosX(Renderer r) {
		double coordsOffsetX = offsetX(r);
		return (float)((SystemData.mouseX - coordsOffsetX)*SettingsData.blockSize);
	}
	private static float getMousePosY(Renderer r) {
		double coordsOffsetY = offsetY(r);
		return (float)((SystemData.mouseY - coordsOffsetY)*SettingsData.blockSize);
	}
	
	
	private static double offsetX(Renderer r) {
		return SystemData.screenX - Double.valueOf(SettingsData.screenWidth) / Double.valueOf(2 * SettingsData.blockSize);
	}
	private static double offsetY(Renderer r) {
		return SystemData.screenY - Double.valueOf(SettingsData.screenHeight) / Double.valueOf(2 * SettingsData.blockSize);
	}
	
	private static void drawBlockHealth(Renderer r, float posX, float posY, float blockSize) {
		if (SystemData.blockDamage != 0 ) {
			r.drawTexture(r.textureManager.getTexture("blockHealthBar"), posX, posY, blockSize, blockSize);
			r.drawTexture(r.textureManager.getTexture("blockHealth"), posX, posY, (float) (blockSize * (SystemData.blockDamage / 10.0)), blockSize);
		}
	}
}
