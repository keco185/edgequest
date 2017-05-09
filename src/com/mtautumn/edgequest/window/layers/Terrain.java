package com.mtautumn.edgequest.window.layers;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;
import com.mtautumn.edgequest.window.renderUtils.TerrainVBO;
public class Terrain extends Thread {
	Renderer r;
	public Terrain(Renderer r) {
		this.r = r;
	}
	@Override
	public void run() {
		draw(r);
		r.terrainVBO.preWrite();
	}
	public void completionTasks(Renderer r) {
		float blockSize = DataManager.settings.blockSize;
		int minTileX = DataManager.system.minTileX;
		int minTileY = DataManager.system.minTileY;
		int maxTileX = DataManager.system.maxTileX;
		int maxTileY = DataManager.system.maxTileY;
		double charX = DataManager.system.screenX;
		double charY = DataManager.system.screenY;
		
		float xPos = (float)((minTileX - charX) * blockSize + DataManager.settings.screenWidth/2.0);
		float yPos = (float)((minTileY - charY) * blockSize + DataManager.settings.screenHeight/2.0);
		float yPosReset = yPos;
		r.terrainVBO.write(r.textureManager.getTexture("blocks." + "blockAtlas"));
		// Structure block outline
		xPos = (float)((minTileX - charX) * blockSize + DataManager.settings.screenWidth/2.0) - blockSize / 6;
		yPosReset = (float)((minTileY - charY) * blockSize + DataManager.settings.screenHeight/2.0) - blockSize / 6;
		float block13 = blockSize * 1.33333333f;
		float block108 = blockSize * 1.0833333333f;
		float blockd24 = blockSize / 24f;
		float halfHeight = DataManager.settings.screenHeight / 2f;
		float halfWidth = DataManager.settings.screenWidth / 2f;
		
		for(int x = minTileX; x <= maxTileX; x++) {
			yPos = yPosReset;
			for (int y = minTileY; y <= maxTileY; y++) {
					if (DataManager.world.isStructBlock(x, y, DataManager.savable.dungeonLevel)) {
						if (DataManager.system.blockIDMap.get(DataManager.world.getStructBlockFast(x, y, DataManager.savable.dungeonLevel)).isSolid) {
							r.drawTexture(getStructureBlockTexture(r, x, y),xPos, yPos, block13, block13);
							r.fillRect(xPos, yPos, block13, block13, 0.1f, 0.1f, 0.1f, 0.5f);
						}
					}
				yPos += blockSize;
			}
			xPos += blockSize;
		}
		xPos = (float)((minTileX - charX) * blockSize + halfWidth);
		yPosReset = (float)((minTileY - charY) * blockSize + halfHeight);
		float offsetConstant = blockSize / 6f / halfWidth;
		for(int x = minTileX; x <= maxTileX; x++) {
			float offsetX = offsetConstant * (xPos - halfWidth) - blockd24;
			yPos = yPosReset;
			for (int y = minTileY; y <= maxTileY; y++) {
					if (DataManager.world.isStructBlock(x, y, DataManager.savable.dungeonLevel)) {
						float offsetY = offsetConstant * (yPos - halfHeight) - blockd24;
						r.drawTexture(getStructureBlockTexture(r, x, y),xPos + offsetX, yPos + offsetY, block108, block108);
					}
				yPos += blockSize;
			}
			xPos += blockSize;
		}
	}
	public static void draw(Renderer r) {
		float blockSize = DataManager.settings.blockSize;
		int minTileX = DataManager.system.minTileX;
		int minTileY = DataManager.system.minTileY;
		int maxTileX = DataManager.system.maxTileX;
		int maxTileY = DataManager.system.maxTileY;
		double charX = DataManager.system.screenX;
		double charY = DataManager.system.screenY;
		r.terrainVBO = new TerrainVBO();
		float xPos = (float)((minTileX - charX) * blockSize + DataManager.settings.screenWidth/2.0);
		float yPos = (float)((minTileY - charY) * blockSize + DataManager.settings.screenHeight/2.0);
		float yPosReset = yPos;
		for(int x = minTileX; x < maxTileX; x++) {
			yPos = yPosReset;
			for (int y = minTileY; y < maxTileY; y++) {
				if (!DataManager.system.blockIDMap.get(DataManager.world.getStructBlock(x, y, DataManager.savable.dungeonLevel)).isSolid) {
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
		return DataManager.system.blockIDMap.get(blockValue).getAtlasedBlockImg(DataManager.system.animationClock);
	}
	private static Texture getStructureBlockTexture(Renderer r, int x, int y) {
		short blockValue = getStructureBlockValue(r, x, y);
		return r.textureManager.getTexture(DataManager.system.blockIDMap.get(blockValue).getBlockImg(DataManager.system.animationClock));
	}
	private static short getStructureBlockValue(Renderer r, int x, int y) {
		return DataManager.world.getStructBlockFast(x, y, DataManager.savable.dungeonLevel);
	}
	private static short getTerrainBlockValue(Renderer r, int x, int y) {
		return DataManager.world.getGroundBlock(x, y, DataManager.savable.dungeonLevel);
	}
}