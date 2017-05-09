package com.mtautumn.edgequest.window.layers;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.data.SettingsData;
import com.mtautumn.edgequest.data.SystemData;
import com.mtautumn.edgequest.dataObjects.ItemDrop;
import com.mtautumn.edgequest.window.Renderer;

public class MouseTooltips {
	public static void draw(Renderer r) {
		if (!SystemData.isKeyboardMenu) {
			double mouseX = SystemData.mouseXExact;
			double mouseY = SystemData.mouseYExact;
			ArrayList<String> inRangeItems = new ArrayList<String>();
			for( int i = 0; i < DataManager.savable.itemDrops.size(); i++) {
				ItemDrop itemDrop = DataManager.savable.itemDrops.get(i);
				if (itemDrop.level == DataManager.savable.dungeonLevel) {
					if (isInRange(mouseX,mouseY,itemDrop.x,itemDrop.y)) {
						if (itemDrop.item.getItemCount() > 1) {
							inRangeItems.add(SystemData.blockIDMap.get(itemDrop.item.getItemID()).getName() + " (" + itemDrop.item.getItemCount() + ")");
						} else if (itemDrop.item.getItemCount() > 0) {
							inRangeItems.add(SystemData.blockIDMap.get(itemDrop.item.getItemID()).getName());
						}
					}
				}
			}
			if (inRangeItems.size() > 0) {
				drawTooltip(SystemData.mousePosition, SettingsData.screenWidth, SettingsData.screenHeight, inRangeItems, r);
			}
		}
	}
	private static boolean isInRange(double mouseX, double mouseY, double itemX, double itemY) {
		return Math.sqrt(Math.pow(mouseX-itemX,2) + Math.pow(mouseY-itemY, 2)) < 1.0;
	}
	private static void drawTooltip(Point mousePos, int screenWidth, int screenHeight, ArrayList<String> lines, Renderer r) {
		int tooltipWidth = 0;
		int padding = (int) (12 * SystemData.uiZoom);
		for (String string : lines) {
			int length = r.tooltipFont.getWidth(string);
			if (length > tooltipWidth) {
				tooltipWidth = length;
			}
		}
		double lineHeight = r.tooltipFont.getLineHeight() * 1.5;
		int tooltipHeight = (int) (lineHeight * lines.size());
		int startX = mousePos.x + 12;
		int startY = mousePos.y + 12;
		if (startX + padding * 2 + tooltipWidth > screenWidth) {
			startX = mousePos.x - padding * 2 - tooltipWidth - 12;
		}
		if (startY + padding * 2 + tooltipHeight > screenHeight) {
			startY = mousePos.y - padding * 2 - tooltipHeight - 12;
		}
		r.fillRect(startX, startY, tooltipWidth + padding * 2f, tooltipHeight + padding * 2f - (float) lineHeight / 3f, 0.3f, 0.3f, 0.3f, 0.8f);
		for (int i = 0; i < lines.size(); i++) {
			r.tooltipFont.drawString(startX+padding, (float) (startY+padding+lineHeight * i), lines.get(i), Color.white);
		}
	}

}
