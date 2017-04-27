package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Time extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			dm.savable.time = Integer.parseInt(args.get(0));
			addInfoLine("Time set to: " + dm.savable.time, dm);
		} else if (args.size() == 0) {
			addInfoLine("Time: " + dm.savable.time, dm);
		} else {
			addErrorLine("use the format " + usage(), dm);
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
