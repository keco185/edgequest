package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Help extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		boolean description = false;
		int page = 1;
		if (args.size() == 1) {
			if (args.get(0).matches("^-?\\d+$")) {
				page = Integer.parseInt(args.get(0));
			} else {
				description = true;
			}
		}
		if (args.size() < 2) {
			if (description) {
				boolean commandFound = false;
				for (Command cmd : DataManager.consoleManager.commands) {
					if (cmd.name().equalsIgnoreCase(args.get(0)) && !commandFound) {
						addInfoLine(args.get(0) + " command description:");
						for (String line : cmd.description()) {
							addInfoLine(line);
						}
						commandFound = true;
					}
				}
				if (!commandFound) {
					ArrayList<Command> possibleMatches = new ArrayList<Command>();
					for (Command cmd : DataManager.consoleManager.commands) {
						if (cmd.name().contains(args.get(0)) || args.get(0).contains(cmd.name())) {
							possibleMatches.add(cmd);
						} else {
							for (String string : cmd.description()) {
								if (string.contains(args.get(0)) && !possibleMatches.contains(cmd)) {
									possibleMatches.add(cmd);
								}
							}
						}
					}
					if (possibleMatches.size() > 0) {
						addInfoLine("Command not recognized. Here are some possible matches:");
						for (Command cmd : possibleMatches) {
							addInfoLine(cmd.usage());
						}
					} else {
						addInfoLine("Could not find any commands related to your query.");
					}
				}
			} else {
				int startCommand = (page - 1) * 7 + 1;
				int endCommand = startCommand + 6;
				if (DataManager.consoleManager.commands.size() >= startCommand && page > 0) {
					int pages = (int) Math.ceil(DataManager.consoleManager.commands.size() / 7.0);
					addInfoLine("Command List: (Page " + page + " of " + pages + ")");
					if (endCommand > DataManager.consoleManager.commands.size()) {
						endCommand = DataManager.consoleManager.commands.size();
					}
					for (int i = startCommand; i <= endCommand; i++) {
						addInfoLine("(" + i + ") " + DataManager.consoleManager.commands.get(i - 1).usage());
					}
				} else {
					addErrorLine("Not a page");
				}
			}
		} else {
			//addErrorLine("use the format " + usage(), dm);
		}
		return true;
	}

	@Override
	public String usage() {
		return "/help [page number | command name]";
	}

	@Override
	public String[] description() {

		return new String[]{
				"Gives information on available commands",
				"Typing /help will list the first page of commands",
				"Typing a /help 2 will list the second page and so on.",
				"Typing a command name after /help will describe the command.",
				"E.g. /help tp or /help help"
		};
	}

	@Override
	public String name() {
		return "help";
	}

}
