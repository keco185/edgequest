/*This class continually checks the buttonActionQueue for new button actions to
 * run. If there is a new button action in the queue, the action gets executed
 * according to a switch statement.
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;

public class ButtonActionManager extends Thread {

	@Override
	public void run() {
		while (DataManager.system.running) {
			runButtonActions();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void runButtonActions() {
		int size = DataManager.system.buttonActionQueue.size();
		if (size > 0) {
			try {
				runButtonAction(DataManager.system.buttonActionQueue.get(size - 1), size - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void runButtonAction(String name, int index) {
		DataManager.system.buttonActionQueue.remove(index);
		String ans;
		switch (name) {
		case "newGame": //New Game
			ans = getInputText("Enter Your World Name:");
			if (ans != null) {
				try {
					DataManager.savable.saveName = ans;
					DataManager.savable.seed = DataManager.savable.saveName.hashCode();
					DataManager.newGame();
					DataManager.system.characterLocationSet = false;
					DataManager.system.loadingWorld = true;
				} catch (Exception e) {
					setNoticeText("An Error Has Occured");
				}
			}
			break;
		case "loadGame": //load game
			ans = getInputText("Enter a File Name:");
			if (ans != null) {
				try {
					GameSaves.loadGame(ans);
					DataManager.system.isGameOnLaunchScreen = false;
					DataManager.system.isLaunchScreenLoaded = false;
					DataManager.system.loadingWorld = false;
					DataManager.system.characterLocationSet = true;
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
						DataManager.settings.targetFPS = fps;
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
				GameSaves.saveGame(DataManager.savable.saveName);
				setNoticeText("Game Saved!");
			} catch (Exception e) {
				setNoticeText("Unable to save game");
			}
			break;
		case "graphics":
			DataManager.system.currentMenu = "Graphics Menu";
			break;
		case "game":
			DataManager.system.currentMenu = "Game Menu";
			break;
		case "fullScreen":
			DataManager.settings.isFullScreen = !DataManager.settings.isFullScreen;
			if (DataManager.settings.isFullScreen) {
				DataManager.menuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Windowed";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("fullScreen").displayName = "Windowed";
			} else {
				DataManager.menuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Full Screen";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("fullScreen").displayName = "Full Screen";
			}
			break;
		case "vSync":
			DataManager.settings.vSyncOn = !DataManager.settings.vSyncOn;
			if (DataManager.settings.vSyncOn) {
				DataManager.menuButtonManager.getMenu("Graphics Menu").getButton("vSync").displayName = "V-Sync Off";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("vSync").displayName = "V-Sync Off";

			} else {
				DataManager.menuButtonManager.getMenu("Graphics Menu").getButton("vSync").displayName = "V-Sync On";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("vSync").displayName = "V-Sync On";
			}
			break;
		case "quit":
			DataManager.system.running = false;
			break;
		case "Go To Parent":
			if (DataManager.menuButtonManager.getCurrentMenu().parent != null) {
				DataManager.system.currentMenu = DataManager.menuButtonManager.getCurrentMenu().parent;
			} else {
				DataManager.system.isKeyboardMenu = false;
			}
			break;
		default:
			break;
		}
	}
	private static String getInputText(String text) {
		DataManager.system.inputText.add(text);
		int length = DataManager.system.inputText.size();
		while (DataManager.system.inputTextResponse.size() < DataManager.system.inputText.size()) {
			DataManager.system.inputTextResponse.add("");
		}
		DataManager.system.inputTextResponse.set(DataManager.system.inputTextResponse.size() - 1, "");
		while (DataManager.system.inputText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return DataManager.system.lastInputMessage;
	}

	private static void setNoticeText(String text) {
		DataManager.system.noticeText.add(text);
		int length = DataManager.system.noticeText.size();
		while (DataManager.system.noticeText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
