package com.mtautumn.edgequest.window;


import com.mtautumn.edgequest.window.layers.*;

public class Layers {
	static void draw(Renderer r) {
		if (r.dataManager.system.isGameOnLaunchScreen) {
			LaunchScreen.draw(r);
		} else {
			Terrain.draw(r);
			Footprints.draw(r);
			CharacterEffects.draw(r);
			Projectiles.draw(r);
			Entities.draw(r);
			Lighting.draw(r);
			BlockDamage.draw(r);
			DamagePosts.draw(r);
			if (!r.dataManager.system.hideMouse) MouseSelection.draw(r);
			StatsBar.draw(r);
			if (r.dataManager.system.isKeyboardBackpack) Backpack.draw(r);
			HotBar.draw(r);
			MouseItem.draw(r);
			if (!r.dataManager.system.characterLocationSet || r.dataManager.system.loadingWorld)
				LoadingScreen.draw(r);
			if (r.dataManager.system.isKeyboardMenu) Menu.draw(r);
			if (r.dataManager.settings.showDiag) DiagnosticsWindow.draw(r);
			if (r.dataManager.system.showConsole) Console.draw(r);
		}
		OptionPane.draw(r);
	}

}
