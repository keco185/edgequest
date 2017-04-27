package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;

public class KillAll extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		int entities = 0;
		for (int i = 0; i < dm.savable.entities.size(); i++) {
			if (dm.savable.entities.get(i) != dm.characterManager.characterEntity) {
				dm.savable.entities.remove(i);
				i--;
				entities++;
			}
		}
		addInfoLine("Killed " + entities + " entities", dm);
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
