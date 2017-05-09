package com.mtautumn.edgequest.utils.io;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.layers.OptionPane;

public class MouseUpdater {
	private boolean wasLeftMouseDown = false;
	private boolean wasRightMouseDown = false;
	private Renderer renderer;
	public MouseUpdater(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void updateMouse() {
		updateMouseHidden();
		Mouse.poll();
		updateMouseButtonStates();
		if (isTextPaneVisible()) {
			if (wasLeftMouseJustPressed()) {
				OptionPane.closeOptionPane();
			}
		} else {
			updateMousePosition();
			if (wasLeftMouseJustPressed()) {
				SystemData.autoWalk = false; //Disables A* walking
				if (SystemData.isKeyboardMenu) { //Is in game menu screen
					DataManager.menuButtonManager.buttonPressed(SystemData.mousePosition.x, SystemData.mousePosition.y); //Check button press
				} else if (SystemData.isGameOnLaunchScreen) { //Is on game launch screen
					renderer.launchScreenManager.buttonPressed(SystemData.mousePosition.x, SystemData.mousePosition.y); //Check button press
				} else if (SystemData.isKeyboardTravel && !SystemData.hideMouse) {
					initiateAStarWalking();
				}
			}
		}
		wasLeftMouseDown = Mouse.isButtonDown(0);
		wasRightMouseDown = Mouse.isButtonDown(1);
	}
	
	private static void updateMouseHidden() {
		try {
			if (SystemData.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(SystemData.mousePosition.x, SystemData.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void updateMouseButtonStates() {
		SystemData.leftMouseDown = Mouse.isButtonDown(0);
		SystemData.rightMouseDown = Mouse.isButtonDown(1);
	}
	
	private static void updateMousePosition() {
		SystemData.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
		double offsetX = (SystemData.screenX * Double.valueOf(SettingsData.blockSize) - Double.valueOf(SettingsData.screenWidth) / 2.0);
		double offsetY = (SystemData.screenY * Double.valueOf(SettingsData.blockSize) - Double.valueOf(SettingsData.screenHeight) / 2.0);
		SystemData.mouseXExact = (offsetX + SystemData.mousePosition.getX())/Double.valueOf(SettingsData.blockSize);
		SystemData.mouseYExact = (offsetY + SystemData.mousePosition.getY())/Double.valueOf(SettingsData.blockSize);
		SystemData.mouseX = (int) Math.floor(SystemData.mouseXExact);
		SystemData.mouseY = (int) Math.floor(SystemData.mouseYExact);
		SystemData.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(SystemData.mouseX) - Math.floor(DataManager.characterManager.characterEntity.getX()), 2.0)+Math.pow(Double.valueOf(SystemData.mouseY) - Math.floor(DataManager.characterManager.characterEntity.getY()), 2.0)) > 3.0);
	}
	
	private static void initiateAStarWalking() {
		SystemData.autoWalkX = SystemData.mouseX;
		SystemData.autoWalkY = SystemData.mouseY;
		DataManager.characterManager.characterEntity.setDestination(SystemData.autoWalkX, SystemData.autoWalkY);
	}
	
	private static boolean isTextPaneVisible() {
		return SystemData.inputText.size() + SystemData.noticeText.size() > 0;
	}
	
	private boolean wasLeftMouseJustPressed() {
		return Mouse.isButtonDown(0) && !wasLeftMouseDown;
	}
	
	private boolean wasRightMouseJustPressed() {
		return Mouse.isButtonDown(1) && !wasRightMouseDown;
	}
}
