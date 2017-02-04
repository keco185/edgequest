package com.mtautumn.edgequest;

import java.io.Serializable;

import com.mtautumn.edgequest.entities.Entity;

public class DamagePost implements Serializable {
	private static final long serialVersionUID = 1L;
	double posX;
	double posY;
	public int level;
	public long postTime;
	public int damage;
	public DamagePost(Entity entity, int damage) {
		posX = entity.posX;
		posY = entity.posY;
		level = entity.dungeonLevel;
		postTime = System.currentTimeMillis();
		this.damage = damage;
	}
	public double getX() {
		return posX;
	}
	public double getY() {
		return posY - Double.valueOf(System.currentTimeMillis() - postTime) / 1000.0;
	}
}
