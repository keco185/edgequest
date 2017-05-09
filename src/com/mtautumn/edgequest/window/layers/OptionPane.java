package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.Renderer;

public class OptionPane {
	
	public static void draw(Renderer r) {
		for (int i = 0; i < DataManager.system.inputText.size(); i++) {
			drawInput(r, DataManager.system.inputText.get(i), i);
		}
		for (int i = 0; i < DataManager.system.noticeText.size(); i++) {
			drawNotice(r, DataManager.system.noticeText.get(i), i);
		}
	}
	private static void drawInput(Renderer r, String text, int count) {
		if (DataManager.settings.screenWidth > 1.6 * DataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(DataManager.settings.screenHeight - DataManager.settings.screenWidth / 1.6) / 2, DataManager.settings.screenWidth,(int)(DataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(DataManager.settings.screenWidth - DataManager.settings.screenHeight * 1.6)/2, 0, (int)(DataManager.settings.screenHeight * 1.6),DataManager.settings.screenHeight);

		}
		int textX = (DataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (DataManager.settings.screenHeight) / 2 - r.font2.getHeight(text) - (int)(100 * DataManager.system.uiZoom);
		r.font2.drawString(textX, textY, text);
		int xPos = (DataManager.settings.screenWidth - (int)(400 * DataManager.system.uiZoom)) / 2;
		int yPos = (DataManager.settings.screenHeight - (int)(60 * DataManager.system.uiZoom)) / 2;
		r.drawTexture(r.textureManager.getTexture("inputField"), xPos, yPos, (int)(400 * DataManager.system.uiZoom), (int)(60 * DataManager.system.uiZoom));
		String inputFieldText = "";
		if (DataManager.system.inputTextResponse.size() > count) {
			inputFieldText = DataManager.system.inputTextResponse.get(count);
		} else {
			DataManager.system.inputTextResponse.add("");
		}
		if (System.currentTimeMillis() / 500 % 2 == 0) {
			inputFieldText = inputFieldText + "|";
		}
		if (DataManager.system.os != 1) { //adjusts font location for macOS
			r.font2.drawString(xPos + (int)(15 * DataManager.system.uiZoom), yPos + (int)(5 * DataManager.system.uiZoom), inputFieldText);
		} else {
			r.font2.drawString(xPos + (int)(15 * DataManager.system.uiZoom), yPos + (int)(15 * DataManager.system.uiZoom), inputFieldText);
		}
		
	}
	private static void drawNotice(Renderer r, String text, int count) {
		if (DataManager.settings.screenWidth > 1.6 * DataManager.settings.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(DataManager.settings.screenHeight - DataManager.settings.screenWidth / 1.6) / 2, DataManager.settings.screenWidth,(int)(DataManager.settings.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(DataManager.settings.screenWidth - DataManager.settings.screenHeight * 1.6)/2, 0, (int)(DataManager.settings.screenHeight * 1.6),DataManager.settings.screenHeight);

		}
		int textX = (DataManager.settings.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (DataManager.settings.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
	public static void closeOptionPane() {
		if (DataManager.system.noticeText.size() > 0) {
			int index = DataManager.system.noticeText.size() - 1;
			DataManager.system.noticeText.remove(index);
		} else {
			int index = DataManager.system.inputText.size() - 1;
			DataManager.system.inputText.remove(index);
			DataManager.system.lastInputMessage = DataManager.system.inputTextResponse.get(index);
			DataManager.system.inputTextResponse.remove(index);
		}
	}
}
