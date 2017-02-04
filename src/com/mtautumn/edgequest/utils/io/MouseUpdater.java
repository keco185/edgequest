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
	private DataManager dm;
	private Renderer renderer;
	public MouseUpdater(DataManager dm, Renderer renderer) {
		this.dm = dm;
		this.renderer = renderer;
	}
	
	public void updateMouse() {
		updateMouseHidden();
		Mouse.poll();
		updateMouseButtonStates();
		if (isTextPaneVisible()) {
			if (wasLeftMouseJustPressed()) {
				OptionPane.closeOptionPane(dm);
			}
		} else {
			updateMousePosition();
			if (wasLeftMouseJustPressed()) {
				dm.system.autoWalk = false; //Disables A* walking
				if (dm.system.isKeyboardMenu) { //Is in game menu screen
					dm.menuButtonManager.buttonPressed(dm.system.mousePosition.x, dm.system.mousePosition.y); //Check button press
				} else if (dm.system.isGameOnLaunchScreen) { //Is on game launch screen
					renderer.launchScreenManager.buttonPressed(dm.system.mousePosition.x, dm.system.mousePosition.y); //Check button press
				} else if (dm.system.isKeyboardTravel && !dm.system.hideMouse) {
					initiateAStarWalking();
				}
			}
		}
		wasLeftMouseDown = Mouse.isButtonDown(0);
		wasRightMouseDown = Mouse.isButtonDown(1);
	}
	
	private void updateMouseHidden() {
		try {
			if (dm.system.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(dm.system.mousePosition.x, dm.system.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateMouseButtonStates() {
		dm.system.leftMouseDown = Mouse.isButtonDown(0);
		dm.system.rightMouseDown = Mouse.isButtonDown(1);
	}
	
	private void updateMousePosition() {
		dm.system.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
		double offsetX = (dm.system.screenX * Double.valueOf(dm.settings.blockSize) - Double.valueOf(dm.settings.screenWidth) / 2.0);
		double offsetY = (dm.system.screenY * Double.valueOf(dm.settings.blockSize) - Double.valueOf(dm.settings.screenHeight) / 2.0);
		dm.system.mouseXExact = (offsetX + dm.system.mousePosition.getX())/Double.valueOf(dm.settings.blockSize);
		dm.system.mouseYExact = (offsetY + dm.system.mousePosition.getY())/Double.valueOf(dm.settings.blockSize);
		dm.system.mouseX = (int) Math.floor(dm.system.mouseXExact);
		dm.system.mouseY = (int) Math.floor(dm.system.mouseYExact);
		dm.system.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(dm.system.mouseX) - Math.floor(dm.characterManager.characterEntity.getX()), 2.0)+Math.pow(Double.valueOf(dm.system.mouseY) - Math.floor(dm.characterManager.characterEntity.getY()), 2.0)) > 3.0);
	}
	
	private void initiateAStarWalking() {
		dm.system.autoWalkX = dm.system.mouseX;
		dm.system.autoWalkY = dm.system.mouseY;
		dm.characterManager.characterEntity.setDestination(dm.system.autoWalkX, dm.system.autoWalkY);
	}
	
	private boolean isTextPaneVisible() {
		return dm.system.inputText.size() + dm.system.noticeText.size() > 0;
	}
	
	private boolean wasLeftMouseJustPressed() {
		return Mouse.isButtonDown(0) && !wasLeftMouseDown;
	}
	
	private boolean wasRightMouseJustPressed() {
		return Mouse.isButtonDown(1) && !wasRightMouseDown;
	}
}
