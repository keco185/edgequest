package com.mtautumn.edgequest.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;

public class SettingsData {
	public final int BACK_BUTTON_SIZE = 64;
	public final int BACK_BUTTON_PADDING = 12;
	public int tickLength = 30;
	public int targetFPS = 60;
	public int chunkSize = 12;
	public boolean showDiag = false;
	public float blockSize = 16;
	public float targetBlockSize = 16;
	public double zoomSpeed = 0.0004;
	public int screenWidth = 800;
	public int screenHeight = 600;
	public boolean isFullScreen = false;
	public boolean vSyncOn = true;
	public double moveSpeed = 3;
	public int maxItemDropAge = 300; //Age of item drops in seconds before they are removed


	public int upKey = Keyboard.KEY_W;
	public int downKey = Keyboard.KEY_S;
	public int rightKey = Keyboard.KEY_D;
	public int leftKey = Keyboard.KEY_A;
	public int sprintKey = Keyboard.KEY_LSHIFT;
	public int dodgeKey = Keyboard.KEY_SPACE;
	public int travelKey = Keyboard.KEY_TAB;
	public int menuKey = Keyboard.KEY_R;
	public int backpackKey = Keyboard.KEY_Q;
	public int zoomInKey = Keyboard.KEY_UP;
	public int zoomOutKey = Keyboard.KEY_DOWN;
	public int showDiagKey = Keyboard.KEY_GRAVE;
	public int placeTorchKey = Keyboard.KEY_F;
	public int consoleKey = Keyboard.KEY_T;
	public int exitKey = Keyboard.KEY_ESCAPE;
	public int actionKey = Keyboard.KEY_E;
	public int aimKey = Keyboard.KEY_LMETA;

	public Map<String, int[]> atlasMap = new ConcurrentHashMap<String, int[]>();
}
