package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public abstract class Command {
	public abstract boolean execute(DataManager dm, ArrayList<String> args); //Return a value of true when execution is successful
	public abstract String usage();
	public abstract String[] description();
	public abstract String name();
	static void addLine(String text, DataManager dm) {
		dm.consoleManager.addCommandFreeLine(text);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	static void addInfoLine(String text, DataManager dm) {
		addLine(text, 2, dm);
	}
	static void addErrorLine(String text, DataManager dm) {
		addLine(text, 1, dm);
	}
	static void addLine(String text, int type, DataManager dm) {
		dm.consoleManager.addCommandFreeLine(text, type);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
