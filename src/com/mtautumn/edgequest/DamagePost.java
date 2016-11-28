package com.mtautumn.edgequest;

import java.io.Serializable;

public class DamagePost implements Serializable {
	private static final long serialVersionUID = 1L;
	double posX;
	double posY;
	public int level;
	public int dungeonX;
	public int dungeonY;
	public long postTime;
	public int damage;
	public DamagePost(Entity entity, int damage) {
		posX = entity.posX;
		posY = entity.posY;
		level = entity.dungeonLevel;
		dungeonX = entity.dungeon[0];
		dungeonY = entity.dungeon[1];
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
