package com.mtautumn.edgequest.utils.io;

import org.lwjgl.input.Keyboard;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.threads.CharacterManager;

public class KeyboardUpdater {
	private TextFieldInputUpdater keyboard;
	private CharacterManager characterManager;
	private static boolean[] wasKeyDown = new boolean[256];
	
	public boolean keyUp;
	public boolean keyDown;
	public boolean keyLeft;
	public boolean keyRight;
	public boolean keySprint;
	public boolean keyDodge;
	public boolean keyTravel;
	public boolean keyMenu;
	public boolean keyBackpack;
	public boolean keyZoomIn;
	public boolean keyZoomOut;
	public boolean keyShowDiag;
	public boolean keyPlaceTorch;
	public boolean keyConsole;
	public boolean keyAction;
	public boolean keyExit;
	
	public KeyboardUpdater() {
		keyboard = new TextFieldInputUpdater();
		characterManager = DataManager.characterManager;
	}
	public void updateKeys() {
		try {
			Keyboard.poll();
			keyUp = Keyboard.isKeyDown(SettingsData.upKey);
			keyDown = Keyboard.isKeyDown(SettingsData.downKey);
			keyLeft = Keyboard.isKeyDown(SettingsData.leftKey);
			keyRight = Keyboard.isKeyDown(SettingsData.rightKey);
			keySprint = Keyboard.isKeyDown(SettingsData.sprintKey);
			keyDodge = Keyboard.isKeyDown(SettingsData.dodgeKey);
			keyTravel = Keyboard.isKeyDown(SettingsData.travelKey);
			keyMenu = Keyboard.isKeyDown(SettingsData.menuKey);
			keyBackpack = Keyboard.isKeyDown(SettingsData.backpackKey);
			keyZoomIn = Keyboard.isKeyDown(SettingsData.zoomInKey);
			keyZoomOut = Keyboard.isKeyDown(SettingsData.zoomOutKey);
			keyShowDiag = Keyboard.isKeyDown(SettingsData.showDiagKey);
			keyPlaceTorch = Keyboard.isKeyDown(SettingsData.placeTorchKey);
			keyConsole = Keyboard.isKeyDown(SettingsData.consoleKey);
			keyAction = Keyboard.isKeyDown(SettingsData.actionKey);
			keyExit = Keyboard.isKeyDown(SettingsData.exitKey);
			
			
			if (SystemData.inputText.size() + SystemData.noticeText.size() > 0 || SystemData.showConsole) {
				keyboard.poll();
				keyboard.wasConsoleUp = SystemData.showConsole;
			} 
			
			else {
				if (!SystemData.isGameOnLaunchScreen) {
					SystemData.isKeyboardSprint = keySprint;
					SystemData.isKeyboardTravel = keyTravel;
					
					//performNumberKeyActions();
					
					if (isPlayerAiming()) {
						SystemData.isAiming = true;
					} else {
						SystemData.isAiming = false;
					}
					
					if (!SystemData.autoWalk) {
						updateMovementKeys(keyUp, keyRight, keyDown, keyLeft);
					} else {
						if (keyUp || keyDown || keyRight || keyLeft) {
							SystemData.autoWalk = false;
						}
					}
					
					if (keyZoomIn && !wasKeyDown[SettingsData.zoomInKey]) { //If zoom in key was just pressed
						if (SettingsData.targetBlockSize < 128 * SystemData.uiZoom) {
							SettingsData.targetBlockSize *= 2;
							SystemData.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[SettingsData.zoomOutKey]) { //If zoom out key was just pressed
						if (SettingsData.targetBlockSize > 16 * SystemData.uiZoom) {
							SettingsData.targetBlockSize /= 2;
							SystemData.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[SettingsData.showDiagKey]) {
						SettingsData.showDiag = !SettingsData.showDiag;
					}

					if (keyMenu && !wasKeyDown[SettingsData.menuKey]) {
						SystemData.isKeyboardMenu = !SystemData.isKeyboardMenu;
					}

					if (keyPlaceTorch && !wasKeyDown[SettingsData.placeTorchKey]) {
						characterManager.charPlaceTorch();
					}

					if (keyBackpack && !wasKeyDown[SettingsData.backpackKey]) {
						SystemData.isKeyboardBackpack = !SystemData.isKeyboardBackpack;
					}

					if (keyConsole && !wasKeyDown[SettingsData.consoleKey]) {
						SystemData.showConsole = true;
					}
					if (keyExit && !wasKeyDown[SettingsData.exitKey]) { //If exit/escape key was just pressed
						if (SystemData.showConsole) {
							SystemData.showConsole = false;
						} else if (SystemData.isKeyboardMenu) {
							SystemData.isKeyboardMenu = false;
						} else if (SystemData.isKeyboardBackpack) {
							SystemData.isKeyboardBackpack = false;
						}
					}
					if (keyAction && !wasKeyDown[SettingsData.actionKey]) { //If action key was just pressed
						if (DataManager.itemDropManager.isItemInRange(CharacterManager.characterEntity)) { //Pickup item if available
							DataManager.itemDropManager.putNearbyItemsInBackpack();
						} else if (CharacterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeonUp")) { //Go up loadder if available
							CharacterManager.characterEntity.dungeonLevel -= 1;
							DataManager.savable.dungeonLevel -= 1;
						} else if (CharacterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeon")) { //Go down ladder if available
							CharacterManager.characterEntity.dungeonLevel += 1;
							DataManager.savable.dungeonLevel += 1;
						}
					}
				}

			}
			
			if (keyboard.wasConsoleUp) {
				keyboard.wasConsoleUp = SystemData.showConsole;
			}
			
			// Perform exit/escape key actions
			if (keyExit && !wasKeyDown[SettingsData.exitKey]) {
				if (SystemData.noticeText.size() > 0) {
					SystemData.noticeText.remove(SystemData.noticeText.size() - 1);
				} else if (SystemData.inputText.size() > 0) {
					SystemData.inputText.remove(SystemData.inputText.size() - 1);
					SystemData.inputTextResponse.remove(SystemData.inputTextResponse.size() - 1);
					SystemData.lastInputMessage = null;
				} else if (SystemData.showConsole) {
					SystemData.showConsole = false;
				}
			}
			
			//Update info on if each key was down last update
			wasKeyDown[SettingsData.menuKey] = keyMenu;
			wasKeyDown[SettingsData.backpackKey] = keyBackpack;
			wasKeyDown[SettingsData.zoomInKey] = keyZoomIn;
			wasKeyDown[SettingsData.zoomOutKey] = keyZoomOut;
			wasKeyDown[SettingsData.showDiagKey] = keyShowDiag;
			wasKeyDown[SettingsData.placeTorchKey] = keyPlaceTorch;
			wasKeyDown[SettingsData.consoleKey] = keyConsole;
			wasKeyDown[SettingsData.actionKey] = keyAction;
			wasKeyDown[SettingsData.exitKey] = keyExit;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void performNumberKeyActions() {
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			dataManager.savable.hotBarSelection = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			dataManager.savable.hotBarSelection = 1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			dataManager.savable.hotBarSelection = 2;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
			dataManager.savable.hotBarSelection = 3;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
			dataManager.savable.hotBarSelection = 4;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
			dataManager.savable.hotBarSelection = 5;
		}
	}*/
	
	private static void updateMovementKeys(boolean keyUp, boolean keyRight, boolean keyDown, boolean keyLeft) {
		SystemData.isKeyboardUp = keyUp;
		SystemData.isKeyboardRight = keyRight;
		SystemData.isKeyboardDown = keyDown;
		SystemData.isKeyboardLeft = keyLeft;
		
		if (keyUp || keyRight || keyDown || keyLeft){
			SystemData.isMoveInput = true;
		} else {
			SystemData.isMoveInput = false;
		}
	}
	
	private static boolean isPlayerAiming() {
		return Keyboard.isKeyDown(SettingsData.aimKey) || SystemData.rightMouseDown;
	}

}
