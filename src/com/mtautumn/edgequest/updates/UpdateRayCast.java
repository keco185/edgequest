/* Not currently used in-game. An attempt at non-voxel raycasts.
 * Needs to be implemented.
 */
package com.mtautumn.edgequest.updates;

import java.util.ArrayList;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.LightSource;
import com.mtautumn.edgequest.dataObjects.Location;
import com.mtautumn.edgequest.dataObjects.Triangle;
import com.mtautumn.edgequest.utils.WorldUtils;


public class UpdateRayCast {
	public ArrayList<LightSource> lightSources;
	public ArrayList<Line> lines = new ArrayList<Line>();
	public UpdateRayCast() {
		lightSources = DataManager.savable.lightSources;
	}
	public void update() {

	}
	public void update(Location location) {
		for (LightSource light : lightSources) {
			if (Math.sqrt(Math.pow(light.posX - Double.valueOf(location.x), 2) + Math.pow(light.posY - Double.valueOf(location.y), 2)) < light.range) {
				updateLightSource(light);
			}
		}
	}
	public void update(LightSource light) {
		updateLightSource(light);
	}
	public class Point {
		public double angle;
		public double radius;
		public Point(double angle, double radius) {
			this.angle = angle;
			this.radius = radius;
		}
	}

	private void addVertex(double angle, double radius, double range, ArrayList<Point> points, ArrayList<Line> lines, LightSource light) {
		double correctedAngle = angle;
		double correctedRadius = radius;
		if (correctedAngle > Math.PI) {
			correctedAngle = Math.PI;
		}
		if (correctedAngle < -Math.PI) {
			correctedAngle = -Math.PI;
		}
		if (correctedRadius < 0) {
			correctedRadius = 0;
		}
		if (correctedRadius > range) {
			correctedRadius = range;
		}
		double dX = Math.cos(angle) * radius;
		double dY = Math.sin(angle) * radius;
		double x = light.posX + dX;
		double y = light.posY - dY;
		for (int i = 0; i < lines.size(); i++) {
			if (linesIntersect(light.posX, light.posY, x, y, lines.get(i).x1, lines.get(i).y1, lines.get(i).x2, lines.get(i).y2)) {
				CartesianPoint intersection = getLineLineIntersection(light.posX, light.posY, x, y, lines.get(i).x1, lines.get(i).y1, lines.get(i).x2, lines.get(i).y2);
				double intersectionRadius = Math.sqrt(Math.pow(intersection.x - light.posX, 2) + Math.pow(intersection.y - light.posY, 2));
				if (!Double.isNaN(intersectionRadius)) {
					if (intersectionRadius < correctedRadius) {
						correctedRadius = intersectionRadius;
					}
				}
				x = light.posX + Math.cos(angle) * radius;
				y = light.posY - Math.sin(angle) * radius;
			}
		}
		if ((correctedRadius > radius - 0.1) || radius >= light.range) {
			points.add(new Point(correctedAngle, correctedRadius));
		}
	}
	public class CartesianPoint {
		double x,y;
		public CartesianPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
		// Return false if either of the lines have zero length
		if (x1 == x2 && y1 == y2 ||
				x3 == x4 && y3 == y4){
			return false;
		}
		// Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
		double ax = x2-x1;
		double ay = y2-y1;
		double bx = x3-x4;
		double by = y3-y4;
		double cx = x1-x3;
		double cy = y1-y3;

