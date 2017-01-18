package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.ItemDrop;
import com.mtautumn.edgequest.window.Renderer;

public class ItemDrops {
	public static void draw(Renderer r) {

		for( int i = 0; i < r.dataManager.savable.itemDrops.size(); i++) {
			ItemDrop itemDrop = r.dataManager.savable.itemDrops.get(i);
			if (itemDrop.level == r.dataManager.savable.dungeonLevel) {
				drawDropsSmall(itemDrop.getTexture(r), itemDrop.x, itemDrop.y, itemDrop.getRotation(r.dataManager), r);
			}
		}
	}
	private static void drawDropsSmall(Texture texture, double posX, double posY, float rotation, Renderer r) {
		float blockSize = r.dataManager.settings.blockSize;
		float itemSize = r.dataManager.settings.blockSize*0.75f;
		float pixelsX = (blockSize-itemSize)/2f + (float) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		float pixelsY = (blockSize-itemSize)/2f + (float) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(texture, (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), itemSize, itemSize, rotation);
	}
}
