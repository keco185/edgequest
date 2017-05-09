package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.SettingsData;

public class Speed extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			SettingsData.moveSpeed = Double.parseDouble(args.get(0));
			addInfoLine("Speed set to: " + SettingsData.moveSpeed);
		} else if (args.size() == 0) {
			addInfoLine("Speed is: " + SettingsData.moveSpeed);
		} else {
			addErrorLine("use the format " + usage());
		}
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
