package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Weather extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("rain") || args.get(0).equalsIgnoreCase("snow") || args.get(0).equalsIgnoreCase("storm")) {
				dm.weatherManager.startRain();
				addInfoLine("Let it rain!", dm);
			} else if (args.get(0).equalsIgnoreCase("sun") || args.get(0).equalsIgnoreCase("sunny") || args.get(0).equalsIgnoreCase("clear")) {
				dm.weatherManager.endRain();
				addInfoLine("Here comes the sun", dm);
			} else {
				addErrorLine("Did not recognize your weather type", dm);
				addErrorLine("Usage: " + usage(), dm);
			}
		} else {
			addErrorLine("Usage: " + usage(), dm);
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
