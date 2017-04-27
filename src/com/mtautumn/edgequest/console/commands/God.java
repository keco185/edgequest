package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class God extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (dm.characterManager.characterEntity.health <= dm.characterManager.characterEntity.maxHealth) {
			dm.characterManager.characterEntity.health = 2147483647;
		} else {
			dm.characterManager.characterEntity.health = dm.characterManager.characterEntity.maxHealth;
		}
		addInfoLine("God Mode toggled", dm);
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
