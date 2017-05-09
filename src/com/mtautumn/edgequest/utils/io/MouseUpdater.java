package com.mtautumn.edgequest.utils.io;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.data.DataManager;
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
				DataManager.system.autoWalk = false; //Disables A* walking
				if (DataManager.system.isKeyboardMenu) { //Is in game menu screen
					DataManager.menuButtonManager.buttonPressed(DataManager.system.mousePosition.x, DataManager.system.mousePosition.y); //Check button press
				} else if (DataManager.system.isGameOnLaunchScreen) { //Is on game launch screen
					renderer.launchScreenManager.buttonPressed(DataManager.system.mousePosition.x, DataManager.system.mousePosition.y); //Check button press
				} else if (DataManager.system.isKeyboardTravel && !DataManager.system.hideMouse) {
					initiateAStarWalking();
				}
			}
		}
		wasLeftMouseDown = Mouse.isButtonDown(0);
		wasRightMouseDown = Mouse.isButtonDown(1);
	}
	
	private static void updateMouseHidden() {
		try {
			if (DataManager.system.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(DataManager.system.mousePosition.x, DataManager.system.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void updateMouseButtonStates() {
		DataManager.system.leftMouseDown = Mouse.isButtonDown(0);
		DataManager.system.rightMouseDown = Mouse.isButtonDown(1);
	}
	
	private static void updateMousePosition() {
		DataManager.system.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
		double offsetX = (DataManager.system.screenX * Double.valueOf(DataManager.settings.blockSize) - Double.valueOf(DataManager.settings.screenWidth) / 2.0);
		double offsetY = (DataManager.system.screenY * Double.valueOf(DataManager.settings.blockSize) - Double.valueOf(DataManager.settings.screenHeight) / 2.0);
		DataManager.system.mouseXExact = (offsetX + DataManager.system.mousePosition.getX())/Double.valueOf(DataManager.settings.blockSize);
		DataManager.system.mouseYExact = (offsetY + DataManager.system.mousePosition.getY())/Double.valueOf(DataManager.settings.blockSize);
		DataManager.system.mouseX = (int) Math.floor(DataManager.system.mouseXExact);
		DataManager.system.mouseY = (int) Math.floor(DataManager.system.mouseYExact);
		DataManager.system.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(DataManager.system.mouseX) - Math.floor(DataManager.characterManager.characterEntity.getX()), 2.0)+Math.pow(Double.valueOf(DataManager.system.mouseY) - Math.floor(DataManager.characterManager.characterEntity.getY()), 2.0)) > 3.0);
	}
	
	private static void initiateAStarWalking() {
		DataManager.system.autoWalkX = DataManager.system.mouseX;
		DataManager.system.autoWalkY = DataManager.system.mouseY;
		DataManager.characterManager.characterEntity.setDestination(DataManager.system.autoWalkX, DataManager.system.autoWalkY);
	}
	
	private static boolean isTextPaneVisible() {
		return DataManager.system.inputText.size() + DataManager.system.noticeText.size() > 0;
	}
	
	private boolean wasLeftMouseJustPressed() {
		return Mouse.isButtonDown(0) && !wasLeftMouseDown;
	}
	
	private boolean wasRightMouseJustPressed() {
		return Mouse.isButtonDown(1) && !wasRightMouseDown;
	}
}
