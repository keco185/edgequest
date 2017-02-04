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
	DataManager dataManager;
	public LaunchScreenManager(DataManager dataManager) {
		this.dataManager = dataManager;
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
		if (posX < dataManager.settings.BACK_BUTTON_SIZE * dataManager.system.uiZoom + dataManager.settings.BACK_BUTTON_PADDING * dataManager.system.uiZoom && posX > 0 && posY < dataManager.settings.BACK_BUTTON_SIZE * dataManager.system.uiZoom + dataManager.settings.BACK_BUTTON_PADDING * dataManager.system.uiZoom && posY > 0) {
			String parent = getCurrentMenu().parent;
			if (parent != null) {
				currentMenu = parent;
			}
		}
		posX -= dataManager.settings.screenWidth / 2;
		posY -= dataManager.settings.screenHeight / 2;
		for (int i = 0; i < getCurrentMenu().getButtons().size(); i++) {
			MenuButton button = getCurrentMenu().getButtons().get(i);
			if (posX > button.getX() && posX < button.getX() + button.getWidth() && posY > button.getY() && posY < button.getY() + button.getHeight()) {
				if (button.visible) runButtonAction(button.name);
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
			dataManager.system.buttonActionQueue.add(name);
		}
	}
	public class LaunchScreenPane {
		private static final int BUTTON_WIDTH = 197;
		private static final int BUTTON_HEIGHT = 73;
		//private static final int SPACING_X = 256;
		private static final int SPACING_Y = 57;
		private static final int START_X = -128;
		private static final int START_Y = -159;
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
			return (int)(posX * dataManager.system.uiZoom);
		}
		public int getPosY() {
			return (int)(posY * dataManager.system.uiZoom);
		}
		public int getX() {
			return (int)(posX * dataManager.system.uiZoom);
		}
		public int getY() {
			return (int)(posY * dataManager.system.uiZoom);
		}
		public int getWidth() {
			return (int) (width * dataManager.system.uiZoom);
		}
		public int getHeight() {
			return (int) (height * dataManager.system.uiZoom);
		}
	}

}
