package com.mtautumn.edgequest.window;

import java.util.ArrayList;


import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class LaunchScreenManager {
	public ArrayList<LaunchScreenPane> menus = new ArrayList<LaunchScreenPane>();
	public ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	public String currentMenu = "Main Menu";
	public LaunchScreenManager() {
		LaunchScreenPane mainMenu = new LaunchScreenPane("Main Menu", null);
		mainMenu.addButton(new MenuButton("newGame", "New Game"));
		mainMenu.addButton(new MenuButton("loadGame", "Load Game"));
		mainMenu.addButton(new MenuButton("settings", "Settings"));
		mainMenu.addButton(new MenuButton("quit", "Quit"));
		menus.add(mainMenu);
		LaunchScreenPane settingsMenu = new LaunchScreenPane("Settings", "Main Menu");
		settingsMenu.addButton(new MenuToggleButton("vSync", "V-Sync", true));
		settingsMenu.addButton(new MenuButton("fullScreen", "Full Screen"));
		settingsMenu.addButton(new MenuToggleButton("fastGraphics", "Fast Graphics", false));
		menus.add(settingsMenu);
	}
	public void buttonPressed(int posX, int posY) {
		if (posX < SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom + SettingsData.BACK_BUTTON_PADDING_LEFT * SystemData.uiZoom && posX > 0 && posY < SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom + SettingsData.BACK_BUTTON_PADDING_TOP * SystemData.uiZoom && posY > 0) {
			String parent = getCurrentMenu().parent;
			if (parent != null) {
				currentMenu = parent;
			}
		}
		double yOffset = 206 * SystemData.uiZoom;
		posX -= SettingsData.screenWidth / 2;
		posY -= (SettingsData.screenHeight - yOffset) / 2;
		
		for (int i = 0; i < getCurrentMenu().getButtons().size(); i++) {
			MenuButton button = getCurrentMenu().getButtons().get(i);
			if (posX > button.getX() && posX < button.getX() + button.getWidth() && posY > button.getY() + yOffset && posY < button.getY() + button.getHeight() + yOffset) {
				if (button.visible) {
					runButtonAction(button.name);
				}
			}
		}
	}
	public LaunchScreenPane getMenu(String name) {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(name)) {
				return menus.get(i);
			}
		}
		return null;
	}
	public LaunchScreenPane getCurrentMenu() {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(currentMenu)) {
				return menus.get(i);
			}
		}
		return null;
	}
	public MenuButton getButtonFromName(String name) {
		MenuButton button = null;
		for (int i = 0; i < buttonIDArray.size(); i++) {
			if (buttonIDArray.get(i).name.equals(name)) {
				button = buttonIDArray.get(i);
			}
		}
		return button;
	}
	private void runButtonAction(String name) {
		if (name.equals("settings")) {
			currentMenu = "Settings";
		} else {
			SystemData.buttonActionQueue.add(name);
		}
	}
	public class LaunchScreenPane {
		private static final int BUTTON_WIDTH = 186;
		private static final int BUTTON_HEIGHT = 72;
		//private static final int SPACING_X = 256;
		private static final int SPACING_Y = 57;
		private static final int START_X = -128;
		private static final int START_Y = -229;
		private int currentX = START_X;
		private int currentY = START_Y;
		private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
		public String name;
		public String parent;
		public LaunchScreenPane(String name, String parent) {
			this.name = name;
			this.parent = parent;
		}
		public void addButton(MenuButton button) {
			button.width = BUTTON_WIDTH;
			button.height = BUTTON_HEIGHT;
			button.posX = currentX;
			button.posY = currentY;
			buttons.add(button);
			//currentX += (SPACING_X + BUTTON_WIDTH);
			//if (currentX > 800 - BUTTON_WIDTH) {
			//	currentX = START_X;
				currentY += (SPACING_Y + BUTTON_HEIGHT);
			//}
		}
		public ArrayList<MenuButton> getButtons() {
			return buttons;
		}
		public int getCount() {
			return buttons.size();
		}
		public MenuButton getButton(String buttonName) {
			for (int i = 0; i < buttons.size(); i++) {
				if (buttons.get(i).name.equals(buttonName)) {
					return buttons.get(i);
				}
			}
			return null;
		}
	}
	public class MenuToggleButton extends MenuButton {
		public boolean state;
		public MenuToggleButton(String name, String displayName, boolean startingState) {
			super(name, displayName);
			state = startingState;
		}
	}
	public class MenuButton {
		private int posX = 0;
		private int posY = 0;
		private int width = 0;
		private int height = 0;
		public String name = "";
		public String displayName = "";
		public boolean visible = true;
		public MenuButton(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}
		public int getPosX() {
			return -(getWidth() / 2);
		}
		public int getPosY() {
			return (int)(posY * SystemData.uiZoom);
		}
		public int getX() {
			return (int)(posX * SystemData.uiZoom);
		}
		public int getY() {
			return (int)(posY * SystemData.uiZoom);
		}
		public int getWidth() {
			return (int) (width * SystemData.uiZoom);
		}
		public int getHeight() {
			return (int) (height * SystemData.uiZoom);
		}
	}

}
