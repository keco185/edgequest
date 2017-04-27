package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Seed extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 0)
			addInfoLine("seed: " + dm.savable.seed, dm);
		else
			addErrorLine("Usage: " + usage(), dm);
		addErrorLine("To change the current seed, type /reseed <seed>", dm);
		return true;
	}

	@Override
	public String usage() {
		return "/seed";
	}

	@Override
	public String[] description() {
		return new String[]{
				"reports the current seed"
		};
	}

	@Override
	public String name() {
		return "seed";
	}

}
