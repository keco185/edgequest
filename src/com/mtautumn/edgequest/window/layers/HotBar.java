package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.window.Renderer;

public class HotBar {
	public static final int HOTBAR_WIDTH = 125;
	public static final int HOTBAR_HEIGHT = 403;
	public static final int START_X = 20;
	public static final int START_Y = 66;
	public static final int DELTA_X = 53;
	public static final double DELTA_Y = 53.5;
	static int xPos;
	static int yPos;
	public static void draw(Renderer r) {
		drawBackground(r);
		drawSpaces(r);	
		drawSelection(r);
	}
	
	private static void drawBackground(Renderer r) {
		yPos = (r.dataManager.settings.screenHeight - (int)(HOTBAR_HEIGHT * r.dataManager.system.uiZoom)) / 2;
		xPos = r.dataManager.settings.screenWidth - (int)(HOTBAR_WIDTH * r.dataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("hotBar"), xPos, yPos, (int) (HOTBAR_WIDTH * r.dataManager.system.uiZoom), (int) (HOTBAR_HEIGHT * r.dataManager.system.uiZoom));
	}
	private static void drawSelection(Renderer r) {
		int posX = xPos + (int)((START_X - 5) * r.dataManager.system.uiZoom);
		int posY = yPos + (int)(((r.dataManager.savable.hotBarSelection * DELTA_Y) + START_Y - 6) * r.dataManager.system.uiZoom);
		r.drawTexture(r.textureManager.getTexture("hotBarSelect"), posX, posY, (int)(101 * r.dataManager.system.uiZoom), (int)(47 * r.dataManager.system.uiZoom));

	}
	
	private static void drawSpaces(Renderer r) {
		int itemSize = (int) (38 * r.dataManager.system.uiZoom);
		for (int i = 0; i < 2; i++) {
			int posX = xPos + (int)((i * DELTA_X + START_X) * r.dataManager.system.uiZoom);
			for (int j = 0; j < 6; j++) {
				int posY = yPos + (int)(((j * DELTA_Y) + START_Y) * r.dataManager.system.uiZoom);
				try {
					r.drawTexture(r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(r.dataManager.savable.backpackItems[i][j].getItemID()).getItemImg(r.dataManager.system.animationClock)), posX, posY, itemSize, itemSize);
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
