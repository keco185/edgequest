package com.mtautumn.edgequest.window.layers;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import java.util.ArrayList;
import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.dataObjects.Triangle;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.window.Renderer;
public class Lighting extends Thread {
	Renderer r;
	public Lighting(Renderer r) {
		this.r = r;
	}
	@Override
	public void run() {
		if (DataManager.world.getBrightness() < 1 && !DataManager.world.noLighting) {
			//updatePlayerLight();
		}
	}
	public static void completionTasks(Renderer r) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f (1.0f,1.0f,1.0f,1.0f);
		if (DataManager.world.getBrightness() < 1 && !DataManager.world.noLighting) {
			r.lightingFBO.enableBuffer();
			GL20.glUseProgram(r.lightingShader.getProgramId());
			float offsetX = (float) (SettingsData.screenWidth / 2.0 - SystemData.screenX * SettingsData.blockSize);
			float offsetY = (float) (SettingsData.screenHeight / 2.0 - SystemData.screenY * SettingsData.blockSize);
			for (LightSource light : DataManager.savable.lightSources) {
				if (light.level == DataManager.savable.dungeonLevel) {
					drawLightingTriangles(light.triangles, light.range, offsetX, offsetY, light.brightness, r);
				}
			}
			GL20.glUseProgram(0);
			r.lightingFBO.disableBuffer();
			
			r.lightingColorFBO.enableBuffer();
			GL20.glUseProgram(r.lightingColorShader.getProgramId());
			int location = GL20.glGetUniformLocation(r.lightingColorShader.getProgramId(), "color");
			for (LightSource light : DataManager.savable.lightSources) {
				if (light.level == DataManager.savable.dungeonLevel) {
					GL20.glUniform3f(location, light.r, light.g, light.b);
					drawColorTriangles(light.triangles, light.range, offsetX, offsetY, light.brightness, r);
				}
			}
			GL20.glUseProgram(0);
			r.lightingColorFBO.disableBuffer();
			
		}
	}
	private static void drawLightingTriangles(ArrayList<Triangle> triangles, double radius, float offsetX, float offsetY, float brightness, Renderer r) {
		for (Triangle triangle : triangles) {
			float brightness1 = brightness;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius) * brightness;
			float brightness3 = (float) (1.0 - triangle.radius2 / radius) * brightness;
			r.fillLightingTriangle((float) triangle.x1 * SettingsData.blockSize + offsetX, (float) triangle.y1 * SettingsData.blockSize + offsetY,(float) triangle.x2 * SettingsData.blockSize + offsetX,(float) triangle.y2 * SettingsData.blockSize + offsetY,(float) triangle.x3 * SettingsData.blockSize + offsetX,(float) triangle.y3 * SettingsData.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}
	private static void drawColorTriangles(ArrayList<Triangle> triangles, double radius, float offsetX, float offsetY, float brightness, Renderer r) {
		for (Triangle triangle : triangles) {
			float brightness1 = brightness;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius) * brightness;
			float brightness3 = (float) (1.0 - triangle.radius2 / radius) * brightness;
			if (brightness2 < 0) {
				brightness2 = 0;
			}
			if (brightness3 < 0) {
				brightness3 = 0;
			}
			r.fillLightingColorTriangle((float) triangle.x1 * SettingsData.blockSize + offsetX, (float) triangle.y1 * SettingsData.blockSize + offsetY,(float) triangle.x2 * SettingsData.blockSize + offsetX,(float) triangle.y2 * SettingsData.blockSize + offsetY,(float) triangle.x3 * SettingsData.blockSize + offsetX,(float) triangle.y3 * SettingsData.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}
	public static void updatePlayerLight() {
		CharacterManager.characterEntity.light.posX = CharacterManager.characterEntity.frameX;
		CharacterManager.characterEntity.light.posY = CharacterManager.characterEntity.frameY;
		CharacterManager.characterEntity.light.level = CharacterManager.characterEntity.dungeonLevel;
		DataManager.blockUpdateManager.lighting.urc.update(CharacterManager.characterEntity.light);
	}
}