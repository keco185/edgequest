package com.mtautumn.edgequest.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.input.Keyboard;

public class SettingsData {
	public static final int BACK_BUTTON_SIZE = 64;
	public static final int BACK_BUTTON_PADDING_LEFT = 50;
	public static final int BACK_BUTTON_PADDING_TOP = 36;
	public static int tickLength = 30;
	public static int targetFPS = 60;
	public static int chunkSize = 12;
	public static boolean showDiag = false;
	public static float blockSize = 16;
	public static float targetBlockSize = 16;
	public static double zoomSpeed = 0.0004;
	public static int screenWidth = 800;
	public static int screenHeight = 600;
	public static boolean isFullScreen = false;
	public static boolean vSyncOn = true;
	public static double moveSpeed = 3;
	public static int maxItemDropAge = 300; //Age of item drops in seconds before they are removed
	public static boolean fastGraphics = false;


	public static int upKey = Keyboard.KEY_W;
	public static int downKey = Keyboard.KEY_S;
	public static int rightKey = Keyboard.KEY_D;
	public static int leftKey = Keyboard.KEY_A;
	public static int sprintKey = Keyboard.KEY_LSHIFT;
	public static int dodgeKey = Keyboard.KEY_SPACE;
	public static int travelKey = Keyboard.KEY_TAB;
	public static int menuKey = Keyboard.KEY_R;
	public static int backpackKey = Keyboard.KEY_Q;
	public static int zoomInKey = Keyboard.KEY_UP;
	public static int zoomOutKey = Keyboard.KEY_DOWN;
	public static int showDiagKey = Keyboard.KEY_GRAVE;
	public static int placeTorchKey = Keyboard.KEY_F;
	public static int consoleKey = Keyboard.KEY_T;
	public static int exitKey = Keyboard.KEY_ESCAPE;
	public static int actionKey = Keyboard.KEY_E;
	public static int aimKey = Keyboard.KEY_LMETA;

	public static Map<String, int[]> atlasMap = new ConcurrentHashMap<String, int[]>();
}
