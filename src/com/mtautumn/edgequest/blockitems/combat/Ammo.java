package com.mtautumn.edgequest.blockitems.combat;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.data.DataManager;

public class Ammo extends BlockItem{
	private static final long serialVersionUID = 1L;
	public Ammo(int id, boolean isBlock, boolean isItem, String name, int[] blockAnimation, int[] itemAnimation,
			DataManager dm) {
		super(id, isBlock, isItem, name, blockAnimation, itemAnimation, dm);
		// TODO Auto-generated constructor stub
	}
	String[] attributes = new String[0];
	double damageMultiplier = 1;
}
