package com.mtautumn.edgequest.window.layers;
import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.window.Renderer;

public class Lighting {
	public static void draw(Renderer r) {	
		GL20.glUseProgram( r.shader.getProgramId() );
		float xPos = xStartPos(r);
		for(int x = r.dataManager.system.minTileX; x <= r.dataManager.system.maxTileX; x++) {
			
			float yPos = yStartPos(r);
			for (int y = r.dataManager.system.minTileY; y <= r.dataManager.system.maxTileY; y++) {
				
				if (r.dataManager.world.getBrightness() < 1) {
					drawBrightness(r, x, y, xPos, yPos);
				}
				yPos += r.dataManager.settings.blockSize;
				
			}
			xPos += r.dataManager.settings.blockSize;
			
		}
		GL20.glUseProgram(0);
	}
	
	private static int xStartPos(Renderer r) {
		return (int) ((r.dataManager.system.minTileX - r.dataManager.system.screenX) * r.dataManager.settings.blockSize + r.dataManager.settings.screenWidth/2.0);
	}
	
	private static int yStartPos(Renderer r) {
		return (int)((r.dataManager.system.minTileY - r.dataManager.system.screenY) * r.dataManager.settings.blockSize + r.dataManager.settings.screenHeight/2.0);
	}
	
	private static double getBrightness(Renderer r, int x, int y) {
			return Double.valueOf((r.dataManager.world.getLight(x, y, r.dataManager.savable.dungeonLevel) + 128)) / 255.0;
	}
	
	private static void drawBrightness(Renderer r, int x, int y, float xPos, float yPos) {
		double brightness1 = getBrightness(r, x, y);
		double brightness2 = getBrightness(r, x + 1, y);
		double brightness3 = getBrightness(r, x + 1, y + 1);
		double brightness4 = getBrightness(r, x, y + 1);
		double worldBrightness = r.dataManager.world.getBrightness();
		double invWorldBrightness = 1 - worldBrightness;
		double nightBrightness1 = (invWorldBrightness) * brightness1 + worldBrightness;
		double nightBrightness2 = (invWorldBrightness) * brightness2 + worldBrightness;
		double nightBrightness3 = (invWorldBrightness) * brightness3 + worldBrightness;
		double nightBrightness4 = (invWorldBrightness) * brightness4 + worldBrightness;

		if (r.dataManager.savable.dungeonLevel > -1) {
			int location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color");
			GL20.glUniform3f(location, 0.02f,0.0f,0.05f);
			location = GL20.glGetUniformLocation(r.shader.getProgramId(),"opacity");
			GL20.glUniform4f(location, (float) (1.0 - nightBrightness1),(float) (1.0 - nightBrightness2),(float) (1.0 - nightBrightness3), (float) (1.0 - nightBrightness4));
			r.fillRect(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
		} else {
			//int location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color1u");
			//GL20.glUniform4f(location,0.01f,0.0f,0.15f, (float) (1.0 - nightBrightness1));
			//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color2u");
			//GL20.glUniform4f(location,0.01f,0.0f,0.15f, (float) (1.0 - nightBrightness2));
			//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color3u");
			//GL20.glUniform4f(location,0.01f,0.0f,0.15f, (float) (1.0 - nightBrightness3));
			//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color4u");
			//GL20.glUniform4f(location,0.01f,0.0f,0.15f, (float) (1.0 - nightBrightness4));
			int location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color");
			GL20.glUniform3f(location,0.01f,0.0f,0.15f);
			location = GL20.glGetUniformLocation(r.shader.getProgramId(),"opacity");
			GL20.glUniform4f(location, (float) (1.0 - nightBrightness1),(float) (1.0 - nightBrightness2),(float) (1.0 - nightBrightness3), (float) (1.0 - nightBrightness4));
			r.fillRect(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
		}
		float blockBrightness1 = (float)(0.2 * (nightBrightness1 - worldBrightness));
		float blockBrightness2 = (float)(0.2 * (nightBrightness2 - worldBrightness));
		float blockBrightness3 = (float)(0.2 * (nightBrightness3 - worldBrightness));
		float blockBrightness4 = (float)(0.2 * (nightBrightness4 - worldBrightness));
		//GL20.glUniform4f(location,1.0f,0.6f,0.05f, (blockBrightness1));
		//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color2u");
		//GL20.glUniform4f(location,1.0f,0.6f,0.05f, (blockBrightness2));
		//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color3u");
		//GL20.glUniform4f(location,1.0f,0.6f,0.05f, (blockBrightness3));
		//location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color4u");
		//GL20.glUniform4f(location,1.0f,0.6f,0.05f, (blockBrightness4));
		int location = GL20.glGetUniformLocation(r.shader.getProgramId(),"color");
		GL20.glUniform3f(location,1.0f,0.6f,0.05f);
		location = GL20.glGetUniformLocation(r.shader.getProgramId(),"opacity");
		GL20.glUniform4f(location, blockBrightness1, blockBrightness2, blockBrightness3, blockBrightness4);
		r.fillRect(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
	}
}
