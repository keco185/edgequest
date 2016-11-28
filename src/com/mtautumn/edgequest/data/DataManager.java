package com.mtautumn.edgequest.data;

import com.mtautumn.edgequest.AttackManager;
import com.mtautumn.edgequest.BackpackManager;
import com.mtautumn.edgequest.ButtonActionManager;
import com.mtautumn.edgequest.CharacterManager;
import com.mtautumn.edgequest.ConsoleManager;
import com.mtautumn.edgequest.DamagePostManager;
import com.mtautumn.edgequest.EntitySpawn;
import com.mtautumn.edgequest.EntityUpdater;
import com.mtautumn.edgequest.GameClock;
import com.mtautumn.edgequest.ItemSlot;
import com.mtautumn.edgequest.ProjectileManager;
import com.mtautumn.edgequest.TerrainManager;
import com.mtautumn.edgequest.WorldUtils;
import com.mtautumn.edgequest.updates.AnimationClock;
import com.mtautumn.edgequest.updates.BlockUpdateManager;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;
import com.mtautumn.edgequest.window.managers.RendererManager;

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
		if (savable.isInDungeon) {
			savable.isInDungeon = false;
			savable.dungeonLevel = -1;
			savable.dungeonCount = 0;
			characterManager.characterEntity.setX(savable.dungeonX);
			characterManager.characterEntity.setY(savable.dungeonY);
		}
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
		system.requestGenUpdate = true;
	}

}
