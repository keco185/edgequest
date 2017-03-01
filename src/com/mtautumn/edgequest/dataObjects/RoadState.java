package com.mtautumn.edgequest.dataObjects;

public class RoadState {
	public boolean roadTop,roadRight,roadBottom,roadLeft = false;
	public void add(RoadState roadState) {
		roadTop = roadTop || roadState.roadTop;
		roadRight = roadRight || roadState.roadRight;
		roadBottom = roadBottom || roadState.roadBottom;
		roadLeft = roadLeft || roadState.roadLeft;
	}
}
