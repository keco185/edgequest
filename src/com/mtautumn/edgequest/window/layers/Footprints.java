package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.FootPrint;
import com.mtautumn.edgequest.window.Renderer;

public class Footprints {
	public static void draw(Renderer r) {	    
		for (int i = 0; i < DataManager.savable.footPrints.size(); i++) {
			FootPrint fp = DataManager.savable.footPrints.get(i);
			if (fp.level == DataManager.savable.dungeonLevel) {
			if (fp.opacity > 0.4) {
				drawPrint(r, fp, "footsteps");
			} else if (fp.opacity > 0.2) {
				drawPrint(r, fp, "footsteps2");
			} else {
				drawPrint(r, fp, "footsteps3");
			}
			}
		}
	}
	
	private static float xPos(Renderer r, FootPrint fp) {
		return (float)((fp.posX - offsetX(r))*DataManager.settings.blockSize);
	}
	
	private static float yPos(Renderer r, FootPrint fp) {
		return (float)((fp.posY - offsetY(r))*DataManager.settings.blockSize);
	}
	
	private static double offsetX(Renderer r) {
		return DataManager.system.screenX - Double.valueOf(DataManager.settings.screenWidth) / 2.0 / Double.valueOf(DataManager.settings.blockSize);
	}
	
	private static double offsetY(Renderer r) {
		return DataManager.system.screenY - Double.valueOf(DataManager.settings.screenHeight) / 2.0 / Double.valueOf(DataManager.settings.blockSize);
	}
	
	
	private static void drawPrint(Renderer r, FootPrint fp, String name) {
		float posX = xPos(r, fp);
		float posY = yPos(r, fp);
		float width = DataManager.settings.blockSize / 6f;
		float length = DataManager.settings.blockSize / 3f;
		r.drawTexture(r.textureManager.getTexture(name), posX - width , posY - length, length * 2f, length, (float) fp.direction);
	}
}
