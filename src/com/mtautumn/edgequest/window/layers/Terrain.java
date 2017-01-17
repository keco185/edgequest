package com.mtautumn.edgequest.window.layers;

import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.VBO;

public class Terrain extends Thread {
	Renderer r;
	public Terrain(Renderer r) {
		this.r = r;
	}
	public void run() {
		draw(r);
		r.terrainVBO.preWrite();
	}
	private static boolean isLight(DataManager dm, int x, int y, int level) {
		return dm.world.isLight(x, y, level) || dm.world.isLight(x, y + 1, level) || dm.world.isLight(x + 1, y, level) || dm.world.isLight(x + 1, y + 1, level);   
	}
	public static void completionTasks(Renderer r) {
		float blockSize = r.dataManager.settings.blockSize;
		int minTileX = r.dataManager.system.minTileX;
		int minTileY = r.dataManager.system.minTileY;
		int maxTileX = r.dataManager.system.maxTileX;
		int maxTileY = r.dataManager.system.maxTileY;
		double charX = r.dataManager.system.screenX;
		double charY = r.dataManager.system.screenY;
		boolean bright = r.dataManager.world.getBrightness() > 0;

		float xPos = (float)((minTileX - charX) * blockSize + r.dataManager.settings.screenWidth/2.0);
		float yPos = (float)((minTileY - charY) * blockSize + r.dataManager.settings.screenHeight/2.0);
		float yPosReset = yPos;


		r.terrainVBO.write(r.textureManager.getTexture("blocks." + "blockAtlas"));
		// Structure block outline
		xPos = (float)((minTileX - charX) * blockSize + r.dataManager.settings.screenWidth/2.0) - blockSize / 6;
		yPosReset = (float)((minTileY - charY) * blockSize + r.dataManager.settings.screenHeight/2.0) - blockSize / 6;
		float block13 = blockSize * 1.33333333f;
		for(int x = minTileX; x <= maxTileX; x++) {
			yPos = yPosReset;
			for (int y = minTileY; y <= maxTileY; y++) {
				if (bright) {
					if (r.dataManager.world.isStructBlock(x, y, r.dataManager.savable.dungeonLevel)) {
						if (r.dataManager.system.blockIDMap.get(r.dataManager.world.getStructBlock(x, y, r.dataManager.savable.dungeonLevel)).isSolid) {
							r.drawTexture(getStructureBlockTexture(r, x, y),xPos, yPos, block13, block13);
							r.fillRect(xPos, yPos, block13, block13, 0.1f, 0.1f, 0.1f, 0.5f);
						}
					}
				} else if (isLight(r.dataManager, x, y, r.dataManager.savable.dungeonLevel)) {
					if (r.dataManager.world.isStructBlock(x, y, r.dataManager.savable.dungeonLevel)) {
						if (r.dataManager.system.blockIDMap.get(r.dataManager.world.getStructBlock(x, y, r.dataManager.savable.dungeonLevel)).isSolid) {
							r.drawTexture(getStructureBlockTexture(r, x, y),xPos, yPos, block13, block13);
							r.fillRect(xPos, yPos, block13, block13, 0.1f, 0.1f, 0.1f, 0.5f);
						}
					}
				}
				yPos += blockSize;
			}
			xPos += blockSize;
		}
		xPos = (float)((minTileX - charX) * blockSize + r.dataManager.settings.screenWidth/2.0);
		yPosReset = (float)((minTileY - charY) * blockSize + r.dataManager.settings.screenHeight/2.0);
		float offsetConstant = blockSize / 6f / (r.dataManager.settings.screenWidth / 2f);
		for(int x = minTileX; x <= maxTileX; x++) {
			yPos = yPosReset;
			for (int y = minTileY; y <= maxTileY; y++) {
				if (bright) {
					if (r.dataManager.world.isStructBlock(x, y, r.dataManager.savable.dungeonLevel)) {
						float offsetX = offsetConstant * (xPos - r.dataManager.settings.screenWidth/2.0f) - blockSize / 24f;
						float offsetY = offsetConstant * (yPos - r.dataManager.settings.screenHeight/2.0f) - blockSize / 24f;
						r.drawTexture(getStructureBlockTexture(r, x, y),xPos + offsetX, yPos + offsetY, blockSize * 1.0833333333f, blockSize * 1.0833333333f);
					}
				} else if (isLight(r.dataManager, x, y, r.dataManager.savable.dungeonLevel)) {
					if (r.dataManager.world.isStructBlock(x, y, r.dataManager.savable.dungeonLevel)) {
						float offsetX = offsetConstant * (xPos - r.dataManager.settings.screenWidth/2.0f) - blockSize / 24f;
						float offsetY = offsetConstant * (yPos - r.dataManager.settings.screenHeight/2.0f) - blockSize / 24f;
						r.drawTexture(getStructureBlockTexture(r, x, y),xPos + offsetX, yPos + offsetY, blockSize * 1.0833333333f, blockSize * 1.0833333333f);
					}
				}
				yPos += blockSize;
			}
			xPos += blockSize;
		}
	}
	public static void draw(Renderer r) {
		float blockSize = r.dataManager.settings.blockSize;
		int minTileX = r.dataManager.system.minTileX;
		int minTileY = r.dataManager.system.minTileY;
		int maxTileX = r.dataManager.system.maxTileX;
		int maxTileY = r.dataManager.system.maxTileY;
		double charX = r.dataManager.system.screenX;
		double charY = r.dataManager.system.screenY;
		boolean bright = r.dataManager.world.getBrightness() > 0;
		r.terrainVBO = new VBO(r.dataManager);


		float xPos = (float)((minTileX - charX) * blockSize + r.dataManager.settings.screenWidth/2.0);
		float yPos = (float)((minTileY - charY) * blockSize + r.dataManager.settings.screenHeight/2.0);
		float yPosReset = yPos;
		for(int x = minTileX; x < maxTileX; x++) {
			yPos = yPosReset;
			for (int y = minTileY; y < maxTileY; y++) {
				if (bright) {
					r.terrainVBO.addQuad(xPos, yPos, blockSize, blockSize);
					r.terrainVBO.addTextureQuad(getTerrainBlockTexture(r,x,y));
				} else if (isLight(r.dataManager, x, y, r.dataManager.savable.dungeonLevel)) {
					r.terrainVBO.addQuad(xPos, yPos, blockSize, blockSize);
					r.terrainVBO.addTextureQuad(getTerrainBlockTexture(r,x,y));
				}
				yPos += blockSize;
			}
			xPos += blockSize;
		}

	}

	private static int[] getTerrainBlockTexture(Renderer r, int x, int y) {
		short blockValue = getTerrainBlockValue(r, x, y);
		return r.dataManager.system.blockIDMap.get(blockValue).getAtlasedBlockImg(r.dataManager.system.animationClock);
	}
	private static Texture getStructureBlockTexture(Renderer r, int x, int y) {
		short blockValue = getStructureBlockValue(r, x, y);
		return r.textureManager.getTexture(r.dataManager.system.blockIDMap.get(blockValue).getBlockImg(r.dataManager.system.animationClock));
	}
	private static short getStructureBlockValue(Renderer r, int x, int y) {
		return r.dataManager.world.getStructBlock(x, y, r.dataManager.savable.dungeonLevel);
	}
	private static short getTerrainBlockValue(Renderer r, int x, int y) {
		return r.dataManager.world.getGroundBlock(x, y, r.dataManager.savable.dungeonLevel);

	}
}
