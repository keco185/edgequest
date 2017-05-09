package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;

public class Tp extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 2) {
			DataManager.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
			SystemData.characterMoving = true;
			SystemData.requestGenUpdate = true;
			SystemData.requestScreenUpdate = true;
			addInfoLine("Teleported to: " + args.get(0) + ", " + args.get(1));
		} else if (args.size() == 3) {
			DataManager.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
			SystemData.characterMoving = true;
			SystemData.requestGenUpdate = true;
			SystemData.requestScreenUpdate = true;
			DataManager.savable.dungeonLevel = Integer.parseInt(args.get(2));
			DataManager.characterManager.characterEntity.dungeonLevel = DataManager.savable.dungeonLevel;
			addInfoLine("Teleported to: " + args.get(0) + ", " + args.get(1) + ", " + args.get(2));
		} else {
			addErrorLine("use the format " + usage());
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
