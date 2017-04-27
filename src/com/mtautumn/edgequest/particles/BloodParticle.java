package com.mtautumn.edgequest.particles;

import com.mtautumn.edgequest.data.DataManager;

public class BloodParticle extends Particle {
	double startX;
	double startY;
	double angle;
	double speed;
	double finalY;
	double height = 0;
	double verticalSpeed;
	double speedXInitial;
	double speedYInitial;
	private int disappearTime;
	private int time = 0;
	public BloodParticle(double x, double y, int level, double speedXInitial, double speedYInitial, double width, double height) {
		super(x, y, level, width, height, "blood");
		angle = Math.random() * 2.0 * Math.PI - Math.PI;
		speed = Math.pow(Math.random() * 0.5 + 0.2, 3) * 5.0;
		verticalSpeed = speed * Math.random() / 15.0;
		finalY = Math.random() * 3.0 - 1.5 + y;
		startX = x;
		startY = y;
		double randMult = Math.pow(Math.random(),2);
		this.speedXInitial = randMult * speedXInitial/10.0;
		this.speedYInitial = randMult * speedYInitial/10.0;
		disappearTime = (int) (3600 + Math.random() * 3600);
	}
	@Override
	public boolean update(DataManager dm) {
		time++;
		if (disappearTime < time) {
			return true;
		}
		speed *= 0.4;
		speedXInitial *= 0.4;
		speedYInitial *= 0.4;
		if (checkMove(x + Math.cos(angle) * speed + speedXInitial, y, dm)) {
			x += Math.cos(angle) * speed + speedXInitial;
		} else {
			x = Math.round(x);
		}
		if (checkMove(x, y + Math.sin(angle) * speed + speedYInitial, dm)) {
			y += Math.sin(angle) * speed + speedYInitial;
		} else {
			y = Math.round(y);
		}
		if (checkMove(x, y - verticalSpeed, dm) && Math.abs(verticalSpeed) > 0.001) {
			height += verticalSpeed;
			y -= verticalSpeed;
		}
		if (height < 0) {
			if (dm.world.getGroundBlock((int) x, (int) y, level) == dm.system.blockNameMap.get("water").getID()) {
				return true;
			}
			if (Math.abs(verticalSpeed) < 0.001) {
				verticalSpeed = 0;
			} else {
				verticalSpeed = -verticalSpeed / 2.0;
				if (checkMove(x,y + height, dm)) {
					y += height;
				}
				height = 0;
			}
		} else if (height > 0){
			verticalSpeed -= 0.01;
		}
		return false;
	}

}
