/* Keeps track of the lines visible in the game console. Will also
 * parse new lines for commands and run such commands if they exist.
 */
package com.mtautumn.edgequest;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;

public class ConsoleManager {
	DataManager dataManager;
	public ConsoleManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public class Line {
		private long creationTime;
		private String text;
		public Color color = new Color(Color.white);

		Line(String text) {
			creationTime = System.currentTimeMillis();
			this.text = text;
		}
		Line(String text, Color color) {
			creationTime = System.currentTimeMillis();
			this.text = text;
			this.color = color;
		}
		public String getText() { return text; }
		public long getTime() { return creationTime; }
	}	
	public ArrayList<Line> lines = new ArrayList<Line>();


	public void addLine(String text) {
		// Linux doesn't like colons
		if (text.startsWith("/"))
			parseCommand(text);
		else
			lines.add(new Line(text));
	}
	public void addLine(String text, int type) {
		// Linux doesn't like colons
		if (text.startsWith("/"))
			parseCommand(text);
		else
			switch (type) {
			case 1:
				lines.add(new Line(text,Color.red));
				break;
			case 2:
				lines.add(new Line(text,Color.blue));
				break;
			default:
				lines.add(new Line(text));
				break;
			}
	}
	private void parseCommand(String text) {
		String cmdName;
		boolean moreArgs;
		if (text.contains(" ")) {
			cmdName = text.substring(1, text.indexOf(" "));
			moreArgs = text.indexOf(" ") + 1 < text.length();
		} else {
			cmdName = text.substring(1, text.length());
			moreArgs = false;
		}
		ArrayList<String> args = new ArrayList<String>();
		String remaining = text.substring(text.indexOf(" ") + 1, text.length());
		while (moreArgs) {
			String nextArg;
			if (remaining.contains(" ")) {
				nextArg = remaining.substring(0, remaining.indexOf(" "));
			} else {
				nextArg = remaining.substring(0, remaining.length());
			}
			args.add(nextArg);			
			moreArgs = false;
			if (remaining.contains(" ")) {
				if (remaining.length() > remaining.indexOf(" ") + 1) {
					remaining = remaining.substring(remaining.indexOf(" ") + 1, remaining.length());
					moreArgs = true;
				}
			}
		}
		try {
			runCommand(cmdName, args);
		} catch (Exception e) {
			addLine("Command entered wrong", 1);
		}
	}
	private void runCommand(String cmdName, ArrayList<String> args) throws InterruptedException {
		switch (cmdName) {
		case "time":
			if (args.size() == 1) {
				dataManager.savable.time = Integer.parseInt(args.get(0));
				addLine("Time set to: " + dataManager.savable.time, 2);
			} else if (args.size() == 0)
				addLine("Time: " + dataManager.savable.time, 2);
			else
				addLine("use the format /time [0-2399]", 1);
			break;
		case "tp":
			if (args.size() == 2) {
				dataManager.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
				dataManager.system.characterMoving = true;
				dataManager.system.requestGenUpdate = true;
				dataManager.system.requestScreenUpdate = true;
				addLine("Teleported to: " + args.get(0) + ", " + args.get(1), 2);
			} else
				addLine("use the format /tp <posX> <posY>", 1);
			break;
		case "speed":
			if (args.size() == 1) {
				dataManager.settings.moveSpeed = Double.parseDouble(args.get(0));
				addLine("Speed set to: " + dataManager.settings.moveSpeed, 2);
			} else if (args.size() == 0)
				addLine("Speed is: " + dataManager.settings.moveSpeed, 2);
			else
				addLine("use the format /speed [value]", 1);
			break;
		case "reseed":
			if (args.size() > 0) {
				dataManager.savable.seed = (long) Double.parseDouble(args.get(0));
				dataManager.resetTerrain();
				addLine("reseeded to seed: " + args.get(0), 2);
			} else
				addLine("use the format /reseed <seed>", 1);
			break;
		case "seed":
			if (args.size() == 0)
				addLine("seed: " + dataManager.savable.seed, 2);
			else
				addLine("To change the current seed, type /reseed <seed>", 1);
			break;
		case "give":
			if (args.size() == 1) {
				dataManager.backpackManager.addItem(dataManager.system.blockNameMap.get(args.get(0)));
				addLine("Gave you 1 " + dataManager.system.blockNameMap.get(args.get(0)).getName(), 2);
			} else if (args.size() == 2) {
				for (int i = 0; i < Integer.parseInt(args.get(1)); i++) {
					dataManager.backpackManager.addItem(dataManager.system.blockNameMap.get(args.get(0)));
				}
				addLine("Gave you " + Integer.parseInt(args.get(1)) + " " + dataManager.system.blockNameMap.get(args.get(0)).getName(), 2);
			} else {
				addLine("use the format /give <item name> [count]", 1);
			}
			break;
		case "spawn":
			if (args.size() == 1) {
				Entity entity = new Entity(args.get(0), null, dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), 0, dataManager.characterManager.characterEntity.dungeonLevel, dataManager);
				switch (args.get(0)) {
				case "ant":
					dataManager.savable.entities.add(new Ant(entity));
					break;
				case "character":
					dataManager.savable.entities.add(new Character(entity));
					break;
				case "troll":
					dataManager.savable.entities.add(new Troll(entity));
					break;
				case "pet":
					dataManager.savable.entities.add(new Pet(entity));
					break;
				default:
					break;
				}
			} else if (args.size() == 2) {
				Entity entity = new Entity(args.get(0), null, dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), 0, dataManager.characterManager.characterEntity.dungeonLevel, dataManager);
				for (int i = 0; i < Integer.parseInt(args.get(1)); i++) {
						switch (args.get(0)) {
						case "ant":
							dataManager.savable.entities.add(new Ant(entity));
							break;
						case "character":
							dataManager.savable.entities.add(new Character(entity));
							break;
						case "troll":
							dataManager.savable.entities.add(new Troll(entity));
							break;
						case "pet":
							dataManager.savable.entities.add(new Pet(entity));
							break;
						default:
							break;
						}
				}
			} else {
				addLine("use the format /spawn <entity name> [count]", 1);
			}
			break;
		case "setHealth":
			if (args.size() > 0) {
				double health = Double.parseDouble(args.get(0));
				if (health > 100) health = 100;
				dataManager.characterManager.characterEntity.health = (int)(health/100.0 * dataManager.characterManager.characterEntity.maxHealth);
				addLine("set health to: " + dataManager.characterManager.characterEntity.health, 2);
			} else
				addLine("use the format /setHealth <percent health>", 1);
			break;
		case "ubm":
			if (dataManager.characterManager.characterEntity.stamina <= dataManager.characterManager.characterEntity.maxStamina) {
				dataManager.characterManager.characterEntity.stamina = 2147483647;
			} else {
				dataManager.characterManager.characterEntity.stamina = dataManager.characterManager.characterEntity.maxStamina;
			}
			addLine("Usain Bolt Mode toggled", 2);
			break;
		case "tgm":
			// '20' is the max health of the character class
			if (dataManager.characterManager.characterEntity.health <= dataManager.characterManager.characterEntity.maxHealth) {
				dataManager.characterManager.characterEntity.health = 2147483647;
			} else {
				dataManager.characterManager.characterEntity.health = dataManager.characterManager.characterEntity.maxHealth;
			}
			addLine("God Mode toggled", 2);
			break;
		case "drop":
			if (args.size() == 1) {
				ItemSlot item = new ItemSlot();
				item.setItem(dataManager.system.blockNameMap.get(args.get(0)).getID());
				item.setItemCount(1);
				dataManager.savable.itemDrops.add(new ItemDrop(dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), dataManager.characterManager.characterEntity.dungeonLevel, item, dataManager));
			} else if (args.size() == 2) {
				ItemSlot item = new ItemSlot();
				item.setItem(dataManager.system.blockNameMap.get(args.get(0)).getID());
				item.setItemCount(Integer.parseInt(args.get(1)));
				dataManager.savable.itemDrops.add(new ItemDrop(dataManager.characterManager.characterEntity.getX(), dataManager.characterManager.characterEntity.getY(), dataManager.characterManager.characterEntity.dungeonLevel, item, dataManager));
			} else {
				addLine("use the format /drop <item name> [count]", 1);
			}
			break;
		case "rain":
			dataManager.weatherManager.startRain();
			addLine("Let it rain!", 2);
			break;
		case "help":
			int page = (args.size() > 0) ? Integer.parseInt(args.get(0)) : 1;
			switch (page) {
			default:
				addLine("Command List: (Page 1)", 2);
				Thread.sleep(1);
				addLine("     (1) /help [page number]", 2);
				Thread.sleep(1);
				addLine("     (2) /time [0-2399]", 2);
				Thread.sleep(1);
				addLine("     (3) /tp <posX> <posY>", 2);
				Thread.sleep(1);
				addLine("     (4) /seed", 2);
				Thread.sleep(1);
				addLine("     (5) /speed [value]", 2);
				Thread.sleep(1);
				addLine("     (6) /reseed <seed>", 2);
				Thread.sleep(1);
				addLine("     (7) /give <item name> [count]", 2);
				break;
			case 2:
				addLine("Command List: (Page 2)", 2);
				Thread.sleep(1);
				addLine("     (8) /spawn <entity name> [count]", 2);
				Thread.sleep(1);
				addLine("     (9) /setHealth [percent health]", 2);
				Thread.sleep(1);
				addLine("     (10) /ubm", 2);
				Thread.sleep(1);
				addLine("     (11) /tgm", 2);
				Thread.sleep(1);
				addLine("     (12) /drop <item name> [count]", 2);
				Thread.sleep(1);
				addLine("     (13) /rain", 2);
				break;
			}
			break;
		default:
			addLine("unknown command \"" + cmdName + "\"", 1);
			break;
		}
	}
	public Line[] getNewestLines(int count) {
		Line[] lines = new Line[count];
		@SuppressWarnings("unchecked")
		ArrayList<Line> lineDB = (ArrayList<Line>) this.lines.clone();
		for (int i = 0; i < count; i++) {
			Line newest = getNewest(lineDB);
			lines[i] = newest;
			if (newest != null)
				lineDB.remove(newest);
		}
		return lines;
	}

	private static Line getNewest(ArrayList<Line> lines) {
		Line newest = null;
		for (int i = 0; i < lines.size(); i++) {
			if (newest != null) {
				if (newest.creationTime < lines.get(i).creationTime)
					newest = lines.get(i);
			} else {
				newest = lines.get(i);
			}
		}
		return newest;
	}

}
