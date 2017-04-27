package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class BlockDamage {
	public static void draw(Renderer r) {		
		float posX = getMousePosX(r);
		float posY = getMousePosY(r);
		float blockSize = r.dataManager.settings.blockSize;
		
		if (!r.dataManager.system.isMouseFar) {
			drawBlockHealth(r, posX, posY, blockSize);
		}		
	}
	
	
	private static float getMousePosX(Renderer r) {
		double coordsOffsetX = offsetX(r);
		return (float)((r.dataManager.system.mouseX - coordsOffsetX)*r.dataManager.settings.blockSize);
	}
	private static float getMousePosY(Renderer r) {
		double coordsOffsetY = offsetY(r);
		return (float)((r.dataManager.system.mouseY - coordsOffsetY)*r.dataManager.settings.blockSize);
	}
	
	
	private static double offsetX(Renderer r) {
		return r.dataManager.system.screenX - Double.valueOf(r.dataManager.settings.screenWidth) / Double.valueOf(2 * r.dataManager.settings.blockSize);
	}
	private static double offsetY(Renderer r) {
		return r.dataManager.system.screenY - Double.valueOf(r.dataManager.settings.screenHeight) / Double.valueOf(2 * r.dataManager.settings.blockSize);
	}
	
	private static void drawBlockHealth(Renderer r, float posX, float posY, float blockSize) {
		if (r.dataManager.system.blockDamage != 0 ) {
			r.drawTexture(r.textureManager.getTexture("blockHealthBar"), posX, posY, blockSize, blockSize);
			r.drawTexture(r.textureManager.getTexture("blockHealth"), posX, posY, (float) (blockSize * (r.dataManager.system.blockDamage / 10.0)), blockSize);
		}
	}
}
