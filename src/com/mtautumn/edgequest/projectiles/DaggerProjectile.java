package com.mtautumn.edgequest.projectiles;

import com.mtautumn.edgequest.entities.Entity;

public class DaggerProjectile extends Projectile {
	private static final long serialVersionUID = 1L;
	private boolean returns = false;
	public DaggerProjectile(double speed, double range, Entity entity, int damage, boolean returns, int maxHits) {
		super();
		this.speed = speed;
		this.returns = returns;
		if (returns) {
			maxIncrement = 2.0 * range / speed;
		} else {
			maxIncrement = range / speed;
		}
		firedBy = entity;
		this.damage = damage;
		startX = entity.getX();
		startY = entity.getY();
		x = startX;
		y = startY;
		level = entity.dungeonLevel;
		texture = new String[]{"dagger0"};
		hitsLeft = maxHits;
	}
	public DaggerProjectile(double speed, double range, Entity entity, int damage, double offsetX, double offsetY, boolean returns, int maxHits) {
		super();
		this.speed = speed;
		this.returns = returns;
		if (returns) {
			maxIncrement = 2.0 * range / speed;
		} else {
			maxIncrement = range / speed;
		}
		firedBy = entity;
		this.damage = damage;
		angle = -entity.getRot();
		startX = entity.getX() + offsetX;
		startY = entity.getY() + offsetY;
		level = entity.dungeonLevel;
		texture = new String[]{"dagger0"};
		x = startX;
		y = startY;
		hitsLeft = maxHits;
	}
	public DaggerProjectile(double speed, double range, Entity entity, int damage, double offsetX, double offsetY, boolean returns, int maxHits, double angle) {
		super();
		this.speed = speed;
		this.returns = returns;
		if (returns) {
			maxIncrement = 2.0 * range / speed;
		} else {
			maxIncrement = range / speed;
		}
		firedBy = entity;
		this.damage = damage;
		this.angle = angle;
		startX = entity.getX() + offsetX;
		startY = entity.getY() + offsetY;
		level = entity.dungeonLevel;
		texture = new String[]{"dagger0"};
		x = startX;
		y = startY;
		hitsLeft = maxHits;
	}
	
	protected double[] increment(double newIncrement) {
		if (returns && newIncrement > maxIncrement / 2.0) {
			double[] newLocation = new double[2];
			newLocation[0] = startX + Math.cos(angle) * speed * (maxIncrement - newIncrement);
			newLocation[1] = startY - Math.sin(angle) * speed * (maxIncrement - newIncrement);
			return newLocation;
		}
			double[] newLocation = new double[2];
			newLocation[0] = startX + Math.cos(angle) * speed * newIncrement;
			newLocation[1] = startY - Math.sin(angle) * speed * newIncrement;
			return newLocation;
	}
}
