package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Reseed extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() > 0) {
			dm.savable.seed = (long) Double.parseDouble(args.get(0));
			dm.resetTerrain();
			addInfoLine("reseeded to seed: " + args.get(0), dm);
		} else {
			addErrorLine("use the format " + usage(), dm);
		}
		return true;
	}

	@Override
	public String usage() {
		return "/reseed <seed>";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Rebuilds the world with a new seed.",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "reseed";
	}

}
