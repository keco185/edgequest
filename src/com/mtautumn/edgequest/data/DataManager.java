package com.mtautumn.edgequest.data;

import com.mtautumn.edgequest.console.ConsoleManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.threads.AnimationClock;
import com.mtautumn.edgequest.threads.AttackManager;
import com.mtautumn.edgequest.threads.BackpackManager;
import com.mtautumn.edgequest.threads.BlockUpdateManager;
import com.mtautumn.edgequest.threads.ButtonActionManager;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.threads.ChunkManager;
import com.mtautumn.edgequest.threads.DamagePostManager;
import com.mtautumn.edgequest.threads.EntitySpawn;
import com.mtautumn.edgequest.threads.EntityUpdater;
import com.mtautumn.edgequest.threads.GameClock;
import com.mtautumn.edgequest.threads.ItemDropManager;
import com.mtautumn.edgequest.threads.LightingUpdater;
import com.mtautumn.edgequest.threads.ParticleManager;
import com.mtautumn.edgequest.threads.ProjectileManager;
import com.mtautumn.edgequest.threads.RendererManager;
import com.mtautumn.edgequest.threads.TerrainManager;
import com.mtautumn.edgequest.threads.WeatherManager;
import com.mtautumn.edgequest.utils.WorldUtils;
import com.mtautumn.edgequest.window.MenuButtonManager;

public class DataManager {
	public static SystemData system = new SystemData();
	public static SavableData savable = new SavableData();
	public static SettingsData settings = new SettingsData();

	public static MenuButtonManager menuButtonManager;
	public static BackpackManager backpackManager = new BackpackManager();
	public static BlockUpdateManager blockUpdateManager = new BlockUpdateManager();
	public static CharacterManager characterManager = new CharacterManager();
	public static RendererManager rendererManager = new RendererManager();
	public static TerrainManager terrainManager = new TerrainManager();
	public static GameClock gameClock = new GameClock();
	public static AnimationClock animationClock = new AnimationClock();
	public static ButtonActionManager buttonActionManager = new ButtonActionManager();
	public static ConsoleManager consoleManager = new ConsoleManager();
	public static AttackManager attackManager = new AttackManager();
	public static EntitySpawn entitySpawn = new EntitySpawn();
	public static ProjectileManager projectileManager = new ProjectileManager();
	public static DamagePostManager damagePostManager = new DamagePostManager();
	public static LightingUpdater lightingUpdater = new LightingUpdater();
	public static ParticleManager particleManager = new ParticleManager();
	public static ItemDropManager itemDropManager = new ItemDropManager();
	public static WeatherManager weatherManager = new WeatherManager();
	public static ChunkManager chunkManager = new ChunkManager();

	public static WorldUtils world = new WorldUtils();
	public static EntityUpdater entityUpdater = new EntityUpdater();

	// Initialize a new game
	public static void newGame() {
		resetTerrain();
		savable.time = 800;
		for (int i = 0; i< savable.backpackItems.length; i++) {
			for (int j = 0; j< savable.backpackItems[i].length; j++) {
				savable.backpackItems[i][j] = new ItemSlot();
			}
		}
		savable.entities.clear();
		characterManager.createCharacterEntity();
		SystemData.blockGenerationLastTick = true;
		SystemData.isGameOnLaunchScreen = false;
		SystemData.isLaunchScreenLoaded = false;
		SystemData.requestGenUpdate = true;
	}

	// Reset the terrain
	public static void resetTerrain() {
		terrainManager.terrainGenerator.clearCache();
		world.wipeMaps();
		savable.footPrints.clear();
		savable.dungeonLevel = -1;
		SystemData.requestGenUpdate = true;
	}

	// Start the managers
	public static void start() {
		System.out.println("Starting managers");
		characterManager.start();
		terrainManager.start();
		rendererManager.start();
		gameClock.start();
		animationClock.start();
		blockUpdateManager.start();
		buttonActionManager.start();
		backpackManager.start();
		entityUpdater.start();
		attackManager.start();
		entitySpawn.start();
		projectileManager.start();
		damagePostManager.start();
		lightingUpdater.start();
		particleManager.start();
		itemDropManager.start();
		weatherManager.start();
		chunkManager.start();
		SystemData.requestGenUpdate = true;
	}

}
