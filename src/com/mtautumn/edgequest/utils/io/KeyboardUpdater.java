package com.mtautumn.edgequest.utils.io;

import org.lwjgl.input.Keyboard;

import com.mtautumn.edgequest.data.DataManager;
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
			keyUp = Keyboard.isKeyDown(DataManager.settings.upKey);
			keyDown = Keyboard.isKeyDown(DataManager.settings.downKey);
			keyLeft = Keyboard.isKeyDown(DataManager.settings.leftKey);
			keyRight = Keyboard.isKeyDown(DataManager.settings.rightKey);
			keySprint = Keyboard.isKeyDown(DataManager.settings.sprintKey);
			keyDodge = Keyboard.isKeyDown(DataManager.settings.dodgeKey);
			keyTravel = Keyboard.isKeyDown(DataManager.settings.travelKey);
			keyMenu = Keyboard.isKeyDown(DataManager.settings.menuKey);
			keyBackpack = Keyboard.isKeyDown(DataManager.settings.backpackKey);
			keyZoomIn = Keyboard.isKeyDown(DataManager.settings.zoomInKey);
			keyZoomOut = Keyboard.isKeyDown(DataManager.settings.zoomOutKey);
			keyShowDiag = Keyboard.isKeyDown(DataManager.settings.showDiagKey);
			keyPlaceTorch = Keyboard.isKeyDown(DataManager.settings.placeTorchKey);
			keyConsole = Keyboard.isKeyDown(DataManager.settings.consoleKey);
			keyAction = Keyboard.isKeyDown(DataManager.settings.actionKey);
			keyExit = Keyboard.isKeyDown(DataManager.settings.exitKey);
			
			
			if (DataManager.system.inputText.size() + DataManager.system.noticeText.size() > 0 || DataManager.system.showConsole) {
				keyboard.poll();
				keyboard.wasConsoleUp = DataManager.system.showConsole;
			} 
			
			else {
				if (!DataManager.system.isGameOnLaunchScreen) {
					DataManager.system.isKeyboardSprint = keySprint;
					DataManager.system.isKeyboardTravel = keyTravel;
					
					//performNumberKeyActions();
					
					if (isPlayerAiming()) {
						DataManager.system.isAiming = true;
					} else {
						DataManager.system.isAiming = false;
					}
					
					if (!DataManager.system.autoWalk) {
						updateMovementKeys(keyUp, keyRight, keyDown, keyLeft);
					} else {
						if (keyUp || keyDown || keyRight || keyLeft) {
							DataManager.system.autoWalk = false;
						}
					}
					
					if (keyZoomIn && !wasKeyDown[DataManager.settings.zoomInKey]) { //If zoom in key was just pressed
						if (DataManager.settings.targetBlockSize < 128 * DataManager.system.uiZoom) {
							DataManager.settings.targetBlockSize *= 2;
							DataManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[DataManager.settings.zoomOutKey]) { //If zoom out key was just pressed
						if (DataManager.settings.targetBlockSize > 16 * DataManager.system.uiZoom) {
							DataManager.settings.targetBlockSize /= 2;
							DataManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[DataManager.settings.showDiagKey]) {
						DataManager.settings.showDiag = !DataManager.settings.showDiag;
					}

					if (keyMenu && !wasKeyDown[DataManager.settings.menuKey]) {
						DataManager.system.isKeyboardMenu = !DataManager.system.isKeyboardMenu;
					}

					if (keyPlaceTorch && !wasKeyDown[DataManager.settings.placeTorchKey]) {
						characterManager.charPlaceTorch();
					}

					if (keyBackpack && !wasKeyDown[DataManager.settings.backpackKey]) {
						DataManager.system.isKeyboardBackpack = !DataManager.system.isKeyboardBackpack;
					}

					if (keyConsole && !wasKeyDown[DataManager.settings.consoleKey]) {
						DataManager.system.showConsole = true;
					}
					if (keyExit && !wasKeyDown[DataManager.settings.exitKey]) { //If exit/escape key was just pressed
						if (DataManager.system.showConsole) {
							DataManager.system.showConsole = false;
						} else if (DataManager.system.isKeyboardMenu) {
							DataManager.system.isKeyboardMenu = false;
						} else if (DataManager.system.isKeyboardBackpack) {
							DataManager.system.isKeyboardBackpack = false;
						}
					}
					if (keyAction && !wasKeyDown[DataManager.settings.actionKey]) { //If action key was just pressed
						if (DataManager.itemDropManager.isItemInRange(DataManager.characterManager.characterEntity)) { //Pickup item if available
							DataManager.itemDropManager.putNearbyItemsInBackpack();
						} else if (DataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeonUp")) { //Go up loadder if available
							DataManager.characterManager.characterEntity.dungeonLevel -= 1;
							DataManager.savable.dungeonLevel -= 1;
						} else if (DataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeon")) { //Go down ladder if available
							DataManager.characterManager.characterEntity.dungeonLevel += 1;
							DataManager.savable.dungeonLevel += 1;
						}
					}
				}

			}
			
			if (keyboard.wasConsoleUp) {
				keyboard.wasConsoleUp = DataManager.system.showConsole;
			}
			
			// Perform exit/escape key actions
			if (keyExit && !wasKeyDown[DataManager.settings.exitKey]) {
				if (DataManager.system.noticeText.size() > 0) {
					DataManager.system.noticeText.remove(DataManager.system.noticeText.size() - 1);
				} else if (DataManager.system.inputText.size() > 0) {
					DataManager.system.inputText.remove(DataManager.system.inputText.size() - 1);
					DataManager.system.inputTextResponse.remove(DataManager.system.inputTextResponse.size() - 1);
					DataManager.system.lastInputMessage = null;
				} else if (DataManager.system.showConsole) {
					DataManager.system.showConsole = false;
				}
			}
			
			//Update info on if each key was down last update
			wasKeyDown[DataManager.settings.menuKey] = keyMenu;
			wasKeyDown[DataManager.settings.backpackKey] = keyBackpack;
			wasKeyDown[DataManager.settings.zoomInKey] = keyZoomIn;
			wasKeyDown[DataManager.settings.zoomOutKey] = keyZoomOut;
			wasKeyDown[DataManager.settings.showDiagKey] = keyShowDiag;
			wasKeyDown[DataManager.settings.placeTorchKey] = keyPlaceTorch;
			wasKeyDown[DataManager.settings.consoleKey] = keyConsole;
			wasKeyDown[DataManager.settings.actionKey] = keyAction;
			wasKeyDown[DataManager.settings.exitKey] = keyExit;
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
		DataManager.system.isKeyboardUp = keyUp;
		DataManager.system.isKeyboardRight = keyRight;
		DataManager.system.isKeyboardDown = keyDown;
		DataManager.system.isKeyboardLeft = keyLeft;
		
		if (keyUp || keyRight || keyDown || keyLeft){
			DataManager.system.isMoveInput = true;
		} else {
			DataManager.system.isMoveInput = false;
		}
	}
	
	private static boolean isPlayerAiming() {
		return Keyboard.isKeyDown(DataManager.settings.aimKey) || DataManager.system.rightMouseDown;
	}

}
