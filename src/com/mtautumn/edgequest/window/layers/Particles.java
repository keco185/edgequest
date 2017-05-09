package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.particles.Particle;
import com.mtautumn.edgequest.window.Renderer;

public class Particles {
	public static void draw(Renderer r) {
		for( int i = 0; i < DataManager.savable.particles.size(); i++) {
			Particle particle = DataManager.savable.particles.get(i);
			if (particle.level == DataManager.savable.dungeonLevel) {
				drawParticle(particle.getTexture(r), particle.x, particle.y, particle.width, particle.height, r);
			}
		}
	}
	private static void drawParticle(Texture texture, double posX, double posY, double width, double height, Renderer r) {
		float particleWidth = (float) (SettingsData.blockSize * width);
		float particleHeight = (float) (SettingsData.blockSize * height);
		float pixelsX = (float) ((posX - (SystemData.screenX - (Double.valueOf(SettingsData.screenWidth)/2.0)/SettingsData.blockSize))*SettingsData.blockSize);
		float pixelsY = (float) ((posY - (SystemData.screenY - (Double.valueOf(SettingsData.screenHeight)/2.0)/SettingsData.blockSize))*SettingsData.blockSize);
		r.drawTexture(texture, (float) (pixelsX - particleWidth / 2.0), (float) (pixelsY - particleHeight / 2.0), particleWidth, particleHeight);
	}
}
