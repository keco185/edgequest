package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.DamagePost;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class DamagePosts {
	public static void draw(Renderer r) {
		for( int i = 0; i < DataManager.savable.damagePosts.size(); i++) {
			DamagePost post = DataManager.savable.damagePosts.get(i);
			if (post.level == DataManager.savable.dungeonLevel) {
					drawPost(post.getX(), post.getY(), post.damage, r);
				}
		}
	}
	private static void drawPost(double x, double y, int damage, Renderer r) {
		double blockSize = DataManager.settings.blockSize;
		float pixelsX = (float) ((x - (DataManager.system.screenX - (Double.valueOf(DataManager.settings.screenWidth)/2.0)/blockSize))*blockSize);
		float pixelsY = (float) ((y - (DataManager.system.screenY - (Double.valueOf(DataManager.settings.screenHeight)/2.0)/blockSize))*blockSize);
		float stringWidth = r.damageFont.getWidth(Integer.toString(damage));
		float stringHeight = r.damageFont.getHeight(Integer.toString(damage));
		float imageSize = (stringWidth > stringHeight) ? stringWidth : stringHeight;
		imageSize *= 1.25f;
		r.drawTexture(r.textureManager.getTexture("bloodSpatter"), pixelsX - (imageSize - stringWidth) / 2f, pixelsY, imageSize, imageSize);
		r.damageFont.drawString(pixelsX, pixelsY, Integer.toString(damage), Color.white);
	}
}
