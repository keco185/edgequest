package com.mtautumn.edgequest.window.layers;

import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.window.Renderer;

public class OptionPane {
	
	public static void draw(Renderer r) {
		for (int i = 0; i < SystemData.inputText.size(); i++) {
			drawInput(r, SystemData.inputText.get(i), i);
		}
		for (int i = 0; i < SystemData.noticeText.size(); i++) {
			drawNotice(r, SystemData.noticeText.get(i), i);
		}
	}
	private static void drawInput(Renderer r, String text, int count) {
		if (SettingsData.screenWidth > 1.6 * SettingsData.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(SettingsData.screenHeight - SettingsData.screenWidth / 1.6) / 2, SettingsData.screenWidth,(int)(SettingsData.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(SettingsData.screenWidth - SettingsData.screenHeight * 1.6)/2, 0, (int)(SettingsData.screenHeight * 1.6),SettingsData.screenHeight);

		}
		int textX = (SettingsData.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (SettingsData.screenHeight) / 2 - r.font2.getHeight(text) - (int)(100 * SystemData.uiZoom);
		r.font2.drawString(textX, textY, text);
		int xPos = (SettingsData.screenWidth - (int)(400 * SystemData.uiZoom)) / 2;
		int yPos = (SettingsData.screenHeight - (int)(60 * SystemData.uiZoom)) / 2;
		r.drawTexture(r.textureManager.getTexture("inputField"), xPos, yPos, (int)(400 * SystemData.uiZoom), (int)(60 * SystemData.uiZoom));
		String inputFieldText = "";
		if (SystemData.inputTextResponse.size() > count) {
			inputFieldText = SystemData.inputTextResponse.get(count);
		} else {
			SystemData.inputTextResponse.add("");
		}
		if (System.currentTimeMillis() / 500 % 2 == 0) {
			inputFieldText = inputFieldText + "|";
		}
		if (SystemData.os != 1) { //adjusts font location for macOS
			r.font2.drawString(xPos + (int)(15 * SystemData.uiZoom), yPos + (int)(5 * SystemData.uiZoom), inputFieldText);
		} else {
			r.font2.drawString(xPos + (int)(15 * SystemData.uiZoom), yPos + (int)(15 * SystemData.uiZoom), inputFieldText);
		}
		
	}
	private static void drawNotice(Renderer r, String text, int count) {
		if (SettingsData.screenWidth > 1.6 * SettingsData.screenHeight) {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), 0, (int)(SettingsData.screenHeight - SettingsData.screenWidth / 1.6) / 2, SettingsData.screenWidth,(int)(SettingsData.screenWidth / 1.6));
		} else {
			r.drawTexture(r.textureManager.getTexture("launchScreenBackground"), (int)(SettingsData.screenWidth - SettingsData.screenHeight * 1.6)/2, 0, (int)(SettingsData.screenHeight * 1.6),SettingsData.screenHeight);

		}
		int textX = (SettingsData.screenWidth - r.font2.getWidth(text)) / 2;
		int textY = (SettingsData.screenHeight) / 2 - r.font2.getHeight(text);
		r.font2.drawString(textX, textY, text);
	}
	public static void closeOptionPane() {
		if (SystemData.noticeText.size() > 0) {
			int index = SystemData.noticeText.size() - 1;
			SystemData.noticeText.remove(index);
		} else {
			int index = SystemData.inputText.size() - 1;
			SystemData.inputText.remove(index);
			SystemData.lastInputMessage = SystemData.inputTextResponse.get(index);
			SystemData.inputTextResponse.remove(index);
		}
	}
}
