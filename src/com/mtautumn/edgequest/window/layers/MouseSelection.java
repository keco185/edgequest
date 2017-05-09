package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class MouseSelection {
	public static void draw(Renderer r) {
		if (!DataManager.system.isAiming) {
			float posX = getMousePosX(r);
			float posY = getMousePosY(r);
			float blockSize = DataManager.settings.blockSize;

			if (DataManager.system.isMouseFar) {
				drawFarSelection(r, posX, posY, blockSize);
			} else {
				drawNearSelection(r, posX, posY, blockSize);
			}

			if (DataManager.system.isKeyboardTravel) {
				drawFlag(r, posX, posY, blockSize);
			}
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
