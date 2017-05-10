package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.threads.CharacterManager;

public class UBM extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (CharacterManager.characterEntity.stamina <= CharacterManager.characterEntity.maxStamina) {
			CharacterManager.characterEntity.stamina = 2147483647;
		} else {
			CharacterManager.characterEntity.stamina = CharacterManager.characterEntity.maxStamina;
		}
		addInfoLine("Usain Bolt Mode toggled");
		return true;
	}

	@Override
	public String usage() {
		return "/ubm";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Toggles Usain Bolt Mode on and off",
				"When on, the player has infinite stamina!",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "ubm";
	}

}
