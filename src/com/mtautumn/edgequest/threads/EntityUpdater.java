package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.utils.WorldUtils;

public class EntityUpdater extends Thread{

	private static void moveEntity(Entity entity, double r, double theta, double x, double y) {
		double deltaX = Math.cos(theta) * r;
		double deltaY = Math.sin(theta) * r;
		if (!WorldUtils.isStructBlock((int)(deltaX + x), (int)(deltaY + y), entity.dungeonLevel)) {
			entity.setPos(deltaX + x, deltaY + y);
		} else if (!SystemData.blockIDMap.get(WorldUtils.getStructBlock((int)(deltaX + x), (int)(deltaY + y), entity.dungeonLevel)).isSolid) {
			entity.setPos(deltaX + x, deltaY + y);
		}
	}
	@Override
	public void run() {
		while(SystemData.running) {
			try {
				if (!SystemData.isGameOnLaunchScreen) {
					for (int i = 0; i < DataManager.savable.entities.size(); i++) {
						if (DataManager.savable.entities.get(i).dungeonLevel == CharacterManager.characterEntity.dungeonLevel) {
							if (DataManager.savable.entities.get(i).distanceToPlayer() < 100) {
								DataManager.savable.entities.get(i).update();
								if ( i < DataManager.savable.entities.size() - 1) {
									for (int j = i + 1; j < DataManager.savable.entities.size(); j++) {
										Entity entity1 = DataManager.savable.entities.get(i);
										Entity entity2 = DataManager.savable.entities.get(j);
										if (Math.sqrt(Math.pow(entity1.posX-entity2.posX,2) + Math.pow(entity1.posY - entity2.posY, 2)) < 1) {
											double midX = (entity1.getX() + entity2.getX())/2.0;
											double midY = (entity1.getY() + entity2.getY())/2.0;
											double theta = Math.atan2(entity1.getY() - midY, entity1.getX() - midX);
											moveEntity(entity1, 0.5, theta, midX, midY);
											moveEntity(entity2, -0.5, theta, midX, midY);
										}
									}
								}
								if (DataManager.savable.entities.get(i).health <= 0) {
									if (DataManager.savable.entities.get(i).entityType != Entity.EntityType.character) {
										DataManager.savable.entities.get(i).death();
										DataManager.savable.entities.remove(i);
										i--;
									}
								}
							}
						}
					}
					if (CharacterManager.characterEntity.health <= 0) {
						SystemData.noticeText.add("Looks like you died... Oops");
						CharacterManager.characterEntity.health = CharacterManager.characterEntity.maxHealth;
						for (int j = 0; j < DataManager.savable.entities.size(); j++) {
							if (DataManager.savable.entities.get(j).entityType != Entity.EntityType.pet) {
								DataManager.savable.entities.remove(j);
								j--;
							}
						}
						DataManager.savable.itemDrops.clear();
						DataManager.savable.entities.add(CharacterManager.characterEntity);
					}
				}
				Thread.sleep(SettingsData.tickLength);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
