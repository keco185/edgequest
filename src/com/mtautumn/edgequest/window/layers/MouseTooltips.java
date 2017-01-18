package com.mtautumn.edgequest.window.layers;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.mtautumn.edgequest.ItemDrop;
import com.mtautumn.edgequest.window.Renderer;

public class MouseTooltips {
	public static void draw(Renderer r) {
		if (!r.dataManager.system.isKeyboardMenu) {
			double mouseX = r.dataManager.system.mouseXExact;
			double mouseY = r.dataManager.system.mouseYExact;
			ArrayList<String> inRangeItems = new ArrayList<String>();
			for( int i = 0; i < r.dataManager.savable.itemDrops.size(); i++) {
				ItemDrop itemDrop = r.dataManager.savable.itemDrops.get(i);
				if (itemDrop.level == r.dataManager.savable.dungeonLevel) {
					if (isInRange(mouseX,mouseY,itemDrop.x,itemDrop.y)) {
						if (itemDrop.item.getItemCount() > 1) {
							inRangeItems.add(r.dataManager.system.blockIDMap.get(itemDrop.item.getItemID()).getName() + " (" + itemDrop.item.getItemCount() + ")");
						} else if (itemDrop.item.getItemCount() > 0) {
							inRangeItems.add(r.dataManager.system.blockIDMap.get(itemDrop.item.getItemID()).getName());
						}
					}
				}
			}
			if (inRangeItems.size() > 0) {
				drawTooltip(r.dataManager.system.mousePosition, r.dataManager.settings.screenWidth, r.dataManager.settings.screenHeight, inRangeItems, r);
			}
		}
	}
	private static boolean isInRange(double mouseX, double mouseY, double itemX, double itemY) {
		return Math.sqrt(Math.pow(mouseX-itemX,2) + Math.pow(mouseY-itemY, 2)) < 1.0;
	}
	private static void drawTooltip(Point mousePos, int screenWidth, int screenHeight, ArrayList<String> lines, Renderer r) {
		int tooltipWidth = 0;
		int padding = (int) (12 * r.dataManager.system.uiZoom);
		for (String string : lines) {
			int length = r.tooltipFont.getWidth(string);
			if (length > tooltipWidth) tooltipWidth = length;
		}
		double lineHeight = (double) r.tooltipFont.getLineHeight() * 1.5;
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
