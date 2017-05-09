package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class BlockDamage {
	public static void draw(Renderer r) {		
		float posX = getMousePosX(r);
		float posY = getMousePosY(r);
		float blockSize = DataManager.settings.blockSize;
		
		if (!DataManager.system.isMouseFar) {
			drawBlockHealth(r, posX, posY, blockSize);
		}		
	}
	
	
	private static float getMousePosX(Renderer r) {
		double coordsOffsetX = offsetX(r);
		return (float)((DataManager.system.mouseX - coordsOffsetX)*DataManager.settings.blockSize);
	}
	private static float getMousePosY(Renderer r) {
		double coordsOffsetY = offsetY(r);
		return (float)((DataManager.system.mouseY - coordsOffsetY)*DataManager.settings.blockSize);
	}
	
	
	private static double offsetX(Renderer r) {
		return DataManager.system.screenX - Double.valueOf(DataManager.settings.screenWidth) / Double.valueOf(2 * DataManager.settings.blockSize);
	}
	private static double offsetY(Renderer r) {
		return DataManager.system.screenY - Double.valueOf(DataManager.settings.screenHeight) / Double.valueOf(2 * DataManager.settings.blockSize);
	}
	
	private static void drawBlockHealth(Renderer r, float posX, float posY, float blockSize) {
		if (DataManager.system.blockDamage != 0 ) {
			r.drawTexture(r.textureManager.getTexture("blockHealthBar"), posX, posY, blockSize, blockSize);
			r.drawTexture(r.textureManager.getTexture("blockHealth"), posX, posY, (float) (blockSize * (DataManager.system.blockDamage / 10.0)), blockSize);
		}
	}
}
