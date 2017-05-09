package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Weather extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("rain") || args.get(0).equalsIgnoreCase("snow") || args.get(0).equalsIgnoreCase("storm")) {
				DataManager.weatherManager.startRain();
				addInfoLine("Let it rain!");
			} else if (args.get(0).equalsIgnoreCase("sun") || args.get(0).equalsIgnoreCase("sunny") || args.get(0).equalsIgnoreCase("clear")) {
				DataManager.weatherManager.endRain();
				addInfoLine("Here comes the sun");
			} else {
				addErrorLine("Did not recognize your weather type");
				addErrorLine("Usage: " + usage());
			}
		} else {
			addErrorLine("Usage: " + usage());
		}
		return true;
	}

	@Override
	public String usage() {
		return "/weather <rain | sun>";
	}

	@Override
	public String[] description() {

		return new String[]{
				"Allows you to turn the rain/snow on and off",
				"/weather rain makes it ran and /weather sun",
				"makes it sunny."
		};
	}

	@Override
	public String name() {
		return "weather";
	}

}
