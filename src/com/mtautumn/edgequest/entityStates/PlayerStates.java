package com.mtautumn.edgequest.entityStates;

import com.mtautumn.edgequest.utils.io.KeyboardUpdater;

/*
 * If a state is entered and exited, but you don't know
 * which behavior to return to, use a stack to return
 * to the last state you were in.
 * 
 * enter() and exit() controls can be added to make these
 * even more useful
 */

public class PlayerStates {
	private KeyboardUpdater keyEvent;
	
	public enum PlayerState {
		idle,
		walk,
		sprint,
		attack,
		dodge,
		stagger,
		die
	};
	public PlayerState currentState = PlayerState.idle;
	public boolean isMoving = false;
	public boolean isSprinting = false;
	
	public PlayerStates (){
		keyEvent = new KeyboardUpdater();
	}
	
	public void updateState() {
		/* include a check for if the player velocity is
		 * != 0 so that player stays idle when not actually
		 * willingly covering any ground.
		 */
		if (keyEvent.keyUp || keyEvent.keyDown || keyEvent.keyLeft || keyEvent.keyRight) {
			isMoving = true;
		}
		if (isMoving && keyEvent.keySprint){
			isSprinting = true;
		}
		
		switch(currentState) {
			case idle:		IdleUpdate();
							break;
			case walk:		WalkUpdate();
							break;
			case sprint:	SprintUpdate();
							break;
			case attack:	AttackUpdate();
							break;
			case dodge:		DodgeUpdate();
							break;
			case stagger:	StaggerUpdate();
							break;
			case die:		DieUpdate();
							break;
			default :		break;
		}
	}
	
	private void IdleUpdate() {
		if (isMoving) {
			if (keyEvent.keySprint) {
				currentState = PlayerState.sprint;
			} else {
				currentState = PlayerState.walk;
			}
		} else if (keyEvent.keyDodge) {
			currentState = PlayerState.dodge;
		} else {
			//idle code
		}
	}
	
	private void WalkUpdate() {
		if (keyEvent.keyDodge) {
			currentState = PlayerState.dodge;
		} else if (keyEvent.keySprint) {
			currentState = PlayerState.sprint;
		} else {
			//walk code
		}
	}
	
	private void SprintUpdate() {
		if (!isMoving) {
			currentState = PlayerState.idle;
		} else if (!keyEvent.keySprint) {
			currentState = PlayerState.walk;
		} else {
			//sprint code
		}
	}
	
	private void AttackUpdate() {
	}
	
	private void DodgeUpdate() {
	}
	
	private void StaggerUpdate() {

		/* interrupts other animations,
		 * initiates stagger animation,
		 * after that it sets state to
		 * be idle
		 */
	}
	
	private void DieUpdate() {
		/* character stops taking damage
		 * monsters stop attacking
		 * initiates death animation
		 */
	}
}

/* If multiple entities need to make use of these
 * states we should consider designing this as an
 * interface renamed as entity states, for all
 * creatures to use at some point.
 */
