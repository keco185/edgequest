package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class Backpack {
	public static void draw(Renderer r) {
		r.fillRect(0, 0, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight, 0.2f,0.2f,0.2f, 0.7f);
		drawBackground(r);
		drawSpaces(r);	
	}
	
	private static void drawBackground(Renderer r) {
		r.dataManager.system.menuX = r.dataManager.settings.screenWidth / 2 - (int) (375 * r.dataManager.system.uiZoom);
		r.dataManager.system.menuY = r.dataManager.settings.screenHeight/2 - (int) (250 * r.dataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("backpack"), r.dataManager.system.menuX, r.dataManager.system.menuY, (int)(750 * r.dataManager.system.uiZoom),(int)(500 * r.dataManager.system.uiZoom));
	}
	
	private static void drawSpaces(Renderer r) {
		int spaceXMult = (int)(64 * r.dataManager.system.uiZoom);
		int spaceYMult = (int)(65 * r.dataManager.system.uiZoom);
		int spaceXAdd = (int)(37 * r.dataManager.system.uiZoom);
		int spaceYAdd = (int)(94 * r.dataManager.system.uiZoom);
		for (int i = 2; i < r.dataManager.savable.backpackItems.length; i++) {
			int posX = r.dataManager.system.menuX + (i - 2) * spaceXMult + spaceXAdd;
			for (int j = 0; j < r.dataManager.savable.backpackItems[i].length; j++) {
				int posY = r.dataManager.system.menuY + j * spaceYMult + spaceYAdd;
				try {
					if (r.dataManager.savable.backpackItems[i][j].getItemCount() > 0) {
						r.drawTexture(r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.dataManager.system.animationClock)), posX, posY, (int)(48 * r.dataManager.system.uiZoom), (int)(48 * r.dataManager.system.uiZoom));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (r.dataManager.savable.backpackItems[i][j].getItemCount() > 1) {
					r.backpackFont.drawString(posX, posY, "" + r.dataManager.savable.backpackItems[i][j].getItemCount(), Color.black);
				}
			}
		}
	}
	
}
