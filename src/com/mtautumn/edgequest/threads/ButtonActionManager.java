/*This class continually checks the buttonActionQueue for new button actions to
 * run. If there is a new button action in the queue, the action gets executed
 * according to a switch statement.
 */
package com.mtautumn.edgequest.threads;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.GameSaves;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.MenuButtonManager;
import com.mtautumn.edgequest.window.MenuButtonManager.MenuToggleButton;

public class ButtonActionManager extends Thread {

	@Override
	public void run() {
		while (SystemData.running) {
			runButtonActions();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void runButtonActions() {
		int size = SystemData.buttonActionQueue.size();
		if (size > 0) {
			try {
				runButtonAction(SystemData.buttonActionQueue.get(size - 1), size - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void runButtonAction(String name, int index) {
		SystemData.buttonActionQueue.remove(index);
		String ans;
		switch (name) {
		case "newGame": //New Game
			SystemData.loadingWorld = true;
			ans = getInputText("Enter Your World Name:");
			if (ans != null) {
				try {
					DataManager.savable.saveName = ans;
					DataManager.savable.seed = DataManager.savable.saveName.hashCode();
					DataManager.newGame();
					SystemData.characterLocationSet = false;
				} catch (Exception e) {
					setNoticeText("An Error Has Occured");
					SystemData.loadingWorld = false;
				}
			}
			break;
		case "loadGame": //load game
			ans = getInputText("Enter a File Name:");
			if (ans != null) {
				try {
					GameSaves.loadGame(ans);
					SystemData.isGameOnLaunchScreen = false;
					SystemData.isLaunchScreenLoaded = false;
					SystemData.loadingWorld = false;
					SystemData.characterLocationSet = true;
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
						SettingsData.targetFPS = fps;
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
				GameSaves.saveGame();
				setNoticeText("Game Saved!");
			} catch (Exception e) {
				setNoticeText("Unable to save game");
			}
			break;
		case "graphics":
			SystemData.currentMenu = "Graphics Menu";
			break;
		case "game":
			SystemData.currentMenu = "Game Menu";
			break;
		case "fullScreen":
			SettingsData.isFullScreen = !SettingsData.isFullScreen;
			if (SettingsData.isFullScreen) {
				MenuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Windowed";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("fullScreen").displayName = "Windowed";
			} else {
				MenuButtonManager.getMenu("Graphics Menu").getButton("fullScreen").displayName = "Full Screen";
				DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("fullScreen").displayName = "Full Screen";
			}
			break;
		case "vSync":
			MenuToggleButton button = (MenuToggleButton) MenuButtonManager.getMenu("Graphics Menu").getButton("vSync");
			com.mtautumn.edgequest.window.LaunchScreenManager.MenuToggleButton button2 = (com.mtautumn.edgequest.window.LaunchScreenManager.MenuToggleButton) DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("vSync");
			button.state = !button.state;
			button2.state = button.state;
			SettingsData.vSyncOn = button.state;
			break;
		case "fastGraphics":
			MenuToggleButton button3 = (MenuToggleButton) MenuButtonManager.getMenu("Graphics Menu").getButton("fastGraphics");
			com.mtautumn.edgequest.window.LaunchScreenManager.MenuToggleButton button4 = (com.mtautumn.edgequest.window.LaunchScreenManager.MenuToggleButton) DataManager.rendererManager.renderer.launchScreenManager.getMenu("Settings").getButton("fastGraphics");
			button3.state = !button3.state;
			button4.state = button3.state;
			SettingsData.fastGraphics = button3.state;
			break;
		case "quit":
			SystemData.running = false;
			break;
		case "Go To Parent":
			if (MenuButtonManager.getCurrentMenu().parent != null) {
				SystemData.currentMenu = MenuButtonManager.getCurrentMenu().parent;
			} else {
				SystemData.isKeyboardMenu = false;
			}
			break;
		default:
			break;
		}
	}
	private static String getInputText(String text) {
		SystemData.inputText.add(text);
		int length = SystemData.inputText.size();
		while (SystemData.inputTextResponse.size() < SystemData.inputText.size()) {
			SystemData.inputTextResponse.add("");
		}
		SystemData.inputTextResponse.set(SystemData.inputTextResponse.size() - 1, "");
		while (SystemData.inputText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return SystemData.lastInputMessage;
	}

	private static void setNoticeText(String text) {
		SystemData.noticeText.add(text);
		int length = SystemData.noticeText.size();
		while (SystemData.noticeText.size() >= length) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
