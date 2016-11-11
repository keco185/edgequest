/* Defines all non-block/item textures. (Such as UI elements)
 * This must be called by the renderer thread when the game starts
 */
package com.mtautumn.edgequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.mtautumn.edgequest.data.DataManager;

public class TextureManager {
	public Map<String, Texture> textureList = new HashMap<String, Texture>();
	public Map<String, Texture> entityTextureList = new HashMap<String, Texture>();
	public Map<String, int[]> textureAnimations = new HashMap<String, int[]>();

	public TextureManager() {
		textureAnimations.put("waterSplash", new int[]{0,0,1,1,2,2});
		addGameTextures();
		addBlockTextures();
		addItemTextures();
		addEntityTextures();
	}
	public Texture getTexture(String texture) {
		return textureList.get(texture);
	}
	public Texture getAnimatedTexture(String texture, DataManager dataManager) {
		return textureList.get(texture + textureAnimations.get(texture)[dataManager.system.animationClock % textureAnimations.get(texture).length]);
	}
	private void addTexture(String name, int[] series) {
		for(int i = 0; i< series.length; i++) {
			addTexture(name + series[i]);
		}
	}
	private void addEntityTextures() {
		File folder = new File("textures/entities");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/entities/" + listOfFiles[i].getName());
						textureList.put(getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/entities/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: /entities/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addBlockTextures() {
		File folder = new File("textures/blocks");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/blocks/" + listOfFiles[i].getName());
						textureList.put(getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/blocks/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: /blocks/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addItemTextures() {
		File folder = new File("textures/items");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/items/" + listOfFiles[i].getName());
						textureList.put(getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/items/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: /items/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addGameTextures() {
		File folder = new File("textures");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/" + listOfFiles[i].getName());
						textureList.put(getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: /" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addTexture(String name) {
		try {
			textureList.put(name, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("textures/" + name + ".png")));
		} catch (Exception e) {
			System.out.println("Could not load texture: textures/" + name);
		}
	}
	private static String getBaseName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1) {
			return fileName;
		}
		return fileName.substring(0, index);
	}
	private static String getExtension(String fileName) {
		int index = fileName.lastIndexOf('.') + 1;
		if (index == -1) {
			return fileName;
		}
		return fileName.substring(index, fileName.length());
	}
}
