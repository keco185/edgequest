package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class EntityUpdater extends Thread{
	DataManager dm;
	public EntityUpdater(DataManager dm) {
		this.dm = dm;
	}
	private void moveEntity(Entity entity, double r, double theta, double x, double y) {
		double deltaX = Math.cos(theta) * r;
		double deltaY = Math.sin(theta) * r;
		if (!dm.world.isStructBlock((int)(deltaX + x), (int)(deltaY + y), entity.dungeonLevel)) {
			entity.setPos(deltaX + x, deltaY + y);
		} else if (!dm.system.blockIDMap.get(dm.world.getStructBlock((int)(deltaX + x), (int)(deltaY + y), entity.dungeonLevel)).isSolid) {
			entity.setPos(deltaX + x, deltaY + y);
		}
	}
	public void run() {
		while(dm.system.running) {
			try {
				for (int i = 0; i < dm.savable.entities.size(); i++) {
					if (dm.savable.entities.get(i).dungeonLevel == dm.characterManager.characterEntity.dungeonLevel) {
						if (dm.savable.entities.get(i).distanceToPlayer() < 100) {
							dm.savable.entities.get(i).update();
							if ( i < dm.savable.entities.size() - 1) {
								for (int j = i + 1; j < dm.savable.entities.size(); j++) {
									Entity entity1 = dm.savable.entities.get(i);
									Entity entity2 = dm.savable.entities.get(j);
									if (Math.sqrt(Math.pow(entity1.posX-entity2.posX,2) + Math.pow(entity1.posY - entity2.posY, 2)) < 1) {
										double midX = (entity1.getX() + entity2.getX())/2.0;
										double midY = (entity1.getY() + entity2.getY())/2.0;
										double theta = Math.atan2(entity1.getY() - midY, entity1.getX() - midX);
										moveEntity(entity1, 0.5, theta, midX, midY);
										moveEntity(entity2, -0.5, theta, midX, midY);
									}
								}
							}
							if (dm.savable.entities.get(i).health <= 0) {
								if (dm.savable.entities.get(i).entityType != Entity.EntityType.character) {
									dm.savable.entities.get(i).death();
									dm.savable.entities.remove(i);
									i--;
								} else {
									dm.system.noticeText.add("Looks like you died... Oops");
									dm.characterManager.characterEntity.health = dm.characterManager.characterEntity.maxHealth;
									for (int j = 0; j < dm.savable.entities.size(); j++) {
										if (dm.savable.entities.get(j).entityType != Entity.EntityType.character) {
											dm.savable.entities.remove(j);
											j--;
										}
									}
								}
							}
						}
					}
				}
				Thread.sleep(dm.settings.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
