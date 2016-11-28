package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class Projectile {
	private double speed;
	public double angle;
	public double maxDistance;
	public double damage;
	public String texture;
	private double startX;
	private double startY;
	public double x;
	public double y;
	public int dungeonX;
	public int dungeonY;
	public int level;
	public Entity firedBy;
	public Projectile(double speed, double angle, double maxDistance, double damage, String texture, double startX, double startY, int dungeonX, int dungeonY, int level, Entity firedBy) {
		this.speed = speed;
		this.angle = angle;
		this.maxDistance = maxDistance;
		this.startX = startX;
		this.startY = startY;
		this.x = startX;
		this.y = startY;
		this.damage = damage;
		this.texture = texture;
		this.dungeonX = dungeonX;
		this.dungeonY = dungeonY;
		this.level = level;
		this.firedBy = firedBy;
	}
	public double distance() {
		return Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
	}
	public boolean advance(DataManager dm) {
		double newX = x + Math.cos(angle) * speed;
		double newY = y - Math.sin(angle) * speed;
		for (double i = 0; i <= speed; i += 0.01) {
			double tempX = x + Math.cos(angle) * i;
			double tempY = y - Math.sin(angle) * i;
			if (isInEntity(dm, tempX, tempY)) {
				x = tempX;
				y = tempY;
				return true;
			}
		}
		x = newX;
		y = newY;
		return false;
	}
	private boolean isInEntity(DataManager dm, double x, double y) {
		for (int i = 0; i < dm.savable.entities.size(); i++) {
			Entity entity = dm.savable.entities.get(i);
			if (checkEntity(entity, x, y)) {
				return true;
			}
		}
		return false;
	}
	private boolean checkEntity(Entity entity, double x, double y) {
		if (entity == firedBy) return false;
		double minX = entity.getX() - 0.5;
		double minY = entity.getY() - 0.5;
		double maxX = entity.getX() + 0.5;
		double maxY = entity.getY() + 0.5;
		return minX <= x && minY <= y && maxX >= x && maxY >= y;
	}
	public Entity getEntityIn(DataManager dm) {
		for (int i = 0; i < dm.savable.entities.size(); i++) {
			Entity entity = dm.savable.entities.get(i);
			if (checkEntity(entity, x, y)) {
				return entity;
			}
		}
		return null;
	}
}
