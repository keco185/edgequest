package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class ReloadTextures extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		DataManager.rendererManager.renderer.textureManager.scheduleReload();
		addInfoLine("Reloading Game Textures...");
		return true;
	}

	@Override
	public String usage() {
		return "/reloadTextures";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Reloads all in-game textures and UI elements.",
				"This is used to see how a new texture looks in-game"
		};
	}

	@Override
	public String name() {
		return "reloadTextures";
	}

}
