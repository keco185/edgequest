package com.mtautumn.edgequest.entityStates;

public interface EntityStatusEffects{
	public void burn(); //take periodic damage which staggers
	public void curse(); //reverse controls, or health debuf, or atk & def debuf :3
	public void fear(); //can only move, not attack, possibly be forced to move away from source
	public void frost(); //slow movement and stamina regen
	public void insanity(); //large instant health loss? screen blur?
	public void paralyze(); //no movement or attacking
	public void poison(); //slow continual damage
	public void slow(); //reduces movement speed
}