package com.mtautumn.edgequest.window.layers;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class Layers {
	public static void draw(Renderer r) throws InterruptedException {
		if (SystemData.isGameOnLaunchScreen) {
			LaunchScreen.draw(r);
		} else {
			r.preLightingFBO.enableBuffer();
			Terrain terrainThread = new Terrain(r);
			terrainThread.start();
			Lighting lightingThread = new Lighting(r);
			lightingThread.start();
			terrainThread.join();
			terrainThread.completionTasks(r);
			Footprints.draw(r);
			Particles.draw(r);
			ItemDrops.draw(r);
			CharacterEffects.draw(r);
			Projectiles.draw(r);
			Entities.draw(r);
			lightingThread.join();
			Lighting.completionTasks(r);
			r.preLightingFBO.disableBuffer();
			drawLightingAndTerrain(r);
			BlockDamage.draw(r);
			DamagePosts.draw(r);
			PrecipitationParticles.draw(r);
			if (!SystemData.hideMouse) {
				MouseSelection.draw(r);
			}
			if (SystemData.isKeyboardBackpack) {
				Backpack.draw(r);
			}
			StatsBar.draw(r);
			//HotBar.draw(r);
			MouseItem.draw(r);
			if (!SystemData.characterLocationSet || SystemData.loadingWorld) {
				LoadingScreen.draw(r);
			}
			if (SystemData.isKeyboardMenu) {
				Menu.draw(r);
			}
			MouseTooltips.draw(r);
			if (SettingsData.showDiag) {
				DiagnosticsWindow.draw(r);
			}
			if (SystemData.showConsole) {
				Console.draw(r);
			}
		}
		OptionPane.draw(r);
	}
	
	private static void drawLightingAndTerrain(Renderer r) {
		GL20.glUseProgram(r.terrainDrawShader.getProgramId());
		int lightTexLocale = GL20.glGetUniformLocation(r.terrainDrawShader.getProgramId(), "lightTex");
		GL20.glUniform1i(lightTexLocale,  1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + 1); // Texture unit 1
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, r.lightingFBO.colorTextureID);
		
		int lightColorTexLocale = GL20.glGetUniformLocation(r.terrainDrawShader.getProgramId(), "lightColorTex");
		GL20.glUniform1i(lightColorTexLocale,  2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + 2); // Texture unit 1
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, r.lightingColorFBO.colorTextureID);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		int ambientColorLocation = GL20.glGetUniformLocation(r.terrainDrawShader.getProgramId(),"ambientLight");
		float brightness = (float) DataManager.world.getBrightness(DataManager.characterManager.characterEntity);
		if (DataManager.world.noLighting) {
			brightness = 1;
		}
		GL20.glUniform3f(ambientColorLocation, (brightness * brightness + brightness)/2f, (brightness * brightness + brightness)/2f, brightness);
		
		
		r.preLightingFBO.drawBuffer(0, 0, Display.getWidth(), Display.getHeight());
		GL20.glUseProgram(0);
	}

}
