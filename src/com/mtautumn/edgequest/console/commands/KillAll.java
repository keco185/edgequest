package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class KillAll extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		int entities = 0;
		for (int i = 0; i < DataManager.savable.entities.size(); i++) {
			if (DataManager.savable.entities.get(i) != DataManager.characterManager.characterEntity) {
				DataManager.savable.entities.remove(i);
				i--;
				entities++;
			}
		}
		addInfoLine("Killed " + entities + " entities");
		return true;
	}

	@Override
	public String usage() {
		return "/killall";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Kills all entities in the game"
		};
	}

	@Override
	public String name() {
		return "killall";
	}

}
