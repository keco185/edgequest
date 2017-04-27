package com.mtautumn.edgequest.dataObjects;

import java.io.Serializable;

public class Triangle implements Serializable {
	private static final long serialVersionUID = 1L;
	public double angle1;
	public double angle2;
	public double radius1;
	public double radius2;
	public double originX;
	public double originY;
	public double x1;
	public double x2;
	public double x3;
	public double y1;
	public double y2;
	public double y3;
	public void setCartesian(double originX, double originY, double p1x, double p1y, double p2x, double p2y) {
		x1 = originX;
		x2 = p1x;
		x3 = p2x;
		y1 = originY;
		y2 = p1y;
		y3 = p2y;
		angle1 = Math.atan2(originY - p1y, p1x-originX);
		angle2 = Math.atan2(originY - p2y, p2x-originX);
		radius1 = Math.sqrt(Math.pow(p1x-originX, 2)+Math.pow(p1y-originY, 2));
		radius2 = Math.sqrt(Math.pow(p2x-originX, 2)+Math.pow(p2y-originY, 2));
		this.originX = originX;
		this.originY = originY;
	}
	public Triangle(double originX, double originY, double p1Angle, double p1Radius, double p2Angle, double p2Radius) {
		this.originX = originX;
		this.originY = originY;
		this.angle1 = p1Angle;
		this.angle2 = p2Angle;
		this.radius1 = p1Radius;
		this.radius2 = p2Radius;
		x1 = originX;
		y1 = originY;
		y2 = y1 - Math.sin(p1Angle) * p1Radius;
		x2 = x1 + Math.cos(p1Angle) * p1Radius;
		y3 = y1 - Math.sin(p2Angle) * p2Radius;
		x3 = x1 + Math.cos(p2Angle) * p2Radius;
	}
	public Triangle() {

	}
	public double getSmallestAngle() {
		if (angle1 < angle2) {
			return angle1;
		}
		return angle2;
	}
	public double getLargestAngle() {
		if (angle2 < angle1) {
			return angle1;
		}
		return angle2;
	}
	public void rotateSide(int line, double x, double y, boolean adjustLength) {
		double angle = Math.atan2(originY - y, x-originX);
		if (line == 1) {
			angle1 = angle;
		} else {
			angle2 = angle;
		}
		if (adjustLength) {
			double length = Math.sqrt(Math.pow(x-originX, 2)+Math.pow(y-originY, 2));
			if (line == 1) {
				radius1 = length;
			} else {
				radius2 = length;
			}
		}
	}
	public boolean isZeroTriangle() {
		return angle1 == angle2;
	}
}