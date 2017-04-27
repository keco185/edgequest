package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class MouseSelection {
	public static void draw(Renderer r) {
		if (!r.dataManager.system.isAiming) {
			float posX = getMousePosX(r);
			float posY = getMousePosY(r);
			float blockSize = r.dataManager.settings.blockSize;

			if (r.dataManager.system.isMouseFar) {
				drawFarSelection(r, posX, posY, blockSize);
			} else {
				drawNearSelection(r, posX, posY, blockSize);
			}

			if (r.dataManager.system.isKeyboardTravel) {
				drawFlag(r, posX, posY, blockSize);
			}
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

	private static void drawFarSelection(Renderer r, float posX, float posY, float blockSize) {
		r.drawTexture(r.textureManager.getTexture("selectFar"), posX, posY, blockSize, blockSize);

	}
	private static void drawNearSelection(Renderer r, float posX, float posY, float blockSize) {
		r.drawTexture(r.textureManager.getTexture("select"), posX, posY, blockSize, blockSize);

	}
	private static void drawFlag(Renderer r, float posX, float posY, float blockSize) {
		r.drawTexture(r.textureManager.getTexture("selectFlag"), posX, posY - (0.4375f * blockSize), blockSize * 1.25f, blockSize*1.4375f);
	}

}
