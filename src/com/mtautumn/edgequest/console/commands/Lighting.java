package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Lighting extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("on")) {
				dm.world.noLighting = false;
				addInfoLine("Turned lighting on", dm);
			} else if (args.get(0).equalsIgnoreCase("off")) {
				dm.world.noLighting = true;
				addInfoLine("Turned lighting off", dm);
			}
		} else {
			if (dm.world.noLighting) {
				addInfoLine("Lighting is turned off", dm);
			} else {
				addInfoLine("Lighting is turned on", dm);
			}
		}
		return true;
	}

	@Override
	public String usage() {
		return "/lighting [on | off]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Enables or disables lighting effects (on or off)",
				"/lighting on enables lighting, /lighting off disables lighting",
				"and /lighting will display the current lighting state"
		};
	}

	@Override
	public String name() {
		return "lighting";
	}

}
