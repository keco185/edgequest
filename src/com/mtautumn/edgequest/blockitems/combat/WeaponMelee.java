package com.mtautumn.edgequest.blockitems.combat;

public class WeaponMelee {
	//public double slashDamage;
	//public double thrustDamage;
	public double damage;
	public double attackSpeed;
	public double attackDelay;
	public int chain;
	public double length;
	public double width;
	//public double radius;
	public double hitboxX;
	public double hitboxY;
	public double stability;
	public double slashLunge;
	public double thrustLunge;
	public double knockback;
	public enum attackTypes{
		slash,
		thrust
	};
	public attackTypes[] attackList = attackTypes.values();
}