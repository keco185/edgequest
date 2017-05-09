package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Seed extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 0) {
			addInfoLine("seed: " + DataManager.savable.seed);
		} else {
			addErrorLine("Usage: " + usage());
		}
		addErrorLine("To change the current seed, type /reseed <seed>");
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
