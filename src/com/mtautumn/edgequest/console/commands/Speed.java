package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Speed extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			dm.settings.moveSpeed = Double.parseDouble(args.get(0));
			addInfoLine("Speed set to: " + dm.settings.moveSpeed, dm);
		} else if (args.size() == 0)
			addInfoLine("Speed is: " + dm.settings.moveSpeed, dm);
		else
			addErrorLine("use the format " + usage(), dm);
		return true;
	}

	@Override
	public String usage() {
		return "/speed [value]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Sets or reports the movement speed of the player",
				"Using /speed with no arguments will report the current speed",
				"/speed # will change the current speed to a new value",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "speed";
	}

}
