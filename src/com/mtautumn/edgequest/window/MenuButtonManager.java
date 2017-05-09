package com.mtautumn.edgequest.window;

import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class MenuButtonManager {
	public ArrayList<MenuPane> menus = new ArrayList<MenuPane>();
	public ArrayList<MenuButton> buttonIDArray = new ArrayList<MenuButton>();
	public MenuButtonManager() {
		MenuPane mainMenu = new MenuPane("Main Menu", null);
		mainMenu.addButton(new MenuButton("game", "Game"));
		mainMenu.addButton(new MenuButton("graphics", "Graphics"));
		mainMenu.addButton(new MenuButton("quit", "Quit"));
		menus.add(mainMenu);
		MenuPane gameMenu = new MenuPane("Game Menu", "Main Menu");
		gameMenu.addButton(new MenuButton("newGame", "New Game"));
		gameMenu.addButton(new MenuButton("saveGame", "Save Game"));
		gameMenu.addButton(new MenuButton("loadGame", "Load Game"));
		menus.add(gameMenu);
		MenuPane graphicsMenu = new MenuPane("Graphics Menu", "Main Menu");
		graphicsMenu.addButton(new MenuButton("vSync", "V-Sync Off"));
		graphicsMenu.addButton(new MenuButton("fullScreen", "Full Screen"));
		menus.add(graphicsMenu);
	}
	public MenuPane getMenu(String name) {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(name)) {
				return menus.get(i);
			}
		}
		return null;
	}
	public MenuPane getCurrentMenu() {
		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).name.equals(SystemData.currentMenu)) {
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
	public void buttonPressed(int posX, int posY) {
		int adjustedX = posX - SystemData.menuX;
		int adjustedY = posY - SystemData.menuY;
		if (adjustedX < SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom + SettingsData.BACK_BUTTON_PADDING_LEFT * SystemData.uiZoom && adjustedX > 0 && adjustedY < SettingsData.BACK_BUTTON_SIZE * SystemData.uiZoom + SettingsData.BACK_BUTTON_PADDING_TOP * SystemData.uiZoom && adjustedY > 0) {
			runButtonAction("Go To Parent");
		}
		for (int i = 0; i < getCurrentMenu().getButtons().size(); i++) {
			MenuButton button = getCurrentMenu().getButtons().get(i);
			if (adjustedX > button.getX() && adjustedX < button.getX() + button.getWidth() && adjustedY > button.getY() && adjustedY < button.getY() + button.getHeight()) {
				if (button.visible) {
					runButtonAction(button.name);
				}
			}
		}
	}
	private static void runButtonAction(String name) {
		SystemData.buttonActionQueue.add(name);
	}
	public class MenuPane {
		private static final int BUTTON_WIDTH = 186;
		private static final int BUTTON_HEIGHT = 72;
		private static final int SPACING_X = 256;
		private static final int SPACING_Y = 57;
		private static final int START_X = 50;
		private static final int START_Y = 120;
		private int currentX = START_X;
		private int currentY = START_Y;
		private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
		public String name;
		public String parent;
		public MenuPane(String name, String parent) {
			this.name = name;
			this.parent = parent;
		}
		public void addButton(MenuButton button) {
			button.width = BUTTON_WIDTH;
			button.height = BUTTON_HEIGHT;
			button.posX = currentX;
			button.posY = currentY;
			buttons.add(button);
			currentX += (SPACING_X + BUTTON_WIDTH);
			if (currentX > 800 - BUTTON_WIDTH) {
				currentX = START_X;
				currentY += (SPACING_Y + BUTTON_HEIGHT);
			}
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
		public int getPosX(int menuStartX) {
			return (int)(posX * SystemData.uiZoom)+menuStartX;
		}
		public int getPosY(int menuStartY) {
			return (int)(posY * SystemData.uiZoom)+menuStartY;
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

