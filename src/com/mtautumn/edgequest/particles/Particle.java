package com.mtautumn.edgequest.particles;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.utils.WorldUtils;
import com.mtautumn.edgequest.window.Renderer;

public class Particle {
	public double x;
	public double y;
	public int level;
	public double opacity = 1.0;
	public double width;
	public double height;
	public String texture;
	public Particle(double x, double y, int level, double width, double height, String texture) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}
	public boolean update() { //Should be overwritten Returns true if should be deleted
		return false;
	}
	protected boolean checkMove(double newX, double newY) { //Returns true if location is free
		if (WorldUtils.isStructBlock((int)newX, (int)newY, level)) {
			if (SystemData.blockIDMap.get(WorldUtils.getStructBlock((int)newX, (int)newY, level)).isPassable) {
				return true;
			}
			return false;
		}
		return true;
	}
	public Texture getTexture(Renderer r) {
		return r.textureManager.getTexture("particles." + texture);
	}
}
