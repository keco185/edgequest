package com.mtautumn.edgequest.data;

import com.mtautumn.edgequest.console.ConsoleManager;
import com.mtautumn.edgequest.dataObjects.ItemSlot;
import com.mtautumn.edgequest.threads.AnimationClock;
import com.mtautumn.edgequest.threads.AttackManager;
import com.mtautumn.edgequest.threads.BackpackManager;
import com.mtautumn.edgequest.threads.BlockUpdateManager;
import com.mtautumn.edgequest.threads.ButtonActionManager;
import com.mtautumn.edgequest.threads.CharacterManager;
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
	public SystemData system = new SystemData();
	public SavableData savable = new SavableData();
	public SettingsData settings = new SettingsData();

	public MenuButtonManager menuButtonManager;
	public BackpackManager backpackManager = new BackpackManager(this);
	public BlockUpdateManager blockUpdateManager = new BlockUpdateManager(this);
	public CharacterManager characterManager = new CharacterManager(this);
	public RendererManager rendererManager = new RendererManager(this);
	public TerrainManager terrainManager = new TerrainManager(this);
	public GameClock gameClock = new GameClock(this);
	public AnimationClock animationClock = new AnimationClock(this);
	public ButtonActionManager buttonActionManager = new ButtonActionManager(this);
	public ConsoleManager consoleManager = new ConsoleManager(this);
	public AttackManager attackManager = new AttackManager(this);
	public EntitySpawn entitySpawn = new EntitySpawn(this);
	public ProjectileManager projectileManager = new ProjectileManager(this);
	public DamagePostManager damagePostManager = new DamagePostManager(this);
	public LightingUpdater lightingUpdater = new LightingUpdater(this);
	public ParticleManager particleManager = new ParticleManager(this);
	public ItemDropManager itemDropManager = new ItemDropManager(this);
	public WeatherManager weatherManager = new WeatherManager(this);

	public WorldUtils world = new WorldUtils(this);
	public EntityUpdater entityUpdater = new EntityUpdater(this);

	// Initialize a new game
	public void newGame() {
		resetTerrain();
		savable.time = 800;
		for (int i = 0; i< savable.backpackItems.length; i++) {
			for (int j = 0; j< savable.backpackItems[i].length; j++) {
				savable.backpackItems[i][j] = new ItemSlot();
			}
		}
		savable.entities.clear();
		characterManager.createCharacterEntity();
		system.blockGenerationLastTick = true;
		system.isGameOnLaunchScreen = false;
		system.isLaunchScreenLoaded = false;
		system.requestGenUpdate = true;
	}

	// Reset the terrain
	public void resetTerrain() {
		terrainManager.terrainGenerator.clearCache();
		world.wipeMaps();
		savable.footPrints.clear();
		savable.dungeonLevel = -1;
		system.requestGenUpdate = true;
	}

	// Start the managers
	public void start() {
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
		system.requestGenUpdate = true;
	}

}
