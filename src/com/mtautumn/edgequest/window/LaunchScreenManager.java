package com.mtautumn.edgequest.window;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.DataManager;

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
		settingsMenu.addButton(new MenuButton("vSync", "V-Sync Off"));
		settingsMenu.addButton(new MenuButton("fullScreen", "Full Screen"));
		menus.add(settingsMenu);
	}
	public void buttonPressed(int posX, int posY) {
		if (posX < DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom + DataManager.settings.BACK_BUTTON_PADDING_LEFT * DataManager.system.uiZoom && posX > 0 && posY < DataManager.settings.BACK_BUTTON_SIZE * DataManager.system.uiZoom + DataManager.settings.BACK_BUTTON_PADDING_TOP * DataManager.system.uiZoom && posY > 0) {
			String parent = getCurrentMenu().parent;
			if (parent != null) {
				currentMenu = parent;
			}
		}
		double yOffset = 206 * DataManager.system.uiZoom;
		posX -= DataManager.settings.screenWidth / 2;
		posY -= (DataManager.settings.screenHeight - yOffset) / 2;
		
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
			DataManager.system.buttonActionQueue.add(name);
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
	public class MenuButton {
		private int posX = 0;
		private int posY = 0;
		private int width = 0;
		private int height = 0;
		public Texture buttonImage;
		public String name = "";
		public String displayName = "";
		public boolean visible = true;
		public MenuButton(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
			try {
				this.buttonImage = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/menuButton.png"));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		public int getPosX() {
			return -(getWidth() / 2);
		}
		public int getPosY() {
			return (int)(posY * DataManager.system.uiZoom);
		}
		public int getX() {
			return (int)(posX * DataManager.system.uiZoom);
		}
		public int getY() {
			return (int)(posY * DataManager.system.uiZoom);
		}
		public int getWidth() {
			return (int) (width * DataManager.system.uiZoom);
		}
		public int getHeight() {
			return (int) (height * DataManager.system.uiZoom);
		}
	}

}
