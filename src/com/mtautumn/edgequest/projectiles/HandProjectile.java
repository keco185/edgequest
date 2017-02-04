package com.mtautumn.edgequest.projectiles;

import com.mtautumn.edgequest.entities.Entity;

public class HandProjectile extends Projectile {
	private static final long serialVersionUID = 1L;

	public HandProjectile(Entity entity, int damage) {
		super();
		this.speed = 11;
		maxIncrement = 0.5 / 11.0;
		firedBy = entity;
		this.damage = damage;
		angle = -entity.getRot();
		startX = entity.getX();
		startY = entity.getY();
		x = startX;
		y = startY;
		level = entity.dungeonLevel;
		texture = new String[]{"none"};
	}
	public HandProjectile(Entity entity, int damage, double offsetX, double offsetY) {
		super();
		this.speed = 11;
		maxIncrement = 0.5 / 11.0;
		firedBy = entity;
		this.damage = damage;
		angle = -entity.getRot();
		startX = entity.getX() + offsetX;
		startY = entity.getY() + offsetY;
		x = startX;
		y = startY;
		level = entity.dungeonLevel;
		texture = new String[]{"none"};
	}
	
	protected double[] increment(double newIncrement) {
		double[] newLocation = new double[2];
		newLocation[0] = startX + Math.cos(angle) * speed * newIncrement;
		newLocation[1] = startY - Math.sin(angle) * speed * newIncrement;
		return newLocation;
	}
}
