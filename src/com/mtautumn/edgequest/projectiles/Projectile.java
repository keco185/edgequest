//Class is designed to be extended with each type of projectile
//New initializers for the extended class should be made with
//Relevant variables. Speed, maxIncrement, damage, texture,
//startX,startY,level,hitsLeft,and firedBy should be overwritten
//Angle is optional but may be useful for saving the entity's
//orientation when the projectile was fired/created
package com.mtautumn.edgequest.projectiles;

import java.io.Serializable;
import java.util.ArrayList;

import com.mtautumn.edgequest.DamagePost;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.entities.Pet;
import com.mtautumn.edgequest.particles.BloodParticle;

public class Projectile implements Serializable {
	private static final long serialVersionUID = 1L;
	public double speed; //Speed at which position of projectile incrememnts
	public double angle; //Starting angle of projectile
	public double maxIncrement; //Max increment value before projectile vanishes
	public int damage; //Damage delt by projectile
	public String[] texture; //Image of projectile
	protected double startX; //Initial position of projectile (x axis)
	protected double startY; //Initial position of projectile (y axis)
	public double x; //Current position of projectile (x axis)
	public double y; //Current position of projectile (y axis)
	public double increment = 0; //Value of projectile position increment
	public int level; //dungeon level of projectile
	public int hitsLeft; //Entity hits before projectile vanishes
	public Entity firedBy; //Entity that fired projectile (cannot be hurt by it)
	public ArrayList<Entity> hitEntities = new ArrayList<Entity>();
	public Projectile(double speed, double angle, double maxDistance, int damage, String texture, double startX, double startY, int level, Entity firedBy) {
		this.speed = speed;
		this.angle = angle;
		this.maxIncrement = maxDistance;
		this.startX = startX;
		this.startY = startY;
		this.x = startX;
		this.y = startY;
		this.damage = damage;
		this.texture = new String[]{texture};
		this.level = level;
		this.firedBy = firedBy;
	}
	public Projectile() {
		speed = 1;
		angle = 1;
		maxIncrement = 1;
		startX = 0;
		startY = 0;
		x = startX;
		y = startY;
		damage = 0;
		texture = new String[]{"none"};
		level = -1;
		firedBy = null;
		hitsLeft = 0;
	}
	protected double[] increment(double newIncrement) { //Override with movement style of projectile
		double[] newLocation = new double[2];
		newLocation[0] = startX + Math.cos(angle) * speed * newIncrement;
		newLocation[1] = startY - Math.sin(angle) * speed * newIncrement;
		return newLocation;
	}
	public boolean advance() { //Returns true if projectile should be removed
		double deltaIncrement = new Double(SettingsData.tickLength / 2)/1000.0;
		double newX = increment(increment + deltaIncrement)[0];
		double newY = increment(increment + deltaIncrement)[1];
		for (double i = increment; i <= deltaIncrement + increment; i += 0.005) {
			double temp[] = increment(i);
			if (isInEntity(temp[0], temp[1])) {
				addHitEntity(temp[0], temp[1]);
				hitsLeft--;
				if (hitsLeft < 1) {
					x = temp[0];
					y = temp[1];
					return true;
				}
			}
		}
		x = newX;
		y = newY;
		increment += deltaIncrement;
		return false;
	}
	public void manipulateHitEntity(Entity entity) { //Run on entities hit by projectile: Should be overwritten
		
	}
	protected boolean isInEntity(double x, double y) {
		for (int i = 0; i < DataManager.savable.entities.size(); i++) {
			Entity entity = DataManager.savable.entities.get(i);
			if (checkEntity(entity, x, y)) {
				return true;
			}
		}
		return false;
	}
	public boolean inStructure() {
		Location location = new Location((int) x, (int) y, level);
		if (DataManager.world.isStructBlock(location)) {
			return !SystemData.blockIDMap.get(DataManager.world.getStructBlock(location)).isPassable;
		}
		return false;
	}
	protected void addHitEntity(double x, double y) {
		for (int i = 0; i < DataManager.savable.entities.size(); i++) {
			Entity entity = DataManager.savable.entities.get(i);
			if (checkEntity(entity, x, y)) {
				manipulateHitEntity(entity);
				hitEntities.add(entity);
				if (firedBy.getType() == Entity.EntityType.character) {
					for (Entity testEntity : DataManager.savable.entities) {
						if (testEntity.getType() == Entity.EntityType.pet) {
							((Pet) testEntity).attackEntity(entity);
						}
					}

				}
				entity.health -= damage;
				double damageVal = damage;
				if (entity.health <= 0) {
					damageVal += entity.health;
					entity.health = 0;
					DataManager.savable.damagePosts.add(new DamagePost(entity, (int) damageVal));
					for (int k = 0; k < 20; k++) {
						DataManager.savable.particles.add(new BloodParticle(entity.getX(), entity.getY(), entity.dungeonLevel, speed * Math.cos(angle), speed * -Math.sin(angle), 0.2, 0.2));
					}
					DataManager.savable.entities.get(i).death();
					DataManager.savable.entities.remove(i);
					i--;
				} else {
					DataManager.savable.damagePosts.add(new DamagePost(entity, (int) damageVal));
				}
			}
		}
	}
	protected boolean checkEntity(Entity entity, double x, double y) {
		if (entity == firedBy) {
			return false;
		}
		for (int i = 0; i < hitEntities.size(); i++) {
			if (entity == hitEntities.get(i)) {
				return false;
			}
		}
		double minX = entity.getX() - 0.5;
		double minY = entity.getY() - 0.5;
		double maxX = entity.getX() + 0.5;
		double maxY = entity.getY() + 0.5;
		if (entity.dungeonLevel == -1) {
			return minX <= x && minY <= y && maxX >= x && maxY >= y && entity.dungeonLevel == level;
		}
		return minX <= x && minY <= y && maxX >= x && maxY >= y && entity.dungeonLevel == level;
	}
	public Entity getEntityIn(DataManager dm) {
		for (int i = 0; i < DataManager.savable.entities.size(); i++) {
			Entity entity = DataManager.savable.entities.get(i);
			if (checkEntity(entity, x, y)) {
				return entity;
			}
		}
		return null;
	}
	public String getTexture() {
		return texture[SystemData.animationClock%texture.length];
	}
}
