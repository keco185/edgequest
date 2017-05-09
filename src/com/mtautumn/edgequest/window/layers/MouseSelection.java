package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class MouseSelection {
	public static void draw(Renderer r) {
		if (!SystemData.isAiming) {
			float posX = getMousePosX(r);
			float posY = getMousePosY(r);
			float blockSize = SettingsData.blockSize;

			if (SystemData.isMouseFar) {
				drawFarSelection(r, posX, posY, blockSize);
			} else {
				drawNearSelection(r, posX, posY, blockSize);
			}

			if (SystemData.isKeyboardTravel) {
				drawFlag(r, posX, posY, blockSize);
			}
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
