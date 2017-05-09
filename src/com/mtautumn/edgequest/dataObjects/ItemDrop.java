package com.mtautumn.edgequest.dataObjects;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class ItemDrop {
	public double x;
	public double y;
	public int level;
	public int age;
	public ItemSlot item;
	private int creationTime;
	public ItemDrop(double x, double y, int level, ItemSlot item) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.item = item;
		creationTime = SystemData.animationClock;
	}
	public Texture getTexture(Renderer r) {
		if (item.getItemCount() > 0) {
			return r.textureManager.getTexture(SystemData.blockIDMap.get(item.getItemID()).getDropImg(SystemData.animationClock));
		}
		return r.textureManager.getTexture("none");
	}
	public float getRotation() {
		return (SystemData.animationClock - creationTime) / 30.0f;
	}
	public void update() {
		age++;
	}
}
