package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.blockitems.BlockItem;

public class Ammo extends BlockItem{
	private static final long serialVersionUID = 1L;
	public Ammo(int id, boolean isBlock, boolean isItem, String name, int[] blockAnimation, int[] itemAnimation) {
		super(id, isBlock, isItem, name, blockAnimation, itemAnimation);
		// TODO Auto-generated constructor stub
	}
	String[] attributes = new String[0];
	double damageMultiplier = 1;
}
