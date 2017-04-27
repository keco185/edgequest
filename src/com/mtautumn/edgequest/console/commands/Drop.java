package com.mtautumn.edgequest.console.commands;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;

public class Drop extends Command {

	@Override
	public boolean execute(DataManager dm, ArrayList<String> args) {
		if (args.size() == 1) {
			ItemSlot item = new ItemSlot();
			item.setItem(dm.system.blockNameMap.get(args.get(0)).getID());
			item.setItemCount(1);
			dm.savable.itemDrops.add(new ItemDrop(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), dm.characterManager.characterEntity.dungeonLevel, item, dm));
		} else if (args.size() == 2) {
			ItemSlot item = new ItemSlot();
			item.setItem(dm.system.blockNameMap.get(args.get(0)).getID());
			item.setItemCount(Integer.parseInt(args.get(1)));
			dm.savable.itemDrops.add(new ItemDrop(dm.characterManager.characterEntity.getX(), dm.characterManager.characterEntity.getY(), dm.characterManager.characterEntity.dungeonLevel, item, dm));
		} else {
			addErrorLine("use the format " + usage(), dm);
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
