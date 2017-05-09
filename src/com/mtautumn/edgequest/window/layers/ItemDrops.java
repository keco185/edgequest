package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.window.Renderer;

public class ItemDrops {
	public static void draw(Renderer r) {

		for( int i = 0; i < DataManager.savable.itemDrops.size(); i++) {
			ItemDrop itemDrop = DataManager.savable.itemDrops.get(i);
			if (itemDrop.level == DataManager.savable.dungeonLevel) {
				drawDropsSmall(itemDrop.getTexture(r), itemDrop.x, itemDrop.y, itemDrop.getRotation(), r);
			}
		}
	}
	private static void drawDropsSmall(Texture texture, double posX, double posY, float rotation, Renderer r) {
		float blockSize = DataManager.settings.blockSize;
		float itemSize = DataManager.settings.blockSize*0.75f;
		float pixelsX = (blockSize-itemSize)/2f + (float) ((posX - (DataManager.system.screenX - (Double.valueOf(DataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		float pixelsY = (blockSize-itemSize)/2f + (float) ((posY - (DataManager.system.screenY - (Double.valueOf(DataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		r.drawTexture(texture, (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), itemSize, itemSize, rotation);
	}
}
