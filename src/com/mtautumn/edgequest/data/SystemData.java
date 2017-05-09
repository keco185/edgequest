package com.mtautumn.edgequest.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.blockitems.BlockItem;

public class SystemData {
	public static boolean loadingWorld = true;
	public static boolean characterLocationSet = false;
	public static boolean requestScreenUpdate = false;
	public static boolean requestGenUpdate = false;
	public static boolean gameLoaded = false;
	public static boolean updateDungeon = false;
	public static boolean showConsole = false;
	public static String consoleText = "";
	public static int miningX = 0;
	public static int miningY = 0;
	public static double blockDamage = 0;
	public static int os = 0;//0 = GNU/Linux, 1 = macOS, 2 = Windows
	public static boolean running = true;
	public static ArrayList<String> inputText = new ArrayList<String>();
	public static ArrayList<String> inputTextResponse = new ArrayList<String>();
	public static ArrayList<String> noticeText = new ArrayList<String>();
	public static ArrayList<String> buttonActionQueue = new ArrayList<String>();
	public static String lastInputMessage = "";
	public static boolean isKeyboardUp = false;		// work to remove
	public static boolean isKeyboardRight = false;		// work to remove
	public static boolean isKeyboardDown = false;		// work to remove
	public static boolean isKeyboardLeft = false;		// work to remove
	public static boolean isKeyboardSprint = false;	// work to remove
	public static boolean isKeyboardMenu = false;         
	public static boolean isKeyboardBackpack = false;     
	public static boolean isGameOnLaunchScreen = true;    
	public static boolean isLaunchScreenLoaded = false;   
	public static boolean isKeyboardTravel = false;	// work to remove
	public static boolean isAiming = false;			// work to remove
	public static boolean isMoveInput = false;
	public static int animationClock = 0;
	public static String timeReadable = "";
	public static String currentMenu = "Main Menu";
	public static int menuX = 0;
	public static int menuY = 0;
	public static int minTileX = 0;
	public static int maxTileX = 0;
	public static int minTileY = 0;
	public static int maxTileY = 0;
	public static int minTileXGen = 0;
	public static int maxTileXGen = 0;
	public static int minTileYGen = 0;
	public static int maxTileYGen = 0;
	public static double screenX = 0;
	public static double screenY = 0;
	public static boolean blockGenerationLastTick = true;
	public static boolean characterMoving = false;
	public static int averagedFPS = 0;
	public static Point mousePosition = new Point(); //coordinates on window
	public static int mouseX = 0; //block location
	public static int mouseY = 0; //block location
	public static double mouseXExact = 0.0;
	public static double mouseYExact = 0.0;
	public static boolean leftMouseDown = false;
	public static boolean rightMouseDown = false;
	public static boolean isMouseFar = false;
	public static boolean autoWalk = false;
	public static int autoWalkX = 0;
	public static int autoWalkY = 0;
	public static boolean setFullScreen = false;
	public static boolean setWindowed = false;
	public static boolean hideMouse = false;
	public static Map<Short, BlockItem> blockIDMap = new HashMap<Short, BlockItem>();
	public static Map<String, BlockItem> blockNameMap = new HashMap<String, BlockItem>();
	public static double uiZoom = 1;
}
