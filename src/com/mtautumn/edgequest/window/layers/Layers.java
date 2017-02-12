package com.mtautumn.edgequest.window.layers;


import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.mtautumn.edgequest.window.Renderer;

public class Layers {
	public static void draw(Renderer r) throws InterruptedException {
		if (r.dataManager.system.isGameOnLaunchScreen) {
			LaunchScreen.draw(r);
		} else {
			r.preLightingFBO.enableBuffer();
			Terrain terrainThread = new Terrain(r);
			terrainThread.start();
			Lighting lightingThread = new Lighting(r);
			lightingThread.start();
			terrainThread.join();
			Terrain.completionTasks(r);
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
			if (!r.dataManager.system.hideMouse) MouseSelection.draw(r);
			StatsBar.draw(r);
			if (r.dataManager.system.isKeyboardBackpack) Backpack.draw(r);
			HotBar.draw(r);
			MouseItem.draw(r);
			if (!r.dataManager.system.characterLocationSet || r.dataManager.system.loadingWorld)
				LoadingScreen.draw(r);
			if (r.dataManager.system.isKeyboardMenu) Menu.draw(r);
			MouseTooltips.draw(r);
			if (r.dataManager.settings.showDiag) DiagnosticsWindow.draw(r);
			if (r.dataManager.system.showConsole) Console.draw(r);
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
		float brightness = (float) r.dataManager.world.getBrightness(r.dataManager.characterManager.characterEntity);
		GL20.glUniform3f(ambientColorLocation, (brightness * brightness + brightness)/2f, (brightness * brightness + brightness)/2f, brightness);
		
		
		r.preLightingFBO.drawBuffer(0, 0, Display.getWidth(), Display.getHeight());
		GL20.glUseProgram(0);
	}

}
