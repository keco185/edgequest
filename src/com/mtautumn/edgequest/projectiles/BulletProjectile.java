package com.mtautumn.edgequest.projectiles;

import com.mtautumn.edgequest.Entity;

public class BulletProjectile extends Projectile {
	private static final long serialVersionUID = 1L;

	public BulletProjectile(double speed, double range, Entity entity, int damage) {
		super();
		this.speed = speed;
		maxIncrement = range;
		firedBy = entity;
		this.damage = damage;
		angle = -entity.getRot();
		startX = entity.getX();
		startY = entity.getY();
		x = startX;
		y = startY;
		level = entity.dungeonLevel;
		texture = new String[]{"bullet0"};
	}
	public BulletProjectile(double speed, double range, Entity entity, int damage, double offsetX, double offsetY) {
		super();
		this.speed = speed;
		maxIncrement = range;
		firedBy = entity;
		this.damage = damage;
		angle = -entity.getRot();
		startX = entity.getX() + offsetX;
		startY = entity.getY() + offsetY;
		x = startX;
		y = startY;
		level = entity.dungeonLevel;
		texture = new String[]{"bullet0"};
	}
	
	protected double[] increment(double newIncrement) {
		double[] newLocation = new double[2];
		newLocation[0] = startX + Math.cos(angle) * speed * newIncrement;
		newLocation[1] = startY - Math.sin(angle) * speed * newIncrement;
		return newLocation;
	}
}