		double alphaNumerator = by*cx - bx*cy;
		double commonDenominator = ay*bx - ax*by;
		if (commonDenominator > 0){
			if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
				return false;
			}
		}
		double betaNumerator = ax*cy - ay*cx;
		if (commonDenominator > 0){
			if (betaNumerator < 0 || betaNumerator > commonDenominator){
				return false;
			}
		}else if (commonDenominator < 0){
			if (betaNumerator > 0 || betaNumerator < commonDenominator){
				return false;
			}
		}
		if (commonDenominator == 0){
			// This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
			// The lines are parallel.
			// Check if they're collinear.
			double y3LessY1 = y3-y1;
			double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
			// If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
			if (collinearityTestForP3 == 0){
				// The lines are collinear. Now check if they overlap.
				if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
						x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
						x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
					if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
							y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
							y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}
	public CartesianPoint getLineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		double det1And2 = det(x1, y1, x2, y2);
		double det3And4 = det(x3, y3, x4, y4);
		double x1LessX2 = x1 - x2;
		double y1LessY2 = y1 - y2;
		double x3LessX4 = x3 - x4;
		double y3LessY4 = y3 - y4;
		double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
		if (det1Less2And3Less4 == 0){
			// the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
			return null;
		}
		double x = (det(det1And2, x1LessX2,
				det3And4, x3LessX4) /
				det1Less2And3Less4);
		double y = (det(det1And2, y1LessY2,
				det3And4, y3LessY4) /
				det1Less2And3Less4);
		return new CartesianPoint(x, y);
	}
	protected static double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}
	private void updateLightSource(LightSource light) {
		ArrayList<Line> rawLines = new ArrayList<Line>();
		createLines(light.posX, light.posY, light.range, light.level, rawLines);
		ArrayList<Line> lines = new ArrayList<Line>();

		for (int i = 0; i < rawLines.size(); i++) {
			if (rawLines.get(i).isInRange(light.posX, light.posY, light.range)) {
				lines.add(rawLines.get(i));
			}
		}




		ArrayList<Point> points = new ArrayList<Point>();

		//Adds points for ends of each line segment
		for (int i = 0; i < lines.size(); i++) {
			double angle1 = Math.atan2(light.posY-lines.get(i).y1, lines.get(i).x1 - light.posX);
			double radius1 = Math.sqrt(Math.pow(light.posY-lines.get(i).y1, 2) + Math.pow(light.posX - lines.get(i).x1, 2));
			double angle2 = Math.atan2(light.posY-lines.get(i).y2, lines.get(i).x2 - light.posX);
			double radius2 = Math.sqrt(Math.pow(light.posY-lines.get(i).y2, 2) + Math.pow(light.posX - lines.get(i).x2, 2));
			addVertex(angle1, radius1, light.range, points, lines, light);
			addVertex(angle2, radius2, light.range, points, lines, light);
		}

		//Remove points that have a closer version
		int currentPoint = 0;
		while (currentPoint < points.size()) {
			boolean removePoint = false;
			for (int i = 0; i < points.size(); i++) {
				if (points.get(i).angle == points.get(currentPoint).angle) {
					if (points.get(i).radius < points.get(currentPoint).radius) {
						removePoint = true;
					}
				}
			}
			if (removePoint) {
				points.remove(currentPoint);
			} else {
				currentPoint++;
			}
		}


		double angle = -Math.PI;
		double lastRadius = light.range;
		final double increment;
		if (SettingsData.fastGraphics) {
			increment = 0.25;
		} else {
			increment = 0.08;
		}
		points.sort((p1,p2) -> (int)((p1.angle - p2.angle)*341782637.788216));
		ArrayList<Point> pointArray = new ArrayList<Point>();
		pointArray.add(new Point(angle, lastRadius));
		while(angle < Math.PI) {
			if (points.size() > 0) {
				if (points.get(0).angle > angle + increment && angle + increment <= Math.PI) {
					pointArray.add(new Point(angle + increment - 0.0000000001, light.range));
					angle += increment;
				} else {
					pointArray.add(new Point(points.get(0).angle - 0.0000000001, pointArray.get(pointArray.size() - 1).radius));
					pointArray.add(points.get(0));
					angle = points.get(0).angle;
					points.remove(0);
					pointArray.add(new Point(angle + 0.0000000001, light.range));
				}
			} else {
				if (angle + increment <= Math.PI) {
					pointArray.add(new Point(angle + increment, light.range));
					angle += increment;
				} else {
					angle = Math.PI;
					pointArray.add(new Point(angle, light.range));
				}
			}
		}

		ArrayList<Point> finalizedPoints = new ArrayList<Point>();
		for (int i = 0; i < pointArray.size(); i++) {
			addVertex(pointArray.get(i).angle, pointArray.get(i).radius, light.range, finalizedPoints, lines, light);
		}

		finalizedPoints.sort((p1,p2) -> (int)((p1.angle - p2.angle)*341782637.788216));
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		for (int i = 0; i < finalizedPoints.size() - 1; i++) {
			Point pt1 = finalizedPoints.get(i);
			Point pt2 = finalizedPoints.get(i + 1);
			Triangle triangle = new Triangle(light.posX, light.posY, pt1.angle, pt1.radius, pt2.angle, pt2.radius);
			triangles.add(triangle);
		}
		while (light.inUse) {
			try {
				Thread.sleep(0, 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		light.triangles = triangles;

	}
	private void createLines(double x, double y, double range, int level, ArrayList<Line> lines) {
		for (double x1 = x - range; x1 <= x + range; x1++) {
			for (double y1 = y - range; y1 <= y + range; y1++) {
				if (doesContainStructure(x1, y1, level)) {
					Line line1 = new Line(Math.floor(x1), Math.floor(y1), Math.ceil(x1), Math.floor(y1));
					Line line2 = new Line(Math.floor(x1), Math.floor(y1), Math.floor(x1), Math.ceil(y1));
					Line line3 = new Line(Math.floor(x1), Math.ceil(y1), Math.ceil(x1), Math.ceil(y1));
					Line line4 = new Line(Math.ceil(x1), Math.floor(y1), Math.ceil(x1), Math.ceil(y1));
					lines.add(line1);
					lines.add(line2);
					lines.add(line3);
					lines.add(line4);
				}
			}
		}
	}
	public boolean doesContainStructure(double x, double y, int level) {
		int x1 = (int) Math.floor(x);
		int y1 = (int) Math.floor(y);
		if (WorldUtils.isStructBlock(x1, y1, level)) {
			return !SystemData.blockIDMap.get(WorldUtils.getStructBlock(x1, y1, level)).isPassable;
		}
		return false;
	}
	public void addLightSource(double x, double y) {

	}
	public class Line {
		public double x1;
		public double x2;
		public double y1;
		public double y2;
		public Line(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}
		public boolean isInRange(double x, double y, double radius) {
			double dist1 = Math.sqrt(Math.pow(x-x1, 2) + Math.pow(y-y1, 2));
			double dist2 = Math.sqrt(Math.pow(x-x2, 2) + Math.pow(y-y2, 2));
			return (dist1 <= radius || dist2 <= radius);
		}
		public boolean isLineHorizontal() {
			return (y1 == y2);
		}
		public boolean isLineVertical() {
			return (x1 == x2);
		}
		public boolean isIntersection(double x, double y, double angle, double radius) {
			double angle1 = getPolarAngle(x, y, 1);
			double angle2 = getPolarAngle(x, y, 2);
			if (getDistance(x, y, 1) <= radius || getDistance(x, y, 2) <= radius) {
				return (angle2 <= angle && angle1 >= angle) || (angle1 <= angle && angle2 >= angle);
			}
			return false;
		}
		private double getDistance(double x, double y, int point) {
			if (point == 1) {
				return Math.sqrt(Math.pow(x-x1, y-y1));
			}
			return Math.sqrt(Math.pow(x-x2, y-y2));
		}
		public double getPolarAngle(double x, double y, int point) {
			if (point == 1) {
				return Math.atan2(y - y1, x1-x);
			}
			return Math.atan2(y - y2, x2-x);
		}
	}
}
