/* Defines all non-block/item textures. (Such as UI elements)
 * This must be called by the renderer thread when the game starts
 */
package com.mtautumn.edgequest;

import java.io.File;
import java.net.URISyntaxException;
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
	public String jarLocal;

	public static String getLocal() throws URISyntaxException {
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
	public TextureManager() {
	try {
		jarLocal = getLocal();
		System.out.println("Game Directory: " + jarLocal);
	} catch (URISyntaxException e) {
		e.printStackTrace();
	}
		textureAnimations.put("waterSplash", new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2});
		addGameTextures();
		addBlockTextures();
		addItemTextures();
		addEntityTextures();
		addProjectileTextures();
		addParticleTextures();
		addDropTextures();
	}
	public Texture getTexture(String texture) {
		return textureList.get(texture);
	}
	public Texture getAnimatedTexture(String texture) {
		return textureList.get(texture + textureAnimations.get(texture)[DataManager.system.animationClock % textureAnimations.get(texture).length]);
	}
	@SuppressWarnings("unused")
	private void addTexture(String name, int[] series) {
		for(int i = 0; i< series.length; i++) {
			addTexture(name + series[i]);
		}
	}
	private void addEntityTextures() {
		File folder = new File(jarLocal + "textures/entities");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isFile()) {
				try {
					File subFolder = new File(jarLocal + "textures/entities/" + listOfFiles[i].getName());
					File[] listOfSubFiles = subFolder.listFiles();
					for (int j = 0; j < listOfSubFiles.length; j++) {
						if (listOfSubFiles[j].isFile()) {
							try {
							if (getExtension(listOfSubFiles[j].getName()).equalsIgnoreCase("png")) {
									System.out.println("Loaded: textures/entities/"  + listOfFiles[i].getName() + "/" + listOfSubFiles[j].getName());
									textureList.put("entities." + listOfFiles[i].getName() + "." + getBaseName(listOfSubFiles[j].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/entities/" + listOfFiles[i].getName() + "/" + listOfSubFiles[j].getName())));
							}
							} catch (Exception e) {
								System.err.println("Could not load texture: textures/entities/"  + listOfFiles[i].getName() + "/" + listOfSubFiles[j].getName());
							}
						}
						
					}
				} catch (Exception e) {
					System.err.println("Could not load textures in: textures/entities/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addParticleTextures() {
		File folder = new File(jarLocal + "textures/particles");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/particles/" + listOfFiles[i].getName());
						textureList.put("particles." + getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/particles/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/particles/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addBlockTextures() {
		File folder = new File(jarLocal + "textures/blocks");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/blocks/" + listOfFiles[i].getName());
						textureList.put("blocks." + getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/blocks/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/blocks/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addItemTextures() {
		File folder = new File(jarLocal + "textures/items");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/items/" + listOfFiles[i].getName());
						textureList.put("items." + getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/items/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/items/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addGameTextures() {
		File folder = new File(jarLocal + "textures");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/" + listOfFiles[i].getName());
						textureList.put(getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addProjectileTextures() {
		File folder = new File(jarLocal + "textures/projectiles");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/projectiles/" + listOfFiles[i].getName());
						textureList.put("projectiles." + getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/projectiles/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/projectiles/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addDropTextures() {
		File folder = new File(jarLocal + "textures/drops");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					if (getExtension(listOfFiles[i].getName()).equalsIgnoreCase("png")) {
						System.out.println("Loaded: textures/drops/" + listOfFiles[i].getName());
						textureList.put("drops." + getBaseName(listOfFiles[i].getName()), TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/drops/" + listOfFiles[i].getName())));
					}
				} catch (Exception e) {
					System.err.println("Could not load texture: textures/drops/" + listOfFiles[i].getName());
				}
			}
		}
	}
	private void addTexture(String name) {
		try {
			textureList.put(name, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(jarLocal + "textures/" + name + ".png")));
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
