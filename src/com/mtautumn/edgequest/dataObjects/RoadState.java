package com.mtautumn.edgequest.dataObjects;

public class RoadState {

	public boolean roadTop,roadRight,roadBottom,roadLeft = false;

	public void add(RoadState roadState) {
		roadTop = roadTop || roadState.roadTop;
		roadRight = roadRight || roadState.roadRight;
		roadBottom = roadBottom || roadState.roadBottom;
		roadLeft = roadLeft || roadState.roadLeft;
	}
	public int countRoads() {
		int i = 0;
		if (roadTop) {
			i++;
		}
		if (roadLeft) {
			i++;
		}
		if (roadRight) {
			i++;
		}
		if (roadBottom) {
			i++;
		}
		return i;
	}

}
