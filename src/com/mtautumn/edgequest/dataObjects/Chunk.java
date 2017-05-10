package com.mtautumn.edgequest.dataObjects;

import java.io.Serializable;
import java.util.ArrayList;

import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.threads.CharacterManager;


//These are 10x10 block chunks
public class Chunk implements Serializable {
	private static final long serialVersionUID = 1L;
	public boolean inUse = false;
	public ArrayList<Integer> entities;
	public ArrayList<Structure> structures;
	public short[][] ground = new short[10][10];
	public short[][] wall = new short[10][10];
	public int x,y,level;
	public Chunk(int x, int y, int level) {
		this.x = x;
		this.y = y;
		this.level = level;
		entities = new ArrayList<>();
		structures = new ArrayList<>();
	}
	public boolean isOnScreen() {
		boolean inXRange = false;
		boolean inYRange = false;
		if (SystemData.minTileX < x + 10 && SystemData.maxTileX >= x) inXRange = true;
		
		if (SystemData.minTileY < y + 10 && SystemData.maxTileY >= y) inYRange = true;		
		return inXRange && inYRange && level == CharacterManager.characterEntity.dungeonLevel;
	}
}
