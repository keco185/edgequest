package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class MouseItem {
	public static void draw(Renderer r) {
		drawItem(r);
	}
	private static void drawItem(Renderer r) {
		if (DataManager.savable.mouseItem.getItemCount() > 0) {
			int posX = (int) (DataManager.system.mousePosition.getX() - (int)(24 * DataManager.system.uiZoom));
			int posY = (int) (DataManager.system.mousePosition.getY() - (int)(24 * DataManager.system.uiZoom));
			r.drawTexture(r.textureManager.getTexture(DataManager.system.blockIDMap.get(DataManager.savable.mouseItem.getItemID()).getItemImg(DataManager.savable.time)), posX, posY, (int)(48 * DataManager.system.uiZoom), (int)(48 * DataManager.system.uiZoom));
			if (DataManager.savable.mouseItem.getItemCount() > 1) {
				r.backpackFont.drawString(posX, posY, "" + DataManager.savable.mouseItem.getItemCount(), Color.black);
			}
		}
		
	}
	
}
