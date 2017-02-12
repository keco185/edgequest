package com.mtautumn.edgequest.dataObjects;

import java.util.ArrayList;

import com.mtautumn.edgequest.updates.UpdateRayCast.Triangle;

public class LightSource {
	public double posX;
	public double posY;
	public int level;
	public final double range;
	public ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	public float r = 1.0f;
	public float g = 0.95f;
	public float b = 0.8f;
	public boolean onEntity = false;
	public LightSource(double posX, double posY, double range, int level) {
		this.posX = posX;
		this.posY = posY;
		this.range = range;
		this.level = level;
	}
	public void assignColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public void removeZeroTriangles() {
		for (int i = 0; i < triangles.size(); i++) {
			if (triangles.get(i).isZeroTriangle())
				triangles.remove(i);
		}
	}
}
