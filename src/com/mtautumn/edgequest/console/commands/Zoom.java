package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Zoom extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			dm.settings.targetBlockSize = Float.parseFloat(args.get(0));
			addInfoLine("Zooming out to a zoom level of " + args.get(0), dm);
		} else {
			addErrorLine("use the format " + usage(), dm);
		}
		return true;
	}

	@Override
	public String usage() {
		return "/zoom <zoom level>";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Allows for the manual adjustment of zoom level",
				"Level value is roughly measured in pixels per tile.",
				"It is highly recommended that the zoom level stay above 8",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "zoom";
	}

}
