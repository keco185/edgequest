package com.mtautumn.edgequest.dataObjects;

import java.util.ArrayList;


//These are 10x10 block chunks
public class Chunk {
	public ArrayList<Integer> entities;
	public ArrayList<Structure> structures;
	public short[][] ground = new short[10][10];
	public short[][] wall = new short[10][10];
	public Chunk() {
		entities = new ArrayList<>();
		structures = new ArrayList<>();
	}
}
