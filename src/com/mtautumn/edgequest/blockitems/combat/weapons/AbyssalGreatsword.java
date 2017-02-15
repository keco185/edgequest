package com.mtautumn.edgequest.blockitems.combat.weapons;

import com.mtautumn.edgequest.blockitems.combat.WeaponMelee;

public class AbyssalGreatsword extends WeaponMelee {
	public AbyssalGreatsword () {
		damage = 10;
		attackSpeed = 1;
		attackDelay = 0;
		chain = 3;
		length = 60;
		width = 10;
		hitboxX = 32; //not accurate
		hitboxY = 16; // not accurate
		stability = 30;
		slashLunge = 5;
		thrustLunge = 10;
		knockback = 10;
		attackList = new attackTypes[] {
				attackTypes.slash,
				attackTypes.slash,
				attackTypes.thrust
				};
		}
}