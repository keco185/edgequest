package com.mtautumn.edgequest.window.layers;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.updates.UpdateRayCast.Triangle;
import com.mtautumn.edgequest.window.Renderer;

public class Lighting extends Thread {
	Renderer r;
	public Lighting(Renderer r) {
		this.r = r;
	}
	public void run() {
		updatePlayerLight(r);
	}
	public static void completionTasks(Renderer r) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f (1.0f,1.0f,1.0f,1.0f);
		if (r.dataManager.world.getBrightness() < 1 || r.dataManager.savable.dryness < -0.2) {
			r.lightingFBO.enableBuffer();
			GL20.glUseProgram(r.lightingShader.getProgramId());
			float offsetX = (float) (r.dataManager.settings.screenWidth / 2.0 - r.dataManager.system.screenX * r.dataManager.settings.blockSize);
			float offsetY = (float) (r.dataManager.settings.screenHeight / 2.0 - r.dataManager.system.screenY * r.dataManager.settings.blockSize);
			for (LightSource light : r.dataManager.savable.lightSources) {
				if (light.level == r.dataManager.savable.dungeonLevel) {
					drawLightingTriangles(light.triangles, light.range, offsetX, offsetY, r);
				}
			}
			GL20.glUseProgram(0);
			r.lightingFBO.disableBuffer();
			
			r.lightingColorFBO.enableBuffer();
			GL20.glUseProgram(r.lightingColorShader.getProgramId());
			int location = GL20.glGetUniformLocation(r.lightingColorShader.getProgramId(), "color");
			for (LightSource light : r.dataManager.savable.lightSources) {
				if (light.level == r.dataManager.savable.dungeonLevel) {
					GL20.glUniform3f(location, light.r, light.g, light.b);
					drawColorTriangles(light.triangles, light.range, offsetX, offsetY, r);
				}
			}
			GL20.glUseProgram(0);
			r.lightingColorFBO.disableBuffer();
			
		}
	}
	private static void drawLightingTriangles(ArrayList<Triangle> triangles, double radius, float offsetX, float offsetY, Renderer r) {
		for (Triangle triangle : triangles) {
			float brightness1 = 1;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius);
			float brightness3 = (float) (1.0 - triangle.radius2 / radius);
			r.fillLightingTriangle((float) triangle.x1 * r.dataManager.settings.blockSize + offsetX, (float) triangle.y1 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x2 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y2 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x3 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y3 * r.dataManager.settings.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}
	private static void drawColorTriangles(ArrayList<Triangle> triangles, double radius, float offsetX, float offsetY, Renderer r) {
		for (Triangle triangle : triangles) {
			float brightness1 = 1;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius);
			float brightness3 = (float) (1.0 - triangle.radius2 / radius);
			if (brightness2 < 0) brightness2 = 0;
			if (brightness3 < 0) brightness3 = 0;
			r.fillLightingColorTriangle((float) triangle.x1 * r.dataManager.settings.blockSize + offsetX, (float) triangle.y1 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x2 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y2 * r.dataManager.settings.blockSize + offsetY,(float) triangle.x3 * r.dataManager.settings.blockSize + offsetX,(float) triangle.y3 * r.dataManager.settings.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}
	private static void updatePlayerLight(Renderer r) {
		r.dataManager.characterManager.characterEntity.light.posX = r.dataManager.characterManager.characterEntity.frameX;
		r.dataManager.characterManager.characterEntity.light.posY = r.dataManager.characterManager.characterEntity.frameY;
		r.dataManager.characterManager.characterEntity.light.level = r.dataManager.characterManager.characterEntity.dungeonLevel;
		r.dataManager.blockUpdateManager.lighting.urc.update(r.dataManager.characterManager.characterEntity.light);
	}
}
