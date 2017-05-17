package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.Chunk;
import com.mtautumn.edgequest.utils.WorldUtils;
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
		float blockSize = SettingsData.blockSize;
		double charX = SystemData.screenX;
		double charY = SystemData.screenY;

		float xPos;
		float yPos;
		float yPosReset;
		r.terrainVBO.write(r.textureManager.getTexture("blocks." + "blockAtlas"));
		// Structure block outline
		float block13 = blockSize * 1.33333333f;
		float block108 = blockSize * 1.0833333333f;
		float blockd24 = blockSize / 24f;
		float halfHeight = SettingsData.screenHeight / 2f;
		float halfWidth = SettingsData.screenWidth / 2f;
		if (DataManager.savable.dungeonLevel > -1) {
			for (Chunk chunk : DataManager.savable.loadedChunks.values()) {
				if (chunk.isOnScreen()) {
					xPos = (float)(( chunk.x - charX) * blockSize + SettingsData.screenWidth/2.0) - blockSize / 6;
					yPosReset = (float)((chunk.y - charY) * blockSize + SettingsData.screenHeight/2.0) - blockSize / 6;
					for (int x = 0; x < 10; x++) {
						yPos = yPosReset;
						for (int y = 0; y < 10; y++) {
							if (SystemData.blockIDMap.get(chunk.wall[x][y]).isSolid) {
								r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(chunk.wall[x][y]).getBlockImg(SystemData.animationClock)),xPos, yPos, block13, block13);
							}
							yPos += blockSize;
						}
						xPos += blockSize;
					}
				}
			}
		} else {
			for (Chunk chunk : DataManager.savable.loadedChunks.values()) {
				if (chunk.isOnScreen()) {
					xPos = (float)(( chunk.x - charX) * blockSize + SettingsData.screenWidth/2.0) - blockSize / 6;
					yPosReset = (float)((chunk.y - charY) * blockSize + SettingsData.screenHeight/2.0) - blockSize / 6;
					for (int x = 0; x < 10; x++) {
						yPos = yPosReset;
						for (int y = 0; y < 10; y++) {
							if (SystemData.blockIDMap.get(chunk.wall[x][y]).isSolid) {
								r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(chunk.wall[x][y]).getBlockImg(SystemData.animationClock)),xPos, yPos, block13, block13);
								r.fillRect(xPos, yPos, block13, block13, 0.1f, 0.1f, 0.1f, 0.5f);
							}
							yPos += blockSize;
						}
						xPos += blockSize;
					}
				}
			}
		}

		float offsetConstant = blockSize / 6f / halfWidth;
		boolean renderSolid = WorldUtils.getBrightness() > 0;
		for (Chunk chunk : DataManager.savable.loadedChunks.values()) {
			if (chunk.isOnScreen()) {
				xPos = (float)((chunk.x - charX) * blockSize + halfWidth);
				yPosReset = (float)((chunk.y - charY) * blockSize + halfHeight);
				for (int x = 0; x < 10; x++) {
					float offsetX = offsetConstant * (xPos - halfWidth) - blockd24;
					yPos = yPosReset;
					for (int y = 0; y < 10; y++) {
						if (chunk.wall[x][y] > 0) {
							if (renderSolid) {
								float offsetY = offsetConstant * (yPos - halfHeight) - blockd24;
								r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(chunk.wall[x][y]).getBlockImg(SystemData.animationClock)),xPos + offsetX, yPos + offsetY, block108, block108);
							} else if (!SystemData.blockIDMap.get(chunk.wall[x][y]).isSolid) {
								float offsetY = offsetConstant * (yPos - halfHeight) - blockd24;
								r.drawTexture(r.textureManager.getTexture(SystemData.blockIDMap.get(chunk.wall[x][y]).getBlockImg(SystemData.animationClock)),xPos + offsetX, yPos + offsetY, block108, block108);
							}
						}
						yPos += blockSize;
					}
					xPos += blockSize;
				}
			}
		}
		if (SettingsData.showDiag) {
			final float lineWidth = 1;
			for (Chunk chunk : DataManager.savable.loadedChunks.values()) {
				if (chunk.isOnScreen()) {
					xPos = (float)((chunk.x - charX) * blockSize + halfWidth);
					yPos = (float)((chunk.y - charY) * blockSize + halfHeight);
					float width = (float) (10.0 * blockSize);
					r.fillRect(xPos - lineWidth / 2, yPos - lineWidth / 2, lineWidth, width, 1.0f,0.0f,0.0f,1.0f);
					r.fillRect(xPos - lineWidth / 2, yPos - lineWidth / 2, width, lineWidth, 1.0f,0.0f,0.0f,1.0f);
					r.fillRect(xPos - lineWidth/2 + width, yPos - lineWidth/2 + width, -lineWidth, -width, 1.0f,0.0f,0.0f,1.0f);
					r.fillRect(xPos - lineWidth/2 + width , yPos - lineWidth/2 + width, -width, -lineWidth, 1.0f,0.0f,0.0f,1.0f);
				}
			}
		}
	}
	public static void draw(Renderer r) {
		float blockSize = SettingsData.blockSize;
		double charX = SystemData.screenX;
		double charY = SystemData.screenY;
		r.terrainVBO = new TerrainVBO();

		boolean renderSolid = WorldUtils.getBrightness() > 0;
		for (Chunk chunk : DataManager.savable.loadedChunks.values()) {
			if (chunk.isOnScreen()) {
				float xPos = (float)((chunk.x - charX) * blockSize + SettingsData.screenWidth/2.0);
				float yPos = (float)((chunk.y - charY) * blockSize + SettingsData.screenHeight/2.0);
				float yPosReset = yPos;
				for (int x = 0; x < 10; x++) {
					yPos = yPosReset;
					for (int y = 0; y < 10; y++) {
						if (renderSolid) {
							r.terrainVBO.addQuad(xPos, yPos, blockSize, blockSize);
							r.terrainVBO.addTextureQuad(SystemData.blockIDMap.get(chunk.ground[x][y]).getAtlasedBlockImg(SystemData.animationClock));
						} else if (!SystemData.blockIDMap.get(chunk.wall[x][y]).isSolid) {
							r.terrainVBO.addQuad(xPos, yPos, blockSize, blockSize);
							r.terrainVBO.addTextureQuad(SystemData.blockIDMap.get(chunk.ground[x][y]).getAtlasedBlockImg(SystemData.animationClock));
						}
						yPos += blockSize;
					}
					xPos += blockSize;
				}
			}
		}
	}
}