package com.mtautumn.edgequest.particles;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.dataObjects.Location;

public class PrecipitationParticle extends Particle {
	double speedX,speedY;
	public PrecipitationParticle(double x, double y, int level, double speedX, double speedY, double width, double height) {
		super(x, y, level, width, height, "raindrop");
		this.speedX = speedX;
		this.speedY = speedY;
	}
	public boolean update(DataManager dm) {
		Location locale = new Location((int)x, (int)y, level);
		if(dm.world.isGroundBlock(locale)) {
			BlockItem block = dm.system.blockIDMap.get(dm.world.getGroundBlock(locale));
			if (block.isName("snow") || block.isName("ice")) {
				super.texture = "snow";
				x += speedX / 6.0;
				y += speedY / 6.0;
			} else {
				super.texture = "raindrop";
				x += speedX;
				y += speedY;
			}
		}
		return (y > dm.system.maxTileY);
	}

}
