package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;

public class RainParticle extends Particle {
	double speedX,speedY;
	public RainParticle(double x, double y, int level, double speedX, double speedY, double width, double height) {
		super(x, y, level, width, height, "raindrop");
		this.speedX = speedX;
		this.speedY = speedY;
	}
	public boolean update(DataManager dm) {
		x += speedX;
		y += speedY;
		return (y > dm.system.maxTileY);
	}

}
