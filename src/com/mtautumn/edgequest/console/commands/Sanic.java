package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Sanic extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		dm.characterManager.characterEntity.stamina = 2147483647;
		dm.settings.moveSpeed = 15;
		return false;
	}

	@Override
	public String usage() {
		return "/sanic";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Gotta go fast",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "sanic";
	}

}
