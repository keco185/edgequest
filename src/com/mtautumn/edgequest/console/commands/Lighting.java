package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Lighting extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("on")) {
				DataManager.world.noLighting = false;
				addInfoLine("Turned lighting on");
			} else if (args.get(0).equalsIgnoreCase("off")) {
				DataManager.world.noLighting = true;
				addInfoLine("Turned lighting off");
			}
		} else {
			if (DataManager.world.noLighting) {
				addInfoLine("Lighting is turned off");
			} else {
				addInfoLine("Lighting is turned on");
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
