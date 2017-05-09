package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.console.ConsoleManager.Line;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class Console {

	private static final int consoleWidth = 500;
	private static final int consoleHeight = 200;
	private static final int lineCount = 8;

	public static void draw(Renderer r) {
		int screenWidth = SettingsData.screenWidth;
		r.fillRect(screenWidth - (int)((10 + consoleWidth) * SystemData.uiZoom),(int)(10 * SystemData.uiZoom), (int)(consoleWidth * SystemData.uiZoom), (int)(consoleHeight * SystemData.uiZoom), 0.7f, 0.7f, 0.7f, 0.9f);
		Line[] lines = DataManager.consoleManager.getNewestLines(lineCount);
		for (int i = 0; i < lineCount; i++) {
			Line line = lines[i];
			if (line != null) {
				if (isDrawable(line.getText())) {
					r.font.drawString(screenWidth - (int) (consoleWidth * SystemData.uiZoom),(int)((consoleHeight -  (i + 1) * consoleHeight / (lineCount + 1) - 10) * SystemData.uiZoom), line.getText(), line.color);
				}
			}
		}
		if (isDrawable(SystemData.consoleText)) {
			r.font.drawString(screenWidth - (int)(consoleWidth * SystemData.uiZoom),(int)((consoleHeight - 10) * SystemData.uiZoom), SystemData.consoleText);
		}
	}
	private static boolean isDrawable(String line) {
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) != " ".charAt(0)) {
				return true;
			}
		}
		return false;
	}
}
