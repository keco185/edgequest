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
	private DataManager dataManager;
	public Renderer renderer;
	private MouseUpdater mouseUpdater;
	private KeyboardUpdater keyboardUpdater;

	private int[] lastXFPS = new int[5];
	private int tempFPS;
	private long lastNanoTimeFPSGrabber;
	private double lastNanoPause;
	
	static GraphicsDevice device = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	public RendererManager(DataManager dataManager) {
		this.dataManager = dataManager;
		renderer = new Renderer(dataManager);
		mouseUpdater = new MouseUpdater(dataManager, renderer);
		keyboardUpdater = new KeyboardUpdater(dataManager);
	}
	
	@Override
	public void run() {
		prepareFPSCounting();
		setupWindow();
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
				mouseUpdater.updateMouse();
				keyboardUpdater.updateKeys();
				updateWindow();
				lastNanoPause += (1.0/Double.valueOf(dataManager.settings.targetFPS) - 1.0/Double.valueOf(dataManager.system.averagedFPS)) * 50000000.0;
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
		if (FPS / 5.0 > dataManager.system.averagedFPS) {
			FPS = dataManager.system.averagedFPS+1;
		}
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
		if (dataManager.system.uiZoom < 1) {
			dataManager.system.uiZoom = 0.8;
		}
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
	
	private void updateScreenCenter() {
		dataManager.system.screenX = dataManager.characterManager.characterEntity.getX();
		dataManager.system.screenY = dataManager.characterManager.characterEntity.getY();
	}
	
	private void setupWindow() {
		lastNanoTimeFPSGrabber = System.nanoTime();
		try {
			Thread.sleep(1000/dataManager.settings.targetFPS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		lastNanoPause = (1000000000.0/Double.valueOf(dataManager.settings.targetFPS));
		renderer.initGL(dataManager.settings.screenWidth, dataManager.settings.screenHeight);
		printOpenGLInfo();
		renderer.loadManagers();
		DefineBlockItems.setDefinitions(dataManager);
		dataManager.system.gameLoaded = true;
	}
}
