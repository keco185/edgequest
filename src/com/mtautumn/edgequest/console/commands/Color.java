package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.threads.CharacterManager;

public class Color extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 3) {
			float r = new Float(args.get(0));
			float g = new Float(args.get(1));
			float b = new Float(args.get(2));
			CharacterManager.characterEntity.light.r = r;
			CharacterManager.characterEntity.light.g = g;
			CharacterManager.characterEntity.light.b = b;
			addInfoLine("Set light color to R: " + r + " G: " + g + " B: " + b);
		} else if (args.size() == 4) {
			float r = new Float(args.get(0));
			float g = new Float(args.get(1));
			float b = new Float(args.get(2));
			float a = new Float(args.get(3));
			CharacterManager.characterEntity.light.r = r;
			CharacterManager.characterEntity.light.g = g;
			CharacterManager.characterEntity.light.b = b;
			CharacterManager.characterEntity.light.maxBrightness = a;
			CharacterManager.characterEntity.light.brightness = a;
			addInfoLine("Set light color to R: " + r + " G: " + g + " B: " + b);
		} else {
			addErrorLine("Usage: " + usage());
		}
		return true;
	}

	@Override
	public String usage() {
		return "/color <red 0-1> <green 0-1> <blue 0-1> [brightness]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Changes the color of the player light source.",
				"Takes in RGB values, each from 0 to 1 and an option brightness",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "color";
	}

}
