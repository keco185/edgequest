package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class Backpack {
	
	public static void draw(Renderer r) {
		r.fillRect(0, 0, DataManager.settings.screenWidth, DataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		DataManager.system.menuX = DataManager.settings.screenWidth / 2 - (int) (375 * DataManager.system.uiZoom);
		DataManager.system.menuY = DataManager.settings.screenHeight/2 - (int) (250 * DataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("backpack"), DataManager.system.menuX, DataManager.system.menuY, (int)(750 * DataManager.system.uiZoom),(int)(500 * DataManager.system.uiZoom));
	}
	
	private static void drawSpaces(Renderer r) {
		int spaceXMult = (int)(64 * DataManager.system.uiZoom);
		int spaceYMult = (int)(65 * DataManager.system.uiZoom);
		int spaceXAdd = (int)(37 * DataManager.system.uiZoom);
		int spaceYAdd = (int)(94 * DataManager.system.uiZoom);
		for (int i = 0; i < DataManager.savable.backpackItems.length - 1; i++) {
			int posX = DataManager.system.menuX + (i) * spaceXMult + spaceXAdd;
			for (int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				int posY = DataManager.system.menuY + j * spaceYMult + spaceYAdd;
				try {
					if (DataManager.savable.backpackItems[i][j].getItemCount() > 0) {
						r.drawTexture(r.textureManager.getTexture(DataManager.system.blockIDMap.get(DataManager.savable.backpackItems[i][j].getItemID()).getItemImg(DataManager.system.animationClock)), posX, posY, (int)(48 * DataManager.system.uiZoom), (int)(48 * DataManager.system.uiZoom));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (DataManager.savable.backpackItems[i][j].getItemCount() > 1) {
					r.backpackFont.drawString(posX, posY, "" + DataManager.savable.backpackItems[i][j].getItemCount(), Color.black);
				}
			}
		}
	}
	
}
