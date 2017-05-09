package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.projectiles.Projectile;
import com.mtautumn.edgequest.window.Renderer;

public class Projectiles {
	public static void draw(Renderer r) {
		for( int i = 0; i < DataManager.savable.projectiles.size(); i++) {
			Projectile projectile = DataManager.savable.projectiles.get(i);
			if (projectile.level == DataManager.savable.dungeonLevel) {
				drawProjectile(r.textureManager.getTexture("projectiles." + projectile.getTexture()), projectile.x, projectile.y, -projectile.angle, r);
			}
		}
	}
	private static void drawProjectile(Texture texture, double posX, double posY, double rotation, Renderer r) {
		if (texture != null) {
			double blockSize = SettingsData.blockSize;
			double pixelsX = (float) ((posX - (SystemData.screenX - (Double.valueOf(SettingsData.screenWidth)/2.0)/blockSize))*blockSize);
			double pixelsY = (float) ((posY - (SystemData.screenY - (Double.valueOf(SettingsData.screenHeight)/2.0)/blockSize))*blockSize);
			r.drawTexture(texture, (float) (pixelsX - blockSize / 2.0), (float) (pixelsY - blockSize / 2.0), (float) blockSize, (float) blockSize, (float) rotation);
		}
	}
}
