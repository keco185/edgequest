/*This class continually checks the buttonActionQueue for new button actions to
 * run. If there is a new button action in the queue, the action gets executed
 * according to a switch statement.
 */
package com.mtautumn.edgequest;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;

public class ButtonActionManager extends Thread {
	DataManager dataManager;
	public ButtonActionManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	public void run() {
		while (dataManager.system.running) {
			runButtonActions();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void runButtonActions() {
		int size = dataManager.system.buttonActionQueue.size();
		if (size > 0) {
			try {
				runButtonAction(dataManager.system.buttonActionQueue.get(size - 1), size - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void runButtonAction(String name, int index) {
		dataManager.system.buttonActionQueue.remove(index);
		String ans;
		switch (name) {
		case "newGame": //New Game
			ans = getInputText("Enter Your World Name:");
			if (ans != null) {
				try {
					dataManager.savable.saveName = ans;
					dataManager.savable.seed = dataManager.savable.saveName.hashCode();
					dataManager.newGame();
					dataManager.system.characterLocationSet = false;
					dataManager.system.loadingWorld = true;
				} catch (Exception e) {
					setNoticeText("An Error Has Occured");
				}
			}
			break;
		case "loadGame": //load game
			ans = getInputText("Enter a File Name:");
			if (ans != null) {
				try {
					GameSaves.loadGame(ans, dataManager);
					dataManager.system.isGameOnLaunchScreen = false;
					dataManager.system.isLaunchScreenLoaded = false;
					dataManager.system.loadingWorld = false;
					dataManager.system.characterLocationSet = true;
				} catch (Exception e) {
					setNoticeText("Could not load game");
					e.printStackTrace();
				}
			}
			break;
		case "setFPS":
			ans = getInputText("Enter FPS Target:");
			if (ans != null) {
				try {
					int fps = Integer.parseInt(ans);
					if (fps > 0) {
						dataManager.settings.targetFPS = fps;
					} else {
						setNoticeText("FPS too low");
					}
				} catch (Exception e) {
					setNoticeText("FPS not valid");
				}
			}
			break;
		case "saveGame":
			try {
				GameSaves.saveGame(dataManager.savable.saveName, dataManager);
				setNoticeText("Game Saved!");
			} catch (Exception e) {
				setNoticeText("Unable to save game");
			}
			break;
		case "graphics":
			dataManager.system.currentMenu = "Graphics Menu";
			break;
		case "game":
			dataManager.system.currentMenu = "Game Menu";
			break;
		case "fullScreen":
			dataManager.settings.isFullScreen = !dataManager.settings.isFullScreen;
			if (dataManager.settings.isFullScreen) {
				dataManager.menuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Windowed";
			} else {
				dataManager.menuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Full Screen";
			}
			break;
		case "vSync":
			dataManager.settings.vSyncOn = !dataManager.settings.vSyncOn;
			if (dataManager.settings.vSyncOn) {
				dataManager.menuButtonManager.getMenu("Graphics Menu").getButton("vSync").displayName = "V-Sync Off";
			} else {
				dataManager.menuButtonManager.getMenu("Graphics Menu").getButton("vSync").displayName = "V-Sync On";
			}
			break;
		case "quit":
			dataManager.system.running = false;
			break;
		case "Go To Parent":
			if (dataManager.menuButtonManager.getCurrentMenu().parent != null) {
				dataManager.system.currentMenu = dataManager.menuButtonManager.getCurrentMenu().parent;
			} else {
				dataManager.system.isKeyboardMenu = false;
			}
			break;
		default:
			break;
		}
	}
	private String getInputText(String text) {
		dataManager.system.inputText.add(text);
		int length = dataManager.system.inputText.size();
		while (dataManager.system.inputTextResponse.size() < dataManager.system.inputText.size()) {
			dataManager.system.inputTextResponse.add("");
		}
		dataManager.system.inputTextResponse.set(dataManager.system.inputTextResponse.size() - 1, "");
		while (dataManager.system.inputText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return dataManager.system.lastInputMessage;
	}

	private void setNoticeText(String text) {
		dataManager.system.noticeText.add(text);
		int length = dataManager.system.noticeText.size();
		while (dataManager.system.noticeText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
