package com.mtautumn.edgequest;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class ItemDrop {
	public double x;
	public double y;
	public int level;
	public int age;
	public ItemSlot item;
	private int creationTime;
	public ItemDrop(double x, double y, int level, ItemSlot item, DataManager dm) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.item = item;
		creationTime = dm.system.animationClock;
	}
	public Texture getTexture(Renderer r) {
		if (item.getItemCount() > 0) {
			return r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(item.getItemID()).getDropImg(r.dataManager.system.animationClock));
		}
		return r.textureManager.getTexture("none");
	}
	public float getRotation(DataManager dm) {
		return (dm.system.animationClock - creationTime) / 30.0f;
	}
	public void update() {
		age++;
	}
}
