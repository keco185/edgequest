/* Keeps track of the lines visible in the game console. Will also
 * parse new lines for commands and run such commands if they exist.
 */
package com.mtautumn.edgequest;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.entities.Ant;
import com.mtautumn.edgequest.entities.Character;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.entities.Pet;
import com.mtautumn.edgequest.entities.Troll;

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
		case "clear":
			dataManager.weatherManager.endRain();
			addLine("Here comes the sun", 2);
			break;
		case "color":
			if (args.size() == 3) {
				float r = new Float(args.get(0));
				float g = new Float(args.get(1));
				float b = new Float(args.get(2));
				dataManager.characterManager.characterEntity.light.r = r;
				dataManager.characterManager.characterEntity.light.g = g;
				dataManager.characterManager.characterEntity.light.b = b;
				addLine("Set light color to R: " + r + " G: " + g + " B: " + b);
			} if (args.size() == 4) {
				float r = new Float(args.get(0));
				float g = new Float(args.get(1));
				float b = new Float(args.get(2));
				float a = new Float(args.get(3));
				dataManager.characterManager.characterEntity.light.r = r;
				dataManager.characterManager.characterEntity.light.g = g;
				dataManager.characterManager.characterEntity.light.b = b;
				dataManager.characterManager.characterEntity.light.maxBrightness = a;
				dataManager.characterManager.characterEntity.light.brightness = a;
				addLine("Set light color to R: " + r + " G: " + g + " B: " + b);
			}
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
		case "killall":
			for (int i = 0; i < dataManager.savable.entities.size(); i++) {
				if (dataManager.savable.entities.get(i).entityType != Entity.EntityType.character) {
					dataManager.savable.entities.remove(i);
					i--;
				}
			}
			break;
		case "rain":
			dataManager.weatherManager.startRain();
			addLine("Let it rain!", 2);
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
		case "setHealth":
			if (args.size() > 0) {
				double health = Double.parseDouble(args.get(0));
				if (health > 100) health = 100;
				dataManager.characterManager.characterEntity.health = (int)(health/100.0 * dataManager.characterManager.characterEntity.maxHealth);
				addLine("set health to: " + dataManager.characterManager.characterEntity.health, 2);
			} else
				addLine("use the format /setHealth <percent health>", 1);
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
		case "speed":
			if (args.size() == 1) {
				dataManager.settings.moveSpeed = Double.parseDouble(args.get(0));
				addLine("Speed set to: " + dataManager.settings.moveSpeed, 2);
			} else if (args.size() == 0)
				addLine("Speed is: " + dataManager.settings.moveSpeed, 2);
			else
				addLine("use the format /speed [value]", 1);
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
			} else if (args.size() == 3) {
				dataManager.characterManager.characterEntity.setPos(Double.parseDouble(args.get(0)),Double.parseDouble(args.get(1)));
				dataManager.system.characterMoving = true;
				dataManager.system.requestGenUpdate = true;
				dataManager.system.requestScreenUpdate = true;
				dataManager.savable.dungeonLevel = Integer.parseInt(args.get(2));
				dataManager.characterManager.characterEntity.dungeonLevel = dataManager.savable.dungeonLevel;
				addLine("Teleported to: " + args.get(0) + ", " + args.get(1) + ", " + args.get(2), 2);
			} else {
				addLine("use the format /tp <posX> <posY> [dungeon level]", 1);
			}
			break;
		case "ubm":
			if (dataManager.characterManager.characterEntity.stamina <= dataManager.characterManager.characterEntity.maxStamina) {
				dataManager.characterManager.characterEntity.stamina = 2147483647;
			} else {
				dataManager.characterManager.characterEntity.stamina = dataManager.characterManager.characterEntity.maxStamina;
			}
			addLine("Usain Bolt Mode toggled", 2);
			break;
		case "help":
			int page = (args.size() > 0) ? Integer.parseInt(args.get(0)) : 1;
			switch (page) {
			default:
				addLine("Command List: (Page 1)", 2);
				Thread.sleep(1);
				addLine("     (1) /help [page number]", 2);
				Thread.sleep(1);
				addLine("     (2) /color <red 0-1> <green 0-1> <blue 0-1> [brightness]", 2);
				Thread.sleep(1);
				addLine("     (3) /drop <item> [amount]", 2);
				Thread.sleep(1);
				addLine("     (4) /give <item name> [count]", 2);
				Thread.sleep(1);
				addLine("     (5) /killall", 2);
				Thread.sleep(1);
				addLine("     (6) /rain", 2);
				Thread.sleep(1);
				addLine("     (7) /reseed <seed>", 2);
				break;
			case 2:
				addLine("Command List: (Page 2)", 2);
				Thread.sleep(1);
				addLine("     (8) /seed", 2);
				Thread.sleep(1);
				addLine("     (9) /setHealth [percent health]", 2);
				Thread.sleep(1);
				addLine("     (10) /spawn <entity name> [count]", 2);
				Thread.sleep(1);
				addLine("     (11) /speed [value]", 2);
				Thread.sleep(1);
				addLine("     (12) /tgm", 2);
				Thread.sleep(1);
				addLine("     (13) /time [0-2399]", 2);
				Thread.sleep(1);
				addLine("     (14) /tp <posX> <posY>", 2);
				break;
			case 3:
				addLine("Command List: (Page 3)", 2);
				Thread.sleep(1);
				addLine("     (15) /ubm", 2);
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
