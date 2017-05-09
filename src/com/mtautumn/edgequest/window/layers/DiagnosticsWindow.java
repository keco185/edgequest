package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class DiagnosticsWindow {
	public static void draw(Renderer r) {
		r.fillRect((int)(10* DataManager.system.uiZoom),(int)(10* DataManager.system.uiZoom), (int)(215* DataManager.system.uiZoom), (int)(240* DataManager.system.uiZoom), 0.7f, 0.7f, 0.7f, 0.7f);

		int i = 0;
		int x = (int)(20* DataManager.system.uiZoom);
		r.font.drawString(x, i+=x, "FPS: " + DataManager.system.averagedFPS);
		r.font.drawString(x, i+=x, "Time: " + DataManager.savable.time);
		r.font.drawString(x, i+=x, "Time Human: " + DataManager.system.timeReadable);
		r.font.drawString(x, i+=x, "Brightness: " + DataManager.world.getBrightness());
		r.font.drawString(x, i+=x, "CharX: " + DataManager.characterManager.characterEntity.getX());
		r.font.drawString(x, i+=x, "CharY: " + DataManager.characterManager.characterEntity.getY());
		r.font.drawString(x, i+=x, "CharDir: " + DataManager.characterManager.characterEntity.getRot());
		r.font.drawString(x, i+=x, "Dungeon Lvl: " + DataManager.savable.dungeonLevel);
		r.font.drawString(x, i+=x, "CharMove: " + DataManager.system.characterMoving);
		r.font.drawString(x, i+=x, "Terrain Gen: " + DataManager.system.blockGenerationLastTick);
		r.font.drawString(x, i+=x, "Block Size: " + DataManager.settings.blockSize);
	}
}
