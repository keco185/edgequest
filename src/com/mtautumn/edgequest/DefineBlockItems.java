/* Basically creates all of the blocks and items for the game. It also creates
 * hash maps which can be used to look up a block/item given its id or name
 */
package com.mtautumn.edgequest;

import java.util.HashMap;
import java.util.Map;

import com.mtautumn.edgequest.blockitems.BlockItem;
import com.mtautumn.edgequest.blockitems.combat.DaggerWeapon;
import com.mtautumn.edgequest.blockitems.combat.PistolWeapon;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;

public class DefineBlockItems {
	public static Map<Short, BlockItem> blockIDMap = new HashMap<Short, BlockItem>();
	public static Map<String, BlockItem> blockNameMap = new HashMap<String, BlockItem>();
	public static void setDefinitions() {
		atlasDefinition();
		noneDefinition();
		noTextureDefinition();
		grassDefinition();
		dirtDefinition();
		stoneDefinition();
		sandDefinition();
		snowDefinition();
		waterDefinition();
		groundDefinition();
		iceDefinition();
		lanternDefinition();
		daggerDefinition();
		pistolDefinition();
		bulletDefinition();
		darkWoodDefinition();
		lightWoodDefinition();
		asphaltDefinition();

		torchDefinition();
		lilyPadDefinition();
		treeDefinition();

		dungeonDefinition();
		dungeonUpDefinition();
		
		snowyStoneDefinition();
		sandyStoneDefinition();
		
		SystemData.blockIDMap = blockIDMap;
		SystemData.blockNameMap = blockNameMap;
	}
	private static void atlasDefinition() {
		SettingsData.atlasMap.put("dimensions", new int[]{4,8});
		SettingsData.atlasMap.put("dirt0", new int[]{0,0});
		SettingsData.atlasMap.put("dungeon0", new int[]{1,0});
		SettingsData.atlasMap.put("sand0", new int[]{2,0});
		SettingsData.atlasMap.put("stone0", new int[]{3,0});
		SettingsData.atlasMap.put("snow0", new int[]{0,1});
		SettingsData.atlasMap.put("grass0", new int[]{1,1});
		SettingsData.atlasMap.put("dungeonUp0", new int[]{2,1});
		SettingsData.atlasMap.put("torch0", new int[]{3,1});
		SettingsData.atlasMap.put("tree0", new int[]{0,2});
		SettingsData.atlasMap.put("water0", new int[]{1,2});
		SettingsData.atlasMap.put("ground0", new int[]{2,2});
		SettingsData.atlasMap.put("water1", new int[]{3,2});
		SettingsData.atlasMap.put("water2", new int[]{0,3});
		SettingsData.atlasMap.put("water3", new int[]{1,3});
		SettingsData.atlasMap.put("noTexture0", new int[]{2,3});
		SettingsData.atlasMap.put("none0", new int[]{3,3});
		SettingsData.atlasMap.put("lilyPad0", new int[]{0,4});
		SettingsData.atlasMap.put("ice0", new int[]{1,4});
		SettingsData.atlasMap.put("darkWood0", new int[]{2,4});
		SettingsData.atlasMap.put("lightWood0", new int[]{3,4});
		SettingsData.atlasMap.put("asphalt0", new int[]{0,5});
		SettingsData.atlasMap.put("snowyStone0", new int[]{1,5});
		SettingsData.atlasMap.put("sandyStone0", new int[]{2,5});
	}
	private static void noneDefinition() {
		BlockItem none = new BlockItem(-1, true, true, "none", new int[]{0} , new int[]{0});
		addToMaps(none);
	}
	private static void noTextureDefinition() {
		BlockItem noTexture = new BlockItem(0, true, true, "noTexture", new int[]{0} , new int[]{0});
		noTexture.isSolid = false;
		addToMaps(noTexture);
	}
	private static void grassDefinition() {
		BlockItem grass = new BlockItem(1, true, false, "grass", new int[]{0} , new int[]{0});
		grass.replacedBy = "dirt";
		addToMaps(grass);
	}
	private static void dirtDefinition() {
		BlockItem dirt = new BlockItem(2, true, false, "dirt", new int[]{0} , new int[]{0});
		addToMaps(dirt);
	}
	private static void stoneDefinition() {
		BlockItem stone = new BlockItem(3, true, false, "stone", new int[]{0} , new int[]{0});
		addToMaps(stone);
	}
	private static void sandDefinition() {
		BlockItem sand = new BlockItem(4, true, false, "sand", new int[]{0} , new int[]{0});
		addToMaps(sand);
	}
	private static void snowDefinition() {
		BlockItem snow = new BlockItem(5, true, false, "snow", new int[]{0} , new int[]{0});
		snow.canHavePrints = true;
		snow.melts = true;
		snow.meltsInto = "grass";
		snow.replacedBy = "grass";
		addToMaps(snow);
	}
	private static void waterDefinition() {
		BlockItem water = new BlockItem(6, true, false, "water", new int[]{0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,2,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1} , null);
		water.isLiquid = true;
		water.isPassable = true;
		water.hardness = -1;
		addToMaps(water);
	}
	private static void groundDefinition() {
		BlockItem ground = new BlockItem(7, true, false, "ground", new int[]{0} , null);
		ground.hardness = -1;
		addToMaps(ground);
	}
	private static void iceDefinition() {
		BlockItem ice = new BlockItem(8, true, true, "ice", new int[]{0} , new int[]{0});
		ice.melts = true;
		ice.meltsInto = "water";
		ice.replacedBy = "water";
		addToMaps(ice);
	}
	private static void lanternDefinition() {
		BlockItem lantern = new BlockItem(9, false, true, "lantern", null , new int[]{0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1});
		lantern.maxFuel = 100;
		lantern.isStackable = false;
		addToMaps(lantern);
	}
	private static void daggerDefinition() {
		BlockItem dagger = new DaggerWeapon(10);
		dagger.isStackable = false;
		dagger.maxHealth = 50;
		addToMaps(dagger);
	}
	private static void pistolDefinition() {
		BlockItem pistol = new PistolWeapon(11);
		pistol.isStackable = false;
		pistol.maxHealth = 50;
		addToMaps(pistol);
	}
	private static void bulletDefinition() {
		BlockItem bullet = new BlockItem(12, false, true, "bullet", null , new int[]{0});
		addToMaps(bullet);
	}
	private static void darkWoodDefinition() {
		BlockItem darkWood = new BlockItem(13, true, false, "darkWood", new int[]{0} , new int[]{0});
		addToMaps(darkWood);
	}
	private static void lightWoodDefinition() {
		BlockItem lightWood = new BlockItem(14, true, false, "lightWood", new int[]{0} , new int[]{0});
		addToMaps(lightWood);
	}
	private static void asphaltDefinition() {
		BlockItem asphalt = new BlockItem(15, true, false, "asphalt", new int[]{0} , new int[]{0});
		addToMaps(asphalt);
	}
	private static void torchDefinition() {
		BlockItem torch = new BlockItem(100, true, true, "torch", new int[]{0} , new int[]{0});
		torch.isLightSource = true;
		torch.isHot = true;
		torch.isPassable = true;
		torch.isSolid = false;
		torch.addDropImgs(new int[]{0});
		addToMaps(torch);
	}
	private static void lilyPadDefinition() {
		BlockItem lilyPad = new BlockItem(101, true, true, "lilyPad", new int[]{0} , new int[]{0});
		lilyPad.isPassable = true;
		lilyPad.isSolid = false;
		addToMaps(lilyPad);
	}
	private static void treeDefinition() {
		BlockItem tree = new BlockItem(102, true, false, "tree", new int[]{0} , null);
		tree.isSolid = false;
		addToMaps(tree);
	}
	private static void dungeonDefinition() {
		BlockItem dungeon = new BlockItem(200, true, false, "dungeon", new int[]{0} , null);
		dungeon.hardness = -1;
		dungeon.isPassable = true;
		dungeon.isSolid = false;
		addToMaps(dungeon);
	}
	private static void dungeonUpDefinition() {
		BlockItem dungeonUp = new BlockItem(201, true, false, "dungeonUp", new int[]{0} , null);
		dungeonUp.isLightSource = true;
		dungeonUp.hardness = -1;
		dungeonUp.isPassable = true;
		dungeonUp.isSolid = false;
		addToMaps(dungeonUp);
	}
	private static void snowyStoneDefinition() {
		BlockItem snowyStone = new BlockItem(202, true, false, "snowyStone", new int[]{0} , new int[]{0});
		snowyStone.canHavePrints = true;
		snowyStone.melts = true;
		snowyStone.meltsInto = "stone";
		snowyStone.replacedBy = "stone";
		addToMaps(snowyStone);
	}
	private static void sandyStoneDefinition() {
		BlockItem sandyStone = new BlockItem(203, true, false, "sandyStone", new int[]{0} , new int[]{0});
		addToMaps(sandyStone);
	}

	private static void addToMaps(BlockItem blockItem) {
		blockIDMap.put(blockItem.getID(), blockItem);
		blockNameMap.put(blockItem.getName(), blockItem);
	}
}

