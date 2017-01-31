package com.mtautumn.edgequest.window.managers;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.mtautumn.edgequest.CharacterManager;
import com.mtautumn.edgequest.DefineBlockItems;
import com.mtautumn.edgequest.Entity;
import com.mtautumn.edgequest.KeyboardInput;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.layers.OptionPane;

public class RendererManager extends Thread {
	private DataManager dataManager;
	public Renderer renderer;
	private CharacterManager characterManager;


	KeyboardInput keyboard;
	int[] lastXFPS = new int[5];
	int tempFPS;
	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];
	public RendererManager(DataManager dataManager) {
		this.dataManager = dataManager;
		characterManager = dataManager.characterManager;
		keyboard = new KeyboardInput(dataManager);
		renderer = new Renderer(dataManager);
	}
	public void run() {
		prepareFPSCounting();
		long lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/dataManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		double lastNanoPause = (1000000000.0/Double.valueOf(dataManager.settings.targetFPS));
		renderer.initGL(dataManager.settings.screenWidth, dataManager.settings.screenHeight);
		renderer.loadManagers();
		DefineBlockItems.setDefinitions(dataManager);
		dataManager.system.gameLoaded = true;
		while (dataManager.system.running) {
			try {
				updateWindowSize();
				tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
				lastNanoTimeFPSGrabber = System.nanoTime();
				updateAverageFPS(tempFPS);
				if (dataManager.system.setFullScreen) {
					dataManager.settings.isFullScreen = true;
					dataManager.system.setFullScreen = false;

				}
				if (dataManager.system.setWindowed) {
					device.setFullScreenWindow(null);
					dataManager.system.setWindowed = false;
					dataManager.settings.isFullScreen = false;
				}
				if (!dataManager.system.isGameOnLaunchScreen) {
					updateScreenCenter();
					for( int i = 0; i < dataManager.savable.entities.size(); i++) {
						Entity entity = dataManager.savable.entities.get(i);
						entity.frameX = entity.getX();
						entity.frameY = entity.getY();
					}
				}
				updateMouse();
				updateKeys();
				updateWindow();
				lastNanoPause += (1.0/Double.valueOf(dataManager.settings.targetFPS) - 1.0/Double.valueOf(dataManager.system.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) lastNanoPause = 0;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Display.destroy();
		System.exit(0);
	}
	private void updateWindow() {
		updateZoom();
		findViewDimensions();
		renderer.drawFrame();
	}
	private long lastZoomUpdate = -1;
	private void updateZoom() {
		double timeStep;
		if (lastZoomUpdate == -1) {
			lastZoomUpdate = System.currentTimeMillis();
			timeStep = 1;
		} else {
			timeStep = System.currentTimeMillis() - lastZoomUpdate;
			lastZoomUpdate = System.currentTimeMillis();
		}
		timeStep /= 17;
		double pixelSize = 1.0 /(dataManager.settings.blockSize/dataManager.system.uiZoom);
		double targetPixelSize = 1.0 /(dataManager.settings.targetBlockSize/dataManager.system.uiZoom);
		if (pixelSize < targetPixelSize) {
			if (pixelSize + dataManager.settings.zoomSpeed * timeStep > targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize += dataManager.settings.zoomSpeed * timeStep;
			}
			dataManager.settings.blockSize = (float) ((float) (1.0/pixelSize) * dataManager.system.uiZoom);
			dataManager.system.requestScreenUpdate = true;
		} else if (pixelSize > targetPixelSize) {
			if (pixelSize - dataManager.settings.zoomSpeed * timeStep < targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize -= dataManager.settings.zoomSpeed * timeStep;
			}
			dataManager.settings.blockSize = (float) ((float) (1.0/pixelSize) * dataManager.system.uiZoom);
			dataManager.system.requestScreenUpdate = true;
		}
	}
	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > dataManager.system.averagedFPS) FPS = dataManager.system.averagedFPS+1;
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		dataManager.system.averagedFPS = (int) Math.ceil(Double.valueOf(fpsSum) / Double.valueOf(lastXFPS.length));
	}
	private void prepareFPSCounting() {
		dataManager.system.averagedFPS = dataManager.settings.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = dataManager.settings.targetFPS;
		}
	}
	private void updateWindowSize() {
		if (dataManager.settings.screenWidth != Display.getWidth() || dataManager.settings.screenHeight != Display.getHeight()) {
			dataManager.settings.screenWidth = Display.getWidth();
			dataManager.settings.screenHeight = Display.getHeight();
			dataManager.system.blockGenerationLastTick = true;
		}
		if (dataManager.settings.screenHeight * 1.6 > dataManager.settings.screenWidth) {
			dataManager.system.uiZoom = dataManager.settings.screenHeight / 800;
		} else {
			dataManager.system.uiZoom = dataManager.settings.screenWidth / 1280;
		}
		if (dataManager.system.uiZoom < 1) dataManager.system.uiZoom = 0.8;
	}
	private boolean wasMouseDown = false;
	private void updateMouse() {
		try {
			if (dataManager.system.hideMouse) {
				if (!Mouse.isGrabbed()) {
					Mouse.setGrabbed(true);
				}
			} else {
				if (Mouse.isGrabbed()) {
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(dataManager.system.mousePosition.x, dataManager.system.mousePosition.y);
					Mouse.updateCursor();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Mouse.poll();
		dataManager.system.leftMouseDown = Mouse.isButtonDown(0);
		dataManager.system.rightMouseDown = Mouse.isButtonDown(1);
		if (dataManager.system.inputText.size() + dataManager.system.noticeText.size() > 0) {
			if (Mouse.isButtonDown(0) && !wasMouseDown) {
				OptionPane.closeOptionPane(dataManager);
			}
		} else {
			dataManager.system.mousePosition = new Point(Mouse.getX(), Display.getHeight() - Mouse.getY());
			int mouseX = dataManager.system.mousePosition.x;
			int mouseY = dataManager.system.mousePosition.y;
			double offsetX = (dataManager.system.screenX * Double.valueOf(dataManager.settings.blockSize) - Double.valueOf(dataManager.settings.screenWidth) / 2.0);
			double offsetY = (dataManager.system.screenY * Double.valueOf(dataManager.settings.blockSize) - Double.valueOf(dataManager.settings.screenHeight) / 2.0);
			dataManager.system.mouseXExact = (offsetX + dataManager.system.mousePosition.getX())/Double.valueOf(dataManager.settings.blockSize);
			dataManager.system.mouseYExact = (offsetY + dataManager.system.mousePosition.getY())/Double.valueOf(dataManager.settings.blockSize);
			dataManager.system.mouseX = (int) Math.floor(dataManager.system.mouseXExact);
			dataManager.system.mouseY = (int) Math.floor(dataManager.system.mouseYExact);
			dataManager.system.isMouseFar =  (Math.sqrt(Math.pow(Double.valueOf(dataManager.system.mouseX) - Math.floor(dataManager.characterManager.characterEntity.getX()), 2.0)+Math.pow(Double.valueOf(dataManager.system.mouseY) - Math.floor(dataManager.characterManager.characterEntity.getY()), 2.0)) > 3.0);
			if (Mouse.isButtonDown(0) && !wasMouseDown) {
				dataManager.system.autoWalk = false;
				if (dataManager.system.isKeyboardMenu) {
					dataManager.menuButtonManager.buttonPressed(mouseX, mouseY);
				} else if (dataManager.system.isGameOnLaunchScreen) {
					renderer.launchScreenManager.buttonPressed(mouseX, mouseY);
				} else if (dataManager.system.isKeyboardTravel && !dataManager.system.hideMouse){
					dataManager.system.autoWalkX = dataManager.system.mouseX;
					dataManager.system.autoWalkY = dataManager.system.mouseY;
					dataManager.characterManager.characterEntity.setDestination(dataManager.system.autoWalkX, dataManager.system.autoWalkY);
				}
			}
		}
		wasMouseDown = Mouse.isButtonDown(0);
	}
	private void findViewDimensions() {
		if (dataManager.system.characterMoving || dataManager.system.blockGenerationLastTick || dataManager.system.requestScreenUpdate) {
			dataManager.system.requestScreenUpdate = false;
			double tileWidth = Double.valueOf(dataManager.settings.screenWidth) / dataManager.settings.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(dataManager.settings.screenHeight) / dataManager.settings.blockSize / 2.0 + 1;
			dataManager.system.minTileX = (int) (dataManager.system.screenX - tileWidth - 1);
			dataManager.system.maxTileX = (int) (dataManager.system.screenX + tileWidth);
			dataManager.system.minTileY = (int) (dataManager.system.screenY - tileHeight - 1);
			dataManager.system.maxTileY = (int) (dataManager.system.screenY + tileHeight);
			tileWidth = Double.valueOf(dataManager.settings.screenWidth) / (16.0 * dataManager.system.uiZoom) / 2.0 + 1;
			tileHeight = Double.valueOf(dataManager.settings.screenHeight) / (16.0 * dataManager.system.uiZoom) / 2.0 + 1;
			dataManager.system.minTileXGen = (int) (dataManager.system.screenX - tileWidth - 1);
			dataManager.system.maxTileXGen = (int) (dataManager.system.screenX + tileWidth);
			dataManager.system.minTileYGen = (int) (dataManager.system.screenY - tileHeight - 1);
			dataManager.system.maxTileYGen = (int) (dataManager.system.screenY + tileHeight);
		}
	}
	private static boolean[] wasKeyDown = new boolean[256];
	public void updateKeys() {
		try {
			Keyboard.poll();
			boolean keyUp = Keyboard.isKeyDown(dataManager.settings.upKey);
			boolean keyDown = Keyboard.isKeyDown(dataManager.settings.downKey);
			boolean keyLeft = Keyboard.isKeyDown(dataManager.settings.leftKey);
			boolean keyRight = Keyboard.isKeyDown(dataManager.settings.rightKey);
			boolean keySprint = Keyboard.isKeyDown(dataManager.settings.sprintKey);
			boolean keyTravel = Keyboard.isKeyDown(dataManager.settings.travelKey);
			boolean keyMenu = Keyboard.isKeyDown(dataManager.settings.menuKey);
			boolean keyBackpack = Keyboard.isKeyDown(dataManager.settings.backpackKey);
			boolean keyZoomIn = Keyboard.isKeyDown(dataManager.settings.zoomInKey);
			boolean keyZoomOut = Keyboard.isKeyDown(dataManager.settings.zoomOutKey);
			boolean keyShowDiag = Keyboard.isKeyDown(dataManager.settings.showDiagKey);
			boolean keyPlaceTorch = Keyboard.isKeyDown(dataManager.settings.placeTorchKey);
			boolean keyConsole = Keyboard.isKeyDown(dataManager.settings.consoleKey);
			boolean keyAction = Keyboard.isKeyDown(dataManager.settings.actionKey);
			boolean keyExit = Keyboard.isKeyDown(dataManager.settings.exitKey);
			if (dataManager.system.inputText.size() + dataManager.system.noticeText.size() > 0 || dataManager.system.showConsole) {
				keyboard.poll();
				keyboard.wasConsoleUp = dataManager.system.showConsole;
			} else {
				if (!dataManager.system.isGameOnLaunchScreen) {
					dataManager.system.isKeyboardSprint = keySprint;
					dataManager.system.isKeyboardTravel = keyTravel;

					if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
						dataManager.savable.hotBarSelection = 0;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
						dataManager.savable.hotBarSelection = 1;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
						dataManager.savable.hotBarSelection = 2;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
						dataManager.savable.hotBarSelection = 3;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
						dataManager.savable.hotBarSelection = 4;
					}
					if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
						dataManager.savable.hotBarSelection = 5;
					}
					if (Keyboard.isKeyDown(dataManager.settings.aimKey) || dataManager.system.rightMouseDown) {
						dataManager.system.isAiming = true;
					} else {
						dataManager.system.isAiming = false;
					}
					if (!dataManager.system.autoWalk) {
						dataManager.system.isKeyboardUp = keyUp;
						dataManager.system.isKeyboardRight = keyRight;
						dataManager.system.isKeyboardDown = keyDown;
						dataManager.system.isKeyboardLeft = keyLeft;
					} else {
						if (keyUp || keyDown || keyRight || keyLeft)
							dataManager.system.autoWalk = false;

					}
					if (keyZoomIn && !wasKeyDown[dataManager.settings.zoomInKey]) {
						if (dataManager.settings.targetBlockSize < 128 * dataManager.system.uiZoom) {
							dataManager.settings.targetBlockSize *= 2;
							dataManager.system.blockGenerationLastTick = true;
						}
					}
					if (keyZoomOut && !wasKeyDown[dataManager.settings.zoomOutKey]) {
						if (dataManager.settings.targetBlockSize > 16 * dataManager.system.uiZoom) {
							dataManager.settings.targetBlockSize /= 2;
							dataManager.system.blockGenerationLastTick = true;
						}	
					}
					if (keyShowDiag && !wasKeyDown[dataManager.settings.showDiagKey])
						dataManager.settings.showDiag = !dataManager.settings.showDiag;

					if (keyMenu && !wasKeyDown[dataManager.settings.menuKey])
						dataManager.system.isKeyboardMenu = !dataManager.system.isKeyboardMenu;

					if (keyPlaceTorch && !wasKeyDown[dataManager.settings.placeTorchKey]) 
						characterManager.charPlaceTorch();

					if (keyBackpack && !wasKeyDown[dataManager.settings.backpackKey]) 
						dataManager.system.isKeyboardBackpack = !dataManager.system.isKeyboardBackpack;

					if (keyConsole && !wasKeyDown[dataManager.settings.consoleKey])
						dataManager.system.showConsole = true;
					if (keyExit && !wasKeyDown[dataManager.settings.exitKey]) {
						if (dataManager.system.showConsole) {
							dataManager.system.showConsole = false;
						} else if (dataManager.system.isKeyboardMenu) {
							dataManager.system.isKeyboardMenu = false;
						} else if (dataManager.system.isKeyboardBackpack) {
							dataManager.system.isKeyboardBackpack = false;
						}
					}
					if (keyAction && !wasKeyDown[dataManager.settings.actionKey]) {
						if (dataManager.itemDropManager.isItemInRange(dataManager.characterManager.characterEntity)) {
							dataManager.itemDropManager.putNearbyItemsInBackpack();
						} else if (dataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeonUp")) {
							dataManager.characterManager.characterEntity.dungeonLevel -= 1;
							dataManager.savable.dungeonLevel -= 1;
						} else if (dataManager.characterManager.characterEntity.getRelativeStructureBlock(0, 0).isName("dungeon")) {
							dataManager.characterManager.characterEntity.dungeonLevel += 1;
							dataManager.savable.dungeonLevel += 1;
						}
					}
				}

			}
			if (keyboard.wasConsoleUp) keyboard.wasConsoleUp = dataManager.system.showConsole;
			if (keyExit && !wasKeyDown[dataManager.settings.exitKey]) {
				if (dataManager.system.noticeText.size() > 0) {
					dataManager.system.noticeText.remove(dataManager.system.noticeText.size() - 1);
				} else if (dataManager.system.inputText.size() > 0) {
					dataManager.system.inputText.remove(dataManager.system.inputText.size() - 1);
					dataManager.system.inputTextResponse.remove(dataManager.system.inputTextResponse.size() - 1);
					dataManager.system.lastInputMessage = null;
				} else if (dataManager.system.showConsole) {
					dataManager.system.showConsole = false;
				}
			}
			wasKeyDown[dataManager.settings.menuKey] = keyMenu;
			wasKeyDown[dataManager.settings.backpackKey] = keyBackpack;
			wasKeyDown[dataManager.settings.zoomInKey] = keyZoomIn;
			wasKeyDown[dataManager.settings.zoomOutKey] = keyZoomOut;
			wasKeyDown[dataManager.settings.showDiagKey] = keyShowDiag;
			wasKeyDown[dataManager.settings.placeTorchKey] = keyPlaceTorch;
			wasKeyDown[dataManager.settings.consoleKey] = keyConsole;
			wasKeyDown[dataManager.settings.actionKey] = keyAction;
			wasKeyDown[dataManager.settings.exitKey] = keyExit;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void updateScreenCenter() {
		dataManager.system.screenX = dataManager.characterManager.characterEntity.getX();
		dataManager.system.screenY = dataManager.characterManager.characterEntity.getY();
	}
}
