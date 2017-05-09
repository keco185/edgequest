package com.mtautumn.edgequest.threads;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.mtautumn.edgequest.DefineBlockItems;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.entities.Entity;
import com.mtautumn.edgequest.utils.io.KeyboardUpdater;
import com.mtautumn.edgequest.utils.io.MouseUpdater;
import com.mtautumn.edgequest.window.Renderer;

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
	
	@Override
	public void run() {
		prepareFPSCounting();
		setupWindow();
		while (DataManager.system.running) {
			try {
				updateWindowSize();
				tempFPS = (int) (1000000000 / (System.nanoTime() - lastNanoTimeFPSGrabber));
				lastNanoTimeFPSGrabber = System.nanoTime();
				updateAverageFPS(tempFPS);
				if (DataManager.system.setFullScreen) {
					DataManager.settings.isFullScreen = true;
					DataManager.system.setFullScreen = false;

				}
				if (DataManager.system.setWindowed) {
					device.setFullScreenWindow(null);
					DataManager.system.setWindowed = false;
					DataManager.settings.isFullScreen = false;
				}
				if (!DataManager.system.isGameOnLaunchScreen) {
					updateScreenCenter();
					for( int i = 0; i < DataManager.savable.entities.size(); i++) {
						Entity entity = DataManager.savable.entities.get(i);
						entity.frameX = entity.getX();
						entity.frameY = entity.getY();
					}
				}
				mouseUpdater.updateMouse();
				keyboardUpdater.updateKeys();
				updateWindow();
				lastNanoPause += (1.0/Double.valueOf(DataManager.settings.targetFPS) - 1.0/Double.valueOf(DataManager.system.averagedFPS)) * 50000000.0;
				if (lastNanoPause < 0) {
					lastNanoPause = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
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
		double pixelSize = 1.0 /(DataManager.settings.blockSize/DataManager.system.uiZoom);
		double targetPixelSize = 1.0 /(DataManager.settings.targetBlockSize/DataManager.system.uiZoom);
		if (pixelSize < targetPixelSize) {
			if (pixelSize + DataManager.settings.zoomSpeed * timeStep > targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize += DataManager.settings.zoomSpeed * timeStep;
			}
			DataManager.settings.blockSize = (float) ((float) (1.0/pixelSize) * DataManager.system.uiZoom);
			DataManager.system.requestScreenUpdate = true;
		} else if (pixelSize > targetPixelSize) {
			if (pixelSize - DataManager.settings.zoomSpeed * timeStep < targetPixelSize) {
				pixelSize = targetPixelSize;
			} else {
				pixelSize -= DataManager.settings.zoomSpeed * timeStep;
			}
			DataManager.settings.blockSize = (float) ((float) (1.0/pixelSize) * DataManager.system.uiZoom);
			DataManager.system.requestScreenUpdate = true;
		}
	}
	
	private void updateAverageFPS(int FPS) {
		if (FPS / 5.0 > DataManager.system.averagedFPS) {
			FPS = DataManager.system.averagedFPS+1;
		}
		int fpsSum = 0;
		for (int i = lastXFPS.length - 1; i > 0; i--) {
			lastXFPS[i] = lastXFPS[i - 1];
			fpsSum += lastXFPS[i];
		}
		lastXFPS[0] = FPS;
		fpsSum += lastXFPS[0];
		DataManager.system.averagedFPS = (int) Math.ceil(Double.valueOf(fpsSum) / Double.valueOf(lastXFPS.length));
	}
	
	private void prepareFPSCounting() {
		DataManager.system.averagedFPS = DataManager.settings.targetFPS;
		for (int i = 0; i< lastXFPS.length; i++) {
			lastXFPS[i] = DataManager.settings.targetFPS;
		}
	}
	
	private static void updateWindowSize() {
		if (DataManager.settings.screenWidth != Display.getWidth() || DataManager.settings.screenHeight != Display.getHeight()) {
			DataManager.settings.screenWidth = Display.getWidth();
			DataManager.settings.screenHeight = Display.getHeight();
			DataManager.system.blockGenerationLastTick = true;
		}
		if (DataManager.settings.screenHeight * 1.6 > DataManager.settings.screenWidth) {
			DataManager.system.uiZoom = DataManager.settings.screenHeight / 800;
		} else {
			DataManager.system.uiZoom = DataManager.settings.screenWidth / 1280;
		}
		if (DataManager.system.uiZoom < 1) {
			DataManager.system.uiZoom = 0.8;
		}
	}
	
	private static void findViewDimensions() {
		if (DataManager.system.characterMoving || DataManager.system.blockGenerationLastTick || DataManager.system.requestScreenUpdate) {
			DataManager.system.requestScreenUpdate = false;
			double tileWidth = Double.valueOf(DataManager.settings.screenWidth) / DataManager.settings.blockSize / 2.0 + 1;
			double tileHeight = Double.valueOf(DataManager.settings.screenHeight) / DataManager.settings.blockSize / 2.0 + 1;
			DataManager.system.minTileX = (int) (DataManager.system.screenX - tileWidth - 1);
			DataManager.system.maxTileX = (int) (DataManager.system.screenX + tileWidth);
			DataManager.system.minTileY = (int) (DataManager.system.screenY - tileHeight - 1);
			DataManager.system.maxTileY = (int) (DataManager.system.screenY + tileHeight);
			tileWidth = Double.valueOf(DataManager.settings.screenWidth) / (16.0 * DataManager.system.uiZoom) / 2.0 + 1;
			tileHeight = Double.valueOf(DataManager.settings.screenHeight) / (16.0 * DataManager.system.uiZoom) / 2.0 + 1;
			DataManager.system.minTileXGen = (int) (DataManager.system.screenX - tileWidth - 1);
			DataManager.system.maxTileXGen = (int) (DataManager.system.screenX + tileWidth);
			DataManager.system.minTileYGen = (int) (DataManager.system.screenY - tileHeight - 1);
			DataManager.system.maxTileYGen = (int) (DataManager.system.screenY + tileHeight);
		}
	}
	
	private static void updateScreenCenter() {
		DataManager.system.screenX = DataManager.characterManager.characterEntity.getX();
		DataManager.system.screenY = DataManager.characterManager.characterEntity.getY();
	}
	
	private void setupWindow() {
		lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/DataManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		lastNanoPause = (1000000000.0/Double.valueOf(DataManager.settings.targetFPS));
		renderer.initGL(DataManager.settings.screenWidth, DataManager.settings.screenHeight);
		printOpenGLInfo();
		renderer.loadManagers();
		DefineBlockItems.setDefinitions();
		DataManager.system.gameLoaded = true;
	}
}
