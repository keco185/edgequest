package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class EntitySpawn extends Thread{
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
		default:
			break;
		}
	}
}
