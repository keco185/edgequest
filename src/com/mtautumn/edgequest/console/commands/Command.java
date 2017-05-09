package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public abstract class Command {
	public abstract boolean execute(ArrayList<String> args); //Return a value of true when execution is successful
	public abstract String usage();
	public abstract String[] description();
	public abstract String name();
	static void addLine(String text) {
		DataManager.consoleManager.addCommandFreeLine(text);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	static void addInfoLine(String text) {
		addLine(text, 2);
	}
	static void addErrorLine(String text) {
		addLine(text, 1);
	}
	static void addLine(String text, int type) {
		DataManager.consoleManager.addCommandFreeLine(text, type);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
