package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class UBM extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (dm.characterManager.characterEntity.stamina <= dm.characterManager.characterEntity.maxStamina) {
			dm.characterManager.characterEntity.stamina = 2147483647;
		} else {
			dm.characterManager.characterEntity.stamina = dm.characterManager.characterEntity.maxStamina;
		}
		addInfoLine("Usain Bolt Mode toggled", dm);
		return true;
	}

	@Override
	public String usage() {
		return "/ubm";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Toggles Usain Bolt Mode on and off",
				"When on, the player has infinite stamina!",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "ubm";
	}

}
