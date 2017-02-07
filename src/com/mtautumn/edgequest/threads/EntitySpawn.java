package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Ant;
import com.mtautumn.edgequest.entities.Character;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.entities.Troll;

public class EntitySpawn extends Thread {
	DataManager dm;
	public EntitySpawn(DataManager dm) {
		this.dm = dm;
	}
	public void run() {
		while (dm.system.running) {
			try {


				Thread.sleep(dm.settings.tickLength);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void considerEntity(Location location) {
		if (location.level > -1) { //in dungeon
			if (!dm.world.isStructBlock(location)) {
				if (Math.random() > 0.9995) {
					spawn("ant", location);
				} 
				if (Math.random() > 1.0 - Math.sqrt(location.level) * 0.0003) {
					spawn("troll", location);
				}
			}
		}
	}
	public void spawn(String type, Location location) {
		Entity entity = new Entity(type, null, location.x, location.y, 0.0, location.level, dm);
		switch (type) {
		case "ant":
			dm.savable.entities.add(new Ant(entity));
			break;
		case "character":
			dm.savable.entities.add(new Character(entity));
			break;
		case "troll":
			dm.savable.entities.add(new Troll(entity));
			break;
		default:
			break;
		}
	}
}