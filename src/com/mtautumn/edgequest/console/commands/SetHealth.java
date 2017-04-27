package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class SetHealth extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() > 0) {
			double health = Double.parseDouble(args.get(0));
			if (health > 100) health = 100;
			dm.characterManager.characterEntity.health = (int)(health/100.0 * dm.characterManager.characterEntity.maxHealth);
			addInfoLine("set health to: " + dm.characterManager.characterEntity.health, dm);
		} else
			addErrorLine("use the format " + usage(), dm);
		return true;
	}

	@Override
	public String usage() {
		return "/setHealth <percent health>";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Sets the player health to a percentage of the max health",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "setHealth";
	}

}
