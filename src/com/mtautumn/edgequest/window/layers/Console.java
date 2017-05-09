package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.console.ConsoleManager.Line;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class Console {

	private static final int consoleWidth = 500;
	private static final int consoleHeight = 200;
	private static final int lineCount = 8;

	public static void draw(Renderer r) {
		int screenWidth = DataManager.settings.screenWidth;
		r.fillRect(screenWidth - (int)((10 + consoleWidth) * DataManager.system.uiZoom),(int)(10 * DataManager.system.uiZoom), (int)(consoleWidth * DataManager.system.uiZoom), (int)(consoleHeight * DataManager.system.uiZoom), 0.7f, 0.7f, 0.7f, 0.9f);
		Line[] lines = DataManager.consoleManager.getNewestLines(lineCount);
		for (int i = 0; i < lineCount; i++) {
			Line line = lines[i];
			if (line != null) {
				if (isDrawable(line.getText())) {
					r.font.drawString(screenWidth - (int) (consoleWidth * DataManager.system.uiZoom),(int)((consoleHeight -  (i + 1) * consoleHeight / (lineCount + 1) - 10) * DataManager.system.uiZoom), line.getText(), line.color);
				}
			}
		}
		if (isDrawable(DataManager.system.consoleText)) {
			r.font.drawString(screenWidth - (int)(consoleWidth * DataManager.system.uiZoom),(int)((consoleHeight - 10) * DataManager.system.uiZoom), DataManager.system.consoleText);
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
