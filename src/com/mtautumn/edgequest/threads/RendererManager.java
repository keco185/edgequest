package com.mtautumn.edgequest.threads;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.mtautumn.edgequest.DefineBlockItems;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.utils.io.KeyboardUpdater;
import com.mtautumn.edgequest.utils.io.MouseUpdater;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.layers.Lighting;

public class RendererManager extends Thread {
	public Renderer renderer;
	private MouseUpdater mouseUpdater;
	private KeyboardUpdater keyboardUpdater;

	private int[] lastXFPS = new int[5];
	private int tempFPS;
	private long lastNanoTimeFPSGrabber;
	private double lastNanoPause;

	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public RendererManager() {
		renderer = new Renderer();
		mouseUpdater = new MouseUpdater(renderer);
		keyboardUpdater = new KeyboardUpdater();
	}

	UpdatePlayerLighting playerLighting;

	@Override
	public void run() {
		playerLighting = new UpdatePlayerLighting();
		prepareFPSCounting();
		setupWindow();
		while (SystemData.running) {
			try {
				updateWindowSize();
				tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
				lastNanoTimeFPSGrabber = System.nanoTime();
				updateAverageFPS(tempFPS);
				if (SystemData.setFullScreen) {
					SettingsData.isFullScreen = true;
					SystemData.setFullScreen = false;

				}
				if (SystemData.setWindowed) {
					device.setFullScreenWindow(null);
					SystemData.setWindowed = false;
					SettingsData.isFullScreen = false;
				}
				mouseUpdater.updateMouse();
				keyboardUpdater.updateKeys();
				updateWindow();
				if (!SystemData.isGameOnLaunchScreen) {
					updateScreenCenter();
					for( int i = 0; i < DataManager.savable.entities.size(); i++) {
						Entity entity = DataManager.savable.entities.get(i);
						entity.frameX = entity.getX();
						entity.frameY = entity.getY();
					}
					if (DataManager.world.getBrightness() < 1 && !DataManager.world.noLighting) {
						if (SettingsData.fastGraphics) {
							if (!playerLighting.isAlive()) {
								playerLighting = new UpdatePlayerLighting();
								playerLighting.start();
							}
						} else {
							Lighting.updatePlayerLight();
						}
					}
				}
				lastNanoPause += (1.0/Double.valueOf(SettingsData.targetFPS) - 1.0/Double.valueOf(SystemData.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) {
					lastNanoPause = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Display.isCloseRequested()) {
				SystemData.running = false;
			}
		}
		Display.destroy();
		System.exit(0);
	}

	private static void printOpenGLInfo() {
		System.out.println("OS name " + System.getProperty("os.name"));
		System.out.println("OS version " + System.getProperty("os.version"));
		System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
		System.out.println("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));	
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
		double pixelSize = 1.0 /(SettingsData.blockSize/SystemData.uiZoom);
		double targetPixelSize = 1.0 /(SettingsData.targetBlockSize/SystemData.uiZoom);
		if (pixelSize < targetPixelSize) {
			if (pixelSize + SettingsData.zoomSpeed * timeStep > targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize += SettingsData.zoomSpeed * timeStep;
			}
			SettingsData.blockSize = (float) ((float) (1.0/pixelSize) * SystemData.uiZoom);
			SystemData.requestScreenUpdate = true;
		} else if (pixelSize > targetPixelSize) {
			if (pixelSize - SettingsData.zoomSpeed * timeStep < targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize -= SettingsData.zoomSpeed * timeStep;
			}
			SettingsData.blockSize = (float) ((float) (1.0/pixelSize) * SystemData.uiZoom);
			SystemData.requestScreenUpdate = true;
		}
	}

	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > SystemData.averagedFPS) {
			FPS = SystemData.averagedFPS+1;
		}
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		SystemData.averagedFPS = (int) Math.ceil(Double.valueOf(fpsSum) / Double.valueOf(lastXFPS.length));
	}

	private void prepareFPSCounting() {
		SystemData.averagedFPS = SettingsData.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = SettingsData.targetFPS;
		}
	}

	private static void updateWindowSize() {
		if (SettingsData.screenWidth != Display.getWidth() || SettingsData.screenHeight != Display.getHeight()) {
			SettingsData.screenWidth = Display.getWidth();
			SettingsData.screenHeight = Display.getHeight();
			SystemData.blockGenerationLastTick = true;
		}
		if (SettingsData.screenHeight * 1.6 > SettingsData.screenWidth) {
			SystemData.uiZoom = SettingsData.screenHeight / 800;
		} else {
			SystemData.uiZoom = SettingsData.screenWidth / 1280;
		}
		if (SystemData.uiZoom < 1) {
			SystemData.uiZoom = 0.8;
		}
	}

	private static void findViewDimensions() {
		if (SystemData.characterMoving || SystemData.blockGenerationLastTick || SystemData.requestScreenUpdate) {
			SystemData.requestScreenUpdate = false;
			double tileWidth = Double.valueOf(SettingsData.screenWidth) / SettingsData.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(SettingsData.screenHeight) / SettingsData.blockSize / 2.0 + 1;
			SystemData.minTileX = (int) (SystemData.screenX - tileWidth - 1);
			SystemData.maxTileX = (int) (SystemData.screenX + tileWidth);
			SystemData.minTileY = (int) (SystemData.screenY - tileHeight - 1);
			SystemData.maxTileY = (int) (SystemData.screenY + tileHeight);
			tileWidth = Double.valueOf(SettingsData.screenWidth) / (16.0 * SystemData.uiZoom) / 2.0 + 1;
			tileHeight = Double.valueOf(SettingsData.screenHeight) / (16.0 * SystemData.uiZoom) / 2.0 + 1;
			SystemData.minTileXGen = (int) (SystemData.screenX - tileWidth - 1);
			SystemData.maxTileXGen = (int) (SystemData.screenX + tileWidth);
			SystemData.minTileYGen = (int) (SystemData.screenY - tileHeight - 1);
			SystemData.maxTileYGen = (int) (SystemData.screenY + tileHeight);
		}
	}

	private static void updateScreenCenter() {
		SystemData.screenX = CharacterManager.characterEntity.getX();
		SystemData.screenY = CharacterManager.characterEntity.getY();
	}

	private void setupWindow() {
		lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/SettingsData.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		lastNanoPause = (1000000000.0/Double.valueOf(SettingsData.targetFPS));
		renderer.initGL(SettingsData.screenWidth, SettingsData.screenHeight);
		printOpenGLInfo();
		renderer.loadManagers();
		DefineBlockItems.setDefinitions();
		SystemData.gameLoaded = true;
	}
}
