package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class Tp extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 2) {
			dm.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
			dm.system.characterMoving = true;
			dm.system.requestGenUpdate = true;
			dm.system.requestScreenUpdate = true;
			addInfoLine("Teleported to: " + args.get(0) + ", " + args.get(1), dm);
		} else if (args.size() == 3) {
			dm.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
			dm.system.characterMoving = true;
			dm.system.requestGenUpdate = true;
			dm.system.requestScreenUpdate = true;
			dm.savable.dungeonLevel = Integer.parseInt(args.get(2));
			dm.characterManager.characterEntity.dungeonLevel = dm.savable.dungeonLevel;
			addInfoLine("Teleported to: " + args.get(0) + ", " + args.get(1) + ", " + args.get(2), dm);
		} else {
			addErrorLine("use the format " + usage(), dm);
		}
		return true;
	}

	@Override
	public String usage() {
		return "/tp <posX> <posY> [dungeon level]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Lets the player teleport to any coordinate.",
				"The player can specify and x and y coordinate",
				"or an x, y, and dungeon level coordinate set",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "tp";
	}

}
