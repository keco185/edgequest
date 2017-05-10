package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.entities.*;
import com.mtautumn.edgequest.entities.Character;
import com.mtautumn.edgequest.threads.CharacterManager;

public class Spawn extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			Entity entity = new Entity(args.get(0), null, CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), 0, CharacterManager.characterEntity.dungeonLevel);
			switch (args.get(0)) {
			case "ant":
				DataManager.savable.entities.add(new Ant(entity));
				break;
			case "character":
				DataManager.savable.entities.add(new Character(entity));
				break;
			case "troll":
				DataManager.savable.entities.add(new Troll(entity));
				break;
			case "pet":
				DataManager.savable.entities.add(new Pet(entity));
				break;
			case "villager":
				DataManager.savable.entities.add(new Villager(entity));
				break;
			default:
				break;
			}
		} else if (args.size() == 2) {
			Entity entity = new Entity(args.get(0), null, CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), 0, CharacterManager.characterEntity.dungeonLevel);
			for (int i = 0; i < Integer.parseInt(args.get(1)); i++) {
				switch (args.get(0)) {
				case "ant":
					DataManager.savable.entities.add(new Ant(entity));
					break;
				case "character":
					DataManager.savable.entities.add(new Character(entity));
					break;
				case "troll":
					DataManager.savable.entities.add(new Troll(entity));
					break;
				case "pet":
					DataManager.savable.entities.add(new Pet(entity));
					break;
				case "villager":
					DataManager.savable.entities.add(new Villager(entity));
					break;
				default:
					break;
				}
			}
		} else {
			addErrorLine("use the format " + usage());
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
