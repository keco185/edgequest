package com.mtautumn.edgequest.window.layers;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import java.util.List;

import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.dataObjects.Triangle;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.WorldUtils;
import com.mtautumn.edgequest.window.Renderer;
public class Lighting extends Thread {
	Renderer r;
	public Lighting(Renderer r) {
		this.r = r;
	}
	@Override
	public void run() {
	}
	public static void completionTasks(Renderer r) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glColor4f (1.0f,1.0f,1.0f,1.0f);
		if (WorldUtils.getBrightness() < 1 && !WorldUtils.noLighting) {
			r.lightingFBO.enableBuffer();
			GL20.glUseProgram(r.lightingShader.getProgramId());
			float offsetX = (float) (SettingsData.screenWidth / 2.0 - SystemData.screenX * SettingsData.blockSize);
			float offsetY = (float) (SettingsData.screenHeight / 2.0 - SystemData.screenY * SettingsData.blockSize);
			LightSource playerLight = CharacterManager.characterEntity.light;
			playerLight.inUse = true;
			float lightOffsetX = (float) (CharacterManager.characterEntity.frameX - playerLight.renderedX);
			float lightOffsetY = (float) (CharacterManager.characterEntity.frameY - playerLight.renderedY);
			for (LightSource light : DataManager.savable.lightSources) {
				if (light.level == DataManager.savable.dungeonLevel) {
					if (light != CharacterManager.characterEntity.light) {
						drawLightingTriangles(light.triangles, light.range, offsetX, offsetY, light.brightness, r);
					}
				}
			}
			drawLightingTriangles(playerLight.triangles, playerLight.range, offsetX + lightOffsetX * SettingsData.blockSize, offsetY + lightOffsetY * SettingsData.blockSize, playerLight.brightness, r);
			GL20.glUseProgram(0);
			r.lightingFBO.disableBuffer();

			r.lightingColorFBO.enableBuffer();
			GL20.glUseProgram(r.lightingColorShader.getProgramId());
			int location = GL20.glGetUniformLocation(r.lightingColorShader.getProgramId(), "color");
			for (LightSource light : DataManager.savable.lightSources) {
				if (light.level == DataManager.savable.dungeonLevel) {
					if (light != CharacterManager.characterEntity.light) {
						GL20.glUniform3f(location, light.r, light.g, light.b);
						drawColorTriangles(light.triangles, light.range, offsetX, offsetY, light.brightness, r);
					}
				}
			}
			GL20.glUniform3f(location, playerLight.r, playerLight.g, playerLight.b);
			drawColorTriangles(playerLight.triangles, playerLight.range, offsetX + lightOffsetX * SettingsData.blockSize, offsetY + lightOffsetY * SettingsData.blockSize, playerLight.brightness, r);
			playerLight.inUse = false;
			GL20.glUseProgram(0);
			r.lightingColorFBO.disableBuffer();

		}
	}
	private static void drawLightingTriangles(List<Triangle> triangles, double radius, float offsetX, float offsetY, float brightness, Renderer r) {
		for (Triangle triangle : triangles) {
			float brightness1 = brightness;
			float brightness2 = (float) (1.0 - triangle.radius1 / radius) * brightness;
			float brightness3 = (float) (1.0 - triangle.radius2 / radius) * brightness;
			r.fillLightingTriangle((float) triangle.x1 * SettingsData.blockSize + offsetX, (float) triangle.y1 * SettingsData.blockSize + offsetY,(float) triangle.x2 * SettingsData.blockSize + offsetX,(float) triangle.y2 * SettingsData.blockSize + offsetY,(float) triangle.x3 * SettingsData.blockSize + offsetX,(float) triangle.y3 * SettingsData.blockSize + offsetY, brightness1, brightness2, brightness3);
		}
	}
	private static void drawColorTriangles(List<Triangle> triangles, double radius, float offsetX, float offsetY, float brightness, Renderer r) {
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
		CharacterManager.characterEntity.light.renderedX = CharacterManager.characterEntity.light.posX;
		CharacterManager.characterEntity.light.renderedY = CharacterManager.characterEntity.light.posY;
	}
}