package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.threads.CharacterManager;
import com.mtautumn.edgequest.utils.WorldUtils;
import com.mtautumn.edgequest.window.Renderer;

public class DiagnosticsWindow {
	public static void draw(Renderer r) {
		r.fillRect((int)(10* SystemData.uiZoom),(int)(10* SystemData.uiZoom), (int)(215* SystemData.uiZoom), (int)(260* SystemData.uiZoom), 0.7f, 0.7f, 0.7f, 0.7f);

		int i = 0;
		int x = (int)(20* SystemData.uiZoom);
		r.font.drawString(x, i+=x, "FPS: " + SystemData.averagedFPS);
		r.font.drawString(x, i+=x, "Time: " + DataManager.savable.time);
		r.font.drawString(x, i+=x, "Time Human: " + SystemData.timeReadable);
		r.font.drawString(x, i+=x, "Brightness: " + WorldUtils.getBrightness());
		r.font.drawString(x, i+=x, "CharX: " + CharacterManager.characterEntity.getX());
		r.font.drawString(x, i+=x, "CharY: " + CharacterManager.characterEntity.getY());
		r.font.drawString(x, i+=x, "CharDir: " + CharacterManager.characterEntity.getRot());
		r.font.drawString(x, i+=x, "Dungeon Lvl: " + DataManager.savable.dungeonLevel);
		r.font.drawString(x, i+=x, "CharMove: " + SystemData.characterMoving);
		r.font.drawString(x, i+=x, "Terrain Gen: " + SystemData.blockGenerationLastTick);
		r.font.drawString(x, i+=x, "Block Size: " + SettingsData.blockSize);
		r.font.drawString(x, i+=x, "Loaded Chunks: " + DataManager.savable.loadedChunks.size());
	}
}
