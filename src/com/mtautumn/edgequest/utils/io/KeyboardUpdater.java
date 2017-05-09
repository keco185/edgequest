package com.mtautumn.edgequest.utils.io;

import org.lwjgl.input.Keyboard;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.threads.CharacterManager;

public class KeyboardUpdater {
	private DataManager dataManager;
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
	
	public KeyboardUpdater(DataManager dm) {
		this.dataManager = dm;
		keyboard = new TextFieldInputUpdater(dataManager);
		characterManager = dataManager.characterManager;
	}
	public void updateKeys() {
		try {
			Keyboard.poll();
			keyUp = Keyboard.isKeyDown(dataManager.settings.upKey);
			keyDown = Keyboard.isKeyDown(dataManager.settings.downKey);
			keyLeft = Keyboard.isKeyDown(dataManager.settings.leftKey);
			keyRight = Keyboard.isKeyDown(dataManager.settings.rightKey);
			keySprint = Keyboard.isKeyDown(dataManager.settings.sprintKey);
			keyDodge = Keyboard.isKeyDown(dataManager.settings.dodgeKey);
			keyTravel = Keyboard.isKeyDown(dataManager.settings.travelKey);
			keyMenu = Keyboard.isKeyDown(dataManager.settings.menuKey);
			keyBackpack = Keyboard.isKeyDown(dataManager.settings.backpackKey);
			keyZoomIn = Keyboard.isKeyDown(dataManager.settings.zoomInKey);
			keyZoomOut = Keyboard.isKeyDown(dataManager.settings.zoomOutKey);
			keyShowDiag = Keyboard.isKeyDown(dataManager.settings.showDiagKey);
			keyPlaceTorch = Keyboard.isKeyDown(dataManager.settings.placeTorchKey);
			keyConsole = Keyboard.isKeyDown(dataManager.settings.consoleKey);
			keyAction = Keyboard.isKeyDown(dataManager.settings.actionKey);
			keyExit = Keyboard.isKeyDown(dataManager.settings.exitKey);
			
			
			if (dataManager.system.inputText.size() + dataManager.system.noticeText.size() > 0 || dataManager.system.showConsole) {
				keyboard.poll();
				keyboard.wasConsoleUp = dataManager.system.showConsole;
			} 
			
			else {
				if (!dataManager.system.isGameOnLaunchScreen) {
					dataManager.system.isKeyboardSprint = keySprint;
					dataManager.system.isKeyboardTravel = keyTravel;
					
					//performNumberKeyActions();
					
					if (isPlayerAiming()) {
						dataManager.system.isAiming = true;
					} else {
						dataManager.system.isAiming = false;
					}
					
					if (!dataManager.system.autoWalk) {
						updateMovementKeys(keyUp, keyRight, keyDown, keyLeft);
					} else {
						if (keyUp || keyDown || keyRight || keyLeft) {
							dataManager.system.autoWalk = false;
						}
					}
					
					if (keyZoomIn && !wasKeyDown[dataManager.settings.zoomInKey]) { //If zoom in key was just pressed
						if (dataManager.settings.targetBlockSize < 128 * dataManager.system.uiZoom) {
							dataManager.settings.targetBlockSize *= 2;
							dataManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[dataManager.settings.zoomOutKey]) { //If zoom out key was just pressed
						if (dataManager.settings.targetBlockSize > 16 * dataManager.system.uiZoom) {
							dataManager.settings.targetBlockSize /= 2;
							dataManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[dataManager.settings.showDiagKey]) {
						dataManager.settings.showDiag = !dataManager.settings.showDiag;
					}

					if (keyMenu && !wasKeyDown[dataManager.settings.menuKey]) {
						dataManager.system.isKeyboardMenu = !dataManager.system.isKeyboardMenu;
					}

					if (keyPlaceTorch && !wasKeyDown[dataManager.settings.placeTorchKey]) {
						characterManager.charPlaceTorch();
					}

					if (keyBackpack && !wasKeyDown[dataManager.settings.backpackKey]) {
						dataManager.system.isKeyboardBackpack = !dataManager.system.isKeyboardBackpack;
					}

					if (keyConsole && !wasKeyDown[dataManager.settings.consoleKey]) {
						dataManager.system.showConsole = true;
					}
					if (keyExit && !wasKeyDown[dataManager.settings.exitKey]) { //If exit/escape key was just pressed
						if (dataManager.system.showConsole) {
							dataManager.system.showConsole = false;
						} else if (dataManager.system.isKeyboardMenu) {
							dataManager.system.isKeyboardMenu = false;
						} else if (dataManager.system.isKeyboardBackpack) {
							dataManager.system.isKeyboardBackpack = false;
						}
					}
					if (keyAction && !wasKeyDown[dataManager.settings.actionKey]) { //If action key was just pressed
						if (dataManager.itemDropManager.isItemInRange(dataManager.characterManager.characterEntity)) { //Pickup item if available
							dataManager.itemDropManager.putNearbyItemsInBackpack();
						} else if (dataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeonUp")) { //Go up loadder if available
							dataManager.characterManager.characterEntity.dungeonLevel -= 1;
							dataManager.savable.dungeonLevel -= 1;
						} else if (dataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeon")) { //Go down ladder if available
							dataManager.characterManager.characterEntity.dungeonLevel += 1;
							dataManager.savable.dungeonLevel += 1;
						}
					}
				}

			}
			
			if (keyboard.wasConsoleUp) {
				keyboard.wasConsoleUp = dataManager.system.showConsole;
			}
			
			// Perform exit/escape key actions
			if (keyExit && !wasKeyDown[dataManager.settings.exitKey]) {
				if (dataManager.system.noticeText.size() > 0) {
					dataManager.system.noticeText.remove(dataManager.system.noticeText.size() - 1);
				} else if (dataManager.system.inputText.size() > 0) {
					dataManager.system.inputText.remove(dataManager.system.inputText.size() - 1);
					dataManager.system.inputTextResponse.remove(dataManager.system.inputTextResponse.size() - 1);
					dataManager.system.lastInputMessage = null;
				} else if (dataManager.system.showConsole) {
					dataManager.system.showConsole = false;
				}
			}
			
			//Update info on if each key was down last update
			wasKeyDown[dataManager.settings.menuKey] = keyMenu;
			wasKeyDown[dataManager.settings.backpackKey] = keyBackpack;
			wasKeyDown[dataManager.settings.zoomInKey] = keyZoomIn;
			wasKeyDown[dataManager.settings.zoomOutKey] = keyZoomOut;
			wasKeyDown[dataManager.settings.showDiagKey] = keyShowDiag;
			wasKeyDown[dataManager.settings.placeTorchKey] = keyPlaceTorch;
			wasKeyDown[dataManager.settings.consoleKey] = keyConsole;
			wasKeyDown[dataManager.settings.actionKey] = keyAction;
			wasKeyDown[dataManager.settings.exitKey] = keyExit;
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
	
	private void updateMovementKeys(boolean keyUp, boolean keyRight, boolean keyDown, boolean keyLeft) {
		dataManager.system.isKeyboardUp = keyUp;
		dataManager.system.isKeyboardRight = keyRight;
		dataManager.system.isKeyboardDown = keyDown;
		dataManager.system.isKeyboardLeft = keyLeft;
		
		if (keyUp || keyRight || keyDown || keyLeft){
			dataManager.system.isMoveInput = true;
		} else {
			dataManager.system.isMoveInput = false;
		}
	}
	
	private boolean isPlayerAiming() {
		return Keyboard.isKeyDown(dataManager.settings.aimKey) || dataManager.system.rightMouseDown;
	}

}
