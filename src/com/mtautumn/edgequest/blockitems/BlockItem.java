/*This class is used as a datatype that defines what each block/item is.
 * These BlockItems are created in the DefineBlockItems class
 */
package com.mtautumn.edgequest.blockitems;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.EdgeQuest;
import com.mtautumn.edgequest.data.DataManager;

public class BlockItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String blockImageDirectory = "blocks";
	private static final String itemImageDirectory = "items";
	private ArrayList<Texture> blockImg = new ArrayList<Texture>();
	private ArrayList<Texture> itemImg = new ArrayList<Texture>();

	private ArrayList<int[]> blockImgAtlas = new ArrayList<int[]>();
	private boolean isItem;
	private boolean isBlock;
	private String name;
	private Short id;

	//---Battle---
	public boolean isLightSource = false;
	public boolean isHot = false;
	public boolean melts = false;
	public String meltsInto = "";
	public double hardness = 0.2; //seconds take to destroy with hands
	public String breaksInto;
	public String replacedBy = "ground";
	public boolean isLiquid = false;
	public boolean isPassable = false;
	public boolean isSolid = true;
	public boolean canHavePrints = false;
	public int blockHeight = 0;

	//Item Specific Attributes
	public boolean isStackable = true;
	public int damage = 1;
	public int maxFuel = 1;
	public int fuel = 1;
	public int maxHealth = 1;

	public BlockItem(int id, boolean isBlock, boolean isItem, String name, int[] blockAnimation, int[] itemAnimation, DataManager dm) {
		this.isItem = isItem;
		this.isBlock = isBlock;
		this.id = (short)id;
		breaksInto = name;
		this.name = name;
		if (isBlock) {
			for (Short i = 0; i < blockAnimation.length; i++) {
				blockImg.add(getTexture(name + blockAnimation[i], blockImageDirectory));
				blockImgAtlas.add(dm.settings.atlasMap.get(name + blockAnimation[i]));
			}
		}
		if (isItem) {
			for (Short i = 0; i < itemAnimation.length; i++) {
				itemImg.add(getTexture(name + itemAnimation[i], itemImageDirectory));
			}
		}
	}

	public Texture getItemImg(int time) {
		if (isItem) return itemImg.get(time % itemImg.size());
		if (isBlock) return blockImg.get(time % blockImg.size());
		return null;
	}

	public Texture getBlockImg(int time) {
		if (isBlock) return blockImg.get(time % blockImg.size());
		if (isItem) return itemImg.get(time % itemImg.size());
		return null;
	}
	public int[] getAtlasedBlockImg(int time) {
		return blockImgAtlas.get(time % blockImgAtlas.size());
	}
	public boolean getIsBlock() { return isBlock; }

	public boolean getIsItem() { return isItem; }

	public String getName() { return name; }

	public Short getID() { return id; }

	public boolean isName(String testName) { return testName.equals(name); }

	public boolean isID(Short testID) { return testID.equals(id); }

	private static Texture getTexture(String name, String directory) {
		try {
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(getLocal() + "/textures/" + directory + "/" + name + ".png"));
		} catch (Exception e) {
			System.err.println("Could not load block/item texture: " + "textures/" + directory + "/" + name + ".png");
			return null;
		}
	}
	private static String getLocal() throws URISyntaxException {
		String baseLocal = EdgeQuest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		if (baseLocal.substring(baseLocal.length() - 1).equals("/") || baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
			baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
		}
		boolean removing = true;
		while (removing) {
			if (!baseLocal.substring(baseLocal.length() - 1).equals("/") && !baseLocal.substring(baseLocal.length() - 1).equals("\\")) {
				baseLocal = baseLocal.substring(0, baseLocal.length() - 1);
			} else {
				removing = false;
			}
		}
		return baseLocal;
	}
}
