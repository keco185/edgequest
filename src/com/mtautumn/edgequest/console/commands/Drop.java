package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.threads.CharacterManager;

public class Drop extends Command {

	@Override
	public boolean execute(ArrayList<String> args) {
		if (args.size() == 1) {
			ItemSlot item = new ItemSlot();
			item.setItem(SystemData.blockNameMap.get(args.get(0)).getID());
			item.setItemCount(1);
			DataManager.savable.itemDrops.add(new ItemDrop(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), CharacterManager.characterEntity.dungeonLevel, item));
		} else if (args.size() == 2) {
			ItemSlot item = new ItemSlot();
			item.setItem(SystemData.blockNameMap.get(args.get(0)).getID());
			item.setItemCount(Integer.parseInt(args.get(1)));
			DataManager.savable.itemDrops.add(new ItemDrop(CharacterManager.characterEntity.getX(), CharacterManager.characterEntity.getY(), CharacterManager.characterEntity.dungeonLevel, item));
		} else {
			addErrorLine("use the format " + usage());
		}
		return true;
	}

	@Override
	public String usage() {
		return "/drop <item name> [count]";
	}

	@Override
	public String[] description() {
		return new String[]{
				"Drops the specified item(s) on the ground under the player.",
				"Adding a number to the end of the command will drop multiple items.",
				"Usage: " + usage()
		};
	}

	@Override
	public String name() {
		return "drop";
	}

}
