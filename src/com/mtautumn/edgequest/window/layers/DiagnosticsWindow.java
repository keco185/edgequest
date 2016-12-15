package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.window.Renderer;

public class DiagnosticsWindow {
	public static void draw(Renderer r) {
		r.fillRect((int)(10* r.dataManager.system.uiZoom),(int)(10* r.dataManager.system.uiZoom), (int)(215* r.dataManager.system.uiZoom), (int)(240* r.dataManager.system.uiZoom), 0.7f, 0.7f, 0.7f, 0.7f);

		int i = 0;
		int x = (int)(20* r.dataManager.system.uiZoom);
		r.font.drawString(x, i+=x, "FPS: " + r.dataManager.system.averagedFPS);
		r.font.drawString(x, i+=x, "Time: " + r.dataManager.savable.time);
		r.font.drawString(x, i+=x, "Time Human: " + r.dataManager.system.timeReadable);
		r.font.drawString(x, i+=x, "Brightness: " + r.dataManager.world.getBrightness());
		r.font.drawString(x, i+=x, "CharX: " + r.dataManager.characterManager.characterEntity.getX());
		r.font.drawString(x, i+=x, "CharY: " + r.dataManager.characterManager.characterEntity.getY());
		r.font.drawString(x, i+=x, "CharDir: " + r.dataManager.characterManager.characterEntity.getRot());
		r.font.drawString(x, i+=x, "Dungeon Lvl: " + r.dataManager.savable.dungeonLevel);
		r.font.drawString(x, i+=x, "CharMove: " + r.dataManager.system.characterMoving);
		r.font.drawString(x, i+=x, "Terrain Gen: " + r.dataManager.system.blockGenerationLastTick);
		r.font.drawString(x, i+=x, "Block Size: " + r.dataManager.settings.blockSize);
	}
}
