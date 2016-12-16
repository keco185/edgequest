package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.ConsoleManager.Line;
import com.mtautumn.edgequest.window.Renderer;

public class Console {
	private static final int consoleWidth = 500;
	private static final int consoleHeight = 200;
	private static final int lineCount = 8;
	public static void draw(Renderer r) {
		int screenWidth = r.dataManager.settings.screenWidth;
		r.fillRect(screenWidth - (int)((10 + consoleWidth) * r.dataManager.system.uiZoom),(int)(10 * r.dataManager.system.uiZoom), (int)(consoleWidth * r.dataManager.system.uiZoom), (int)(consoleHeight * r.dataManager.system.uiZoom), 0.7f, 0.7f, 0.7f, 0.9f);
		Line[] lines = r.dataManager.consoleManager.getNewestLines(lineCount);
		for (int i = 0; i < lineCount; i++) {
			Line line = lines[i];
			if (line != null) {
				if (isDrawable(line.getText())) {
					r.font.drawString(screenWidth - (int) (consoleWidth * r.dataManager.system.uiZoom),(int)((consoleHeight -  (i + 1) * consoleHeight / (lineCount + 1) - 10) * r.dataManager.system.uiZoom), line.getText(), line.color);
				}
			}
		}
		if (isDrawable(r.dataManager.system.consoleText)) {
			r.font.drawString(screenWidth - (int)(consoleWidth * r.dataManager.system.uiZoom),(int)((consoleHeight - 10) * r.dataManager.system.uiZoom), r.dataManager.system.consoleText);
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
