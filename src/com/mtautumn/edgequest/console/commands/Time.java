package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Time extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			DataManager.savable.time = Integer.parseInt(args.get(0));
			addInfoLine("Time set to: " + DataManager.savable.time);
		} else if (args.size() == 0) {
			addInfoLine("Time: " + DataManager.savable.time);
		} else {
			addErrorLine("use the format " + usage());
		}
		return false;
	}

	@Override
	public String usage() {
		return "/time [0-2399]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Used to set the time of day.",
				"Values roughly correspond to hours of the day",
				"with 1200 being noon as 2399 being right before midnight.",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "time";
	}

}
