package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.particles.Particle;
import com.mtautumn.edgequest.window.Renderer;

public class PrecipitationParticles {
	public static void draw(Renderer r) {
		for( int i = 0; i < r.dataManager.savable.precipitationParticles.size(); i++) {
			Particle particle = r.dataManager.savable.precipitationParticles.get(i);
			if (particle.level == r.dataManager.savable.dungeonLevel) {
				drawParticle(particle.getTexture(r), particle.x, particle.y, particle.width, particle.height, r);
			}
		}
	}
	private static void drawParticle(Texture texture, double posX, double posY, double width, double height, Renderer r) {
		float particleWidth = (float) (r.dataManager.settings.blockSize * width);
		float particleHeight = (float) (r.dataManager.settings.blockSize * height);
		float pixelsX = (float) ((posX - (r.dataManager.system.screenX - (Double.valueOf(r.dataManager.settings.screenWidth)/2.0)/r.dataManager.settings.blockSize))*r.dataManager.settings.blockSize);
		float pixelsY = (float) ((posY - (r.dataManager.system.screenY - (Double.valueOf(r.dataManager.settings.screenHeight)/2.0)/r.dataManager.settings.blockSize))*r.dataManager.settings.blockSize);
		r.drawTexture(texture, (float) (pixelsX - particleWidth / 2.0), (float) (pixelsY - particleHeight / 2.0), particleWidth, particleHeight);
	}
}
