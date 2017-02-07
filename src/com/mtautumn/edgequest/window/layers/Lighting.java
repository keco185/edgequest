package com.mtautumn.edgequest.window.layers;
import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.updates.UpdateRayCast.Triangle;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.renderUtils.LightingVBO;

public class Lighting extends Thread {
	Renderer r;
	public Lighting(Renderer r) {
		this.r = r;
	}
	public void run() {
		draw(r);
	}
	public static void completionTasks(Renderer r) {
		if (r.dataManager.world.getBrightness() < 1 || r.dataManager.savable.dryness < -0.2) {
			/*GL20.glUseProgram( r.lightingShader.getProgramId() );
			int colorLocation = GL20.glGetUniformLocation(r.lightingShader.getProgramId(),"color");
			if (r.dataManager.savable.dungeonLevel > -1) {
				GL20.glUniform3f(colorLocation, 0.02f,0.0f,0.05f);
			} else {
				GL20.glUniform3f(colorLocation,0.01f,0.0f,0.15f);
			}
			r.lightingVBODarkness.write(r);
			GL20.glUniform3f(colorLocation,1.0f,0.6f,0.05f);
			r.lightingVBOBrightness.write(r);
			GL20.glUseProgram(0);*/
			int opacLocation = GL20.glGetUniformLocation(r.raycastShader.getProgramId(),"opacity");
			r.lightingFBO.enableBuffer();
			for (LightSource light : r.dataManager.savable.lightSources) {
				drawTriangles(light.triangles, light.range, r);
			}
			r.lightingFBO.disableBuffer();
			GL20.glUseProgram(r.raycastShader.getProgramId());
			GL20.glUniform1f(opacLocation, 1.0f - (float) r.dataManager.world.getBrightness(r.dataManager.characterManager.characterEntity));
			r.lightingFBO.drawBuffer(0, 0, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight);
			GL20.glUseProgram(0);
		}
	}
	public static void draw(Renderer r) {
		/*if (r.dataManager.world.getBrightness() < 1) {
		//if (false) {
			r.lightingVBODarkness = new LightingVBO();
			r.lightingVBOBrightness = new LightingVBO();

			float xPos = xStartPos(r);
			float yStart = yStartPos(r);

			for(int x = r.dataManager.system.minTileX; x < r.dataManager.system.maxTileX; x++) {

				float yPos = yStart;
				for (int y = r.dataManager.system.minTileY; y < r.dataManager.system.maxTileY; y++) {
					//drawBrightness(r, x, y, xPos, yPos);
					yPos += r.dataManager.settings.blockSize;

				}
				xPos += r.dataManager.settings.blockSize;

			}
			r.lightingVBOBrightness.preWrite();
			r.lightingVBODarkness.preWrite();
		}*/
	}
	private static void drawTriangles(ArrayList<Triangle> triangles, double radius, Renderer r) {
		for (Triangle triangle : triangles) {
			float offsetX = (float) (r.dataManager.settings.screenWidth / 2.0 - r.dataManager.system.screenX * r.dataManager.settings.blockSize);
			float offsetY = (float) (r.dataManager.settings.screenHeight / 2.0 - r.dataManager.system.screenY * r.dataManager.settings.blockSize);
			float brightness1 = 1;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius);
			float brightness3 = (float) (1.0 - triangle.radius2 / radius);
			if (brightness2 < 0) brightness2 = 0;
			if (brightness3 < 0) brightness3 = 0;
			r.fillTriangle((float) triangle.x1 * r.dataManager.settings.blockSize + offsetX, (float) triangle.y1 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x2 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y2 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x3 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y3 * r.dataManager.settings.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}

	/*private static int xStartPos(Renderer r) {
		return (int) ((r.dataManager.system.minTileX - r.dataManager.system.screenX) * r.dataManager.settings.blockSize + r.dataManager.settings.screenWidth/2.0);
	}

	private static int yStartPos(Renderer r) {
		return (int)((r.dataManager.system.minTileY - r.dataManager.system.screenY) * r.dataManager.settings.blockSize + r.dataManager.settings.screenHeight/2.0);
	}

	private static double getBrightness(Renderer r, int x, int y) {
		return Double.valueOf((r.dataManager.world.getLight(x, y, r.dataManager.savable.dungeonLevel) + 128)) / 255.0;
	}

	private static void drawBrightness(Renderer r, int x, int y, float xPos, float yPos) {
		double brightness1 = getBrightness(r, x, y);//10%
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
			r.lightingVBODarkness.addOpacity((float) (1.0 - nightBrightness1),(float) (1.0 - nightBrightness2),(float) (1.0 - nightBrightness3), (float) (1.0 - nightBrightness4));
			r.lightingVBODarkness.addQuad(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
		} else {
			r.lightingVBODarkness.addOpacity((float) (1.0 - nightBrightness1),(float) (1.0 - nightBrightness2),(float) (1.0 - nightBrightness3), (float) (1.0 - nightBrightness4));
			r.lightingVBODarkness.addQuad(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
		}		
		float blockBrightness1 = (float)(0.2 * (nightBrightness1 - worldBrightness));
		float blockBrightness2 = (float)(0.2 * (nightBrightness2 - worldBrightness));
		float blockBrightness3 = (float)(0.2 * (nightBrightness3 - worldBrightness));
		float blockBrightness4 = (float)(0.2 * (nightBrightness4 - worldBrightness));
		r.lightingVBOBrightness.addOpacity(blockBrightness1, blockBrightness2, blockBrightness3, blockBrightness4);
		r.lightingVBOBrightness.addQuad(xPos, yPos, r.dataManager.settings.blockSize, r.dataManager.settings.blockSize);
	}*/
}
