/* Keeps track of the lines visible in the game console. Will also
 * parse new lines for commands and run such commands if they exist.
 */
package com.mtautumn.edgequest.console;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.console.commands.*;
import com.mtautumn.edgequest.data.DataManager;

public class ConsoleManager {
	DataManager dataManager;
	public ArrayList<Command> commands;
	public ConsoleManager(DataManager dataManager) {
		this.dataManager = dataManager;
		commands = loadCommands();
	}
	public class Line {
		private long creationTime;
		private String text;
		public Color color = new Color(Color.white);

		Line(String text) {
			creationTime = System.currentTimeMillis();
			this.text = text;
		}
		Line(String text, Color color) {
			creationTime = System.currentTimeMillis();
			this.text = text;
			this.color = color;
		}
		public String getText() { return text; }
		public long getTime() { return creationTime; }
	}	
	public ArrayList<Line> lines = new ArrayList<Line>();

	private static ArrayList<Command> loadCommands() {
		ArrayList<Command> commands = new ArrayList<Command>();
		commands.add(new com.mtautumn.edgequest.console.commands.Color());
		commands.add(new Drop());
		commands.add(new Give());
		commands.add(new God());
		commands.add(new Help());
		commands.add(new KillAll());
		commands.add(new Lighting());
		commands.add(new Reseed());
		commands.add(new Sanic());
		commands.add(new Seed());
		commands.add(new SetHealth());
		commands.add(new Spawn());
		commands.add(new Speed());
		commands.add(new Time());
		commands.add(new Tp());
		commands.add(new UBM());
		commands.add(new Weather());
		commands.add(new Zoom());

		commands.sort((s1, s2) -> s1.name().compareToIgnoreCase(s2.name()));
		return commands;
	}

	public void addLine(String text) {
		// Linux doesn't like colons
		if (text.startsWith("/")) {
			parseCommand(text);
		} else {
			lines.add(new Line(text));
		}
	}
	public void addCommandFreeLine(String text) {
		lines.add(new Line(text));
	}
	public void addLine(String text, int type) {
		// Linux doesn't like colons
		if (text.startsWith("/")) {
			parseCommand(text);
		} else {
			switch (type) {
			case 1:
				lines.add(new Line(text,Color.red));
				break;
			case 2:
				lines.add(new Line(text,Color.blue));
				break;
			default:
				lines.add(new Line(text));
				break;
			}
		}
	}
	public void addCommandFreeLine(String text, int type) {
		switch (type) {
		case 1:
			lines.add(new Line(text,Color.red));
			break;
		case 2:
			lines.add(new Line(text,Color.blue));
			break;
		default:
			lines.add(new Line(text));
			break;
		}
	}
	private void parseCommand(String text) {
		String cmdName;
		boolean moreArgs;
		if (text.contains(" ")) {
			cmdName = text.substring(1, text.indexOf(" "));
			moreArgs = text.indexOf(" ") + 1 < text.length();
		} else {
			cmdName = text.substring(1, text.length());
			moreArgs = false;
		}
		ArrayList<String> args = new ArrayList<String>();
		String remaining = text.substring(text.indexOf(" ") + 1, text.length());
		while (moreArgs) {
			String nextArg;
			if (remaining.contains(" ")) {
				nextArg = remaining.substring(0, remaining.indexOf(" "));
			} else {
				nextArg = remaining.substring(0, remaining.length());
			}
			args.add(nextArg);			
			moreArgs = false;
			if (remaining.contains(" ")) {
				if (remaining.length() > remaining.indexOf(" ") + 1) {
					remaining = remaining.substring(remaining.indexOf(" ") + 1, remaining.length());
					moreArgs = true;
				}
			}
		}
		try {
			runCommand(cmdName, args);
		} catch (Exception e) {
			addLine("Command entered wrong", 1);
			e.printStackTrace();
		}
	}
	private void runCommand(String cmdName, ArrayList<String> args) throws InterruptedException {
		boolean commandFound = false;
		for (Command cmd : commands) {
			if (cmd.name().equalsIgnoreCase(cmdName)) {
				cmd.execute(dataManager, args);
				commandFound = true;
			}
		}
		if (!commandFound) {
			addLine("unknown command \"" + cmdName + "\"", 1);
		}
	}
	public Line[] getNewestLines(int count) {
		Line[] lines = new Line[count];
		@SuppressWarnings("unchecked")
		ArrayList<Line> lineDB = (ArrayList<Line>) this.lines.clone();
		for (int i = 0; i < count; i++) {
			Line newest = getNewest(lineDB);
			lines[i] = newest;
			if (newest != null) {
				lineDB.remove(newest);
			}
		}
		return lines;
	}

	private static Line getNewest(ArrayList<Line> lines) {
		Line newest = null;
		for (int i = 0; i < lines.size(); i++) {
			if (newest != null) {
				if (newest.creationTime < lines.get(i).creationTime) {
					newest = lines.get(i);
				}
			} else {
				newest = lines.get(i);
			}
		}
		return newest;
	}

}
