/* A* algorithm for determining optimal path. Called by creating an instance
 * then running the findPath method which returns a series of nodes to follow
 */
package com.mtautumn.edgequest.utils;

import java.io.Serializable;
import java.util.ArrayList;

import com.mtautumn.edgequest.data.SystemData;

public class PathFinder {
	public ArrayList<IntCoord> findPath(int startX, int startY, int endX, int endY, int level) {
		int iterationCount = 0;
		IntCoord start = new IntCoord(startX, startY);
		IntCoord end = new IntCoord(endX, endY);
		ArrayList<IntCoord> path = new ArrayList<IntCoord>();
		ArrayList<Node> openNodes = new ArrayList<Node>();
		ArrayList<Node> closedNodes = new ArrayList<Node>();
		openNodes.add(new Node(start.x, start.y, 0, end));
		Node current = getLowestFCost(openNodes);
		boolean findingPath = true;
		while(findingPath && iterationCount < 5000 && openNodes.size() > 0) {
			current = getLowestFCost(openNodes);
			iterationCount++;
			closedNodes.add(current);
			openNodes.remove(current);
			if (current.x == end.x && current.y == end.y) {
				findingPath = false;
			} else {
				for (int x = current.x - 1; x <= current.x + 1; x++) {
					for (int y = current.y - 1; y <= current.y + 1; y++) {
						if (x != current.x || y != current.y) {
							if (isNodeClear(x, y, current.x, current.y, level) && !doesNodeExist(x, y, closedNodes)) {
								int gCost = current.gCost;
								if (current.x - x == 0 || current.y - y == 0) {
									gCost += 10;
								} else {
									gCost += 14;
								}
								Node replacement = new Node(x, y, gCost, end);
								replacement.parent = current;
								if (doesNodeExist(x, y, openNodes)) {
									if (replacement.gCost < openNodes.get(getNode(x, y, openNodes)).gCost) {
										 openNodes.remove(getNode(x, y, openNodes));
										 openNodes.add(replacement);
									}
								} else {
									openNodes.add(replacement);
								}
							}
						}
					}
				}
			}
		}
		boolean creatingPath = !findingPath;
		while (creatingPath) {
			if (current.parent == null) {
				creatingPath = false;
			} else {
				path.add(new IntCoord(current.x, current.y));
				current = current.parent;
			}
		}
		if (findingPath) {
			path.add(start);
		}
		return path;
	}
	private static boolean isNodeClear(int x, int y, int firstX, int firstY, int level) {
		boolean nodeClear = true;
		boolean diag1Clear = true;
		boolean diag2Clear = true;
		if (WorldUtils.isStructBlock(x, y, level)) {
			nodeClear = SystemData.blockIDMap.get(WorldUtils.getStructBlock(x, y, level)).isPassable;
		}
		if (WorldUtils.isStructBlock(firstX, y, level)) {
			diag1Clear = SystemData.blockIDMap.get(WorldUtils.getStructBlock(firstX, y, level)).isPassable;
		}
		if (WorldUtils.isStructBlock(x, firstY, level)) {
			diag2Clear = SystemData.blockIDMap.get(WorldUtils.getStructBlock(x, firstY, level)).isPassable;
		}
		return nodeClear && (diag1Clear || diag2Clear);
		
	}
	private static int getNode(int x, int y, ArrayList<Node> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).x == x && nodes.get(i).y == y) {
				return i;
			}
		}
		return -1;
	}
	private static boolean doesNodeExist(int x, int y, ArrayList<Node> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).x == x && nodes.get(i).y == y) {
				return true;
			}
		}
		return false;
	}
	private static Node getLowestFCost(ArrayList<Node> nodes) {
		Node bestNode = nodes.get(0);
		for (int i = 1; i < nodes.size(); i++) {
			if (nodes.get(i).fCost < bestNode.fCost) {
				bestNode = nodes.get(i);
			}
		}
		return bestNode;
	}
	public class IntCoord implements Serializable {
		private static final long serialVersionUID = 1L;
		public int x;
		public int y;
		public IntCoord(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	private class Node {
		int x;
		int y;
		int fCost; //g+hcost
		int gCost; //Distance from beginning
		int hCost; //Distance from end
		Node parent = null;
		public Node(int x, int y, int gCost, IntCoord end) {
			this.x = x;
			this.y = y;
			this.gCost = gCost;
			this.hCost = getHCost(end);
			this.fCost = gCost + this.hCost;
		}
		private int getHCost(IntCoord end) {
			int dX = Math.abs(end.x - x);
			int dY = Math.abs(end.y - y);
			int diag = 0;
			if (dX > dY) {
				diag = dY;
				dX -= dY;
				dY = 0;
			} else {
				diag = dX;
				dY -= dX;
				dX = 0;
			}
			return diag * 14 + dX * 10 + dY + 10;
		}
	}
}
