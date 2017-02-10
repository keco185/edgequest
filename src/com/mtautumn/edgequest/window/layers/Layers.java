package com.mtautumn.edgequest.window.layers;


import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.layers.*;

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
			r.preLightingFBO.disableBuffer();
			r.preLightingFBO.drawBuffer(0, 0, Display.getWidth(), Display.getHeight());
			lightingThread.join();
			Lighting.completionTasks(r);
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

}
