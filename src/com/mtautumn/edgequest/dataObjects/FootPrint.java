// Defines what a footprint is and its location
package com.mtautumn.edgequest.dataObjects;

import java.io.Serializable;

public class FootPrint implements Serializable {
	private static final long serialVersionUID = 1L;
	public double posX;
	public double posY;
	public double opacity;
	public double direction;
	public int level;
	public FootPrint(double posX, double posY, double direction, int level) {
		this.posX = posX;
		this.posY = posY;
		this.direction = direction;
		this.level = level;
		opacity = 1.0;
	}
}
