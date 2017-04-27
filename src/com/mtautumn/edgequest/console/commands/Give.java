package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Give extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			dm.backpackManager.addItem(dm.system.blockNameMap.get(args.get(0)));
			addInfoLine("Gave you 1 " + dm.system.blockNameMap.get(args.get(0)).getName(), dm);
		} else if (args.size() == 2) {
			for (int i = 0; i < Integer.parseInt(args.get(1)); i++) {
				dm.backpackManager.addItem(dm.system.blockNameMap.get(args.get(0)));
			}
			addInfoLine("Gave you " + Integer.parseInt(args.get(1)) + " " + dm.system.blockNameMap.get(args.get(0)).getName(), dm);
		} else {
			addErrorLine("use the format " + usage(), dm);
		}
		return false;
	}

	@Override
	public String usage() {
		return "/give <item name> [count]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Gives the player the object in question. Specifying a quantity after",
				"the object name will give multiple objects to the player.",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "give";
	}

}
