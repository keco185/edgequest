package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class God extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (DataManager.characterManager.characterEntity.health <= DataManager.characterManager.characterEntity.maxHealth) {
			DataManager.characterManager.characterEntity.health = 2147483647;
		} else {
			DataManager.characterManager.characterEntity.health = DataManager.characterManager.characterEntity.maxHealth;
		}
		addInfoLine("God Mode toggled");
		return true;
	}

	@Override
	public String usage() {
		return "/god";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Toggles god Mode on and off",
				"When on, the player has infinite health!",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "god";
	}

}
