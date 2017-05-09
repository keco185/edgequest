package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class Backpack {
	
	public static void draw(Renderer r) {
		r.fillRect(0, 0, SettingsData.screenWidth, SettingsData.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		SystemData.menuX = SettingsData.screenWidth / 2 - (int) (375 * SystemData.uiZoom);
		SystemData.menuY = SettingsData.screenHeight/2 - (int) (250 * SystemData.uiZoom);
		r.drawTexture(r.textureManager.getTexture("backpack"), SystemData.menuX, SystemData.menuY, (int)(750 * SystemData.uiZoom),(int)(500 * SystemData.uiZoom));
	}
	
	private static void drawSpaces(Renderer r) {
		int spaceXMult = (int)(64 * SystemData.uiZoom);
		int spaceYMult = (int)(65 * SystemData.uiZoom);
		int spaceXAdd = (int)(37 * SystemData.uiZoom);
		int spaceYAdd = (int)(94 * SystemData.uiZoom);
		for (int i = 0; i < DataManager.savable.backpackItems.length - 1; i++) {
			int posX = SystemData.menuX + (i) * spaceXMult + spaceXAdd;
			for (int j = 0; j < DataManager.savable.backpackItems[i].length; j++) {
				int posY = SystemData.menuY + j * spaceYMult + spaceYAdd;
				try {
					if (DataManager.savable.backpackItems[i][j].getItemCount() > 0) {
						r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(DataManager.savable.backpackItems[i][j].getItemID()).getItemImg(SystemData.animationClock)), posX, posY, (int)(48 * SystemData.uiZoom), (int)(48 * SystemData.uiZoom));
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
