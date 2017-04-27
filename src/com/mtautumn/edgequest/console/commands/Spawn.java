package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.entities.*;
import com.mtautumn.edgequest.entities.Character;

public class Spawn extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			Entity entity = new Entity(args.get(0), null, dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), 0, dm.characterManager.characterEntity.dungeonLevel, dm);
			switch (args.get(0)) {
			case "ant":
				dm.savable.entities.add(new Ant(entity));
				break;
			case "character":
				dm.savable.entities.add(new Character(entity));
				break;
			case "troll":
				dm.savable.entities.add(new Troll(entity));
				break;
			case "pet":
				dm.savable.entities.add(new Pet(entity));
				break;
			case "villager":
				dm.savable.entities.add(new Villager(entity));
				break;
			default:
				break;
			}
		} else if (args.size() == 2) {
			Entity entity = new Entity(args.get(0), null, dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), 0, dm.characterManager.characterEntity.dungeonLevel, dm);
			for (int i = 0; i < Integer.parseInt(args.get(1)); i++) {
				switch (args.get(0)) {
				case "ant":
					dm.savable.entities.add(new Ant(entity));
					break;
				case "character":
					dm.savable.entities.add(new Character(entity));
					break;
				case "troll":
					dm.savable.entities.add(new Troll(entity));
					break;
				case "pet":
					dm.savable.entities.add(new Pet(entity));
					break;
				case "villager":
					dm.savable.entities.add(new Villager(entity));
					break;
				default:
					break;
				}
			}
		} else {
			addErrorLine("use the format " + usage(), dm);
		}
		return true;
	}

	@Override
	public String usage() {
		return "/spawn <entity name> [count]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Spawns a defined number of a given entity",
				"These entities include any in-game creatures",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "spawn";
	}

}
