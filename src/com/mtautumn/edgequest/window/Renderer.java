package com.mtautumn.edgequest.window;

import java.awt.Font;
import java.io.File;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.TextureManager;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.managers.LaunchScreenManager;
import com.mtautumn.edgequest.window.managers.MenuButtonManager;

public class Renderer {
	public DataManager dataManager;
	public TextureManager textureManager;
	public LaunchScreenManager launchScreenManager;
	public UnicodeFont font;
	public UnicodeFont font2;
	public UnicodeFont backpackFont;
	public UnicodeFont buttonFont;
	public UnicodeFont damageFont;
	private double lastUIZoom;
	private final double awtFontSize = 12;
	private final double awt2FontSize = 36;
	private final float buttonFontSize = 42;
	private final double awtBackpackFontSize = 14;
	private final double awtDamageFontSize = 14;
	public Renderer(DataManager dataManager) {
		this.dataManager = dataManager;
		lastUIZoom = dataManager.system.uiZoom;
	}
	public void initGL(int width, int height) {
		try {
			DisplayMode displayMode = null;
			DisplayMode[] modes;
			try {
				modes = Display.getAvailableDisplayModes();

				for (int i = 0; i < modes.length; i++)
				{
					displayMode = modes[i];
				}
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
			Display.setDisplayMode(displayMode);
			Display.create();
			Display.setVSyncEnabled(true);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		glEnable(GL_TEXTURE_2D);               

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          
		glEnable(GL_COLOR_ARRAY);
		glColorMask(true, true, true, true);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		glViewport(0,0,width,height);
		glMatrixMode(GL_MODELVIEW);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}
	@SuppressWarnings("unchecked")
	private static void setupFont(UnicodeFont font) {
		try {
			font.addAsciiGlyphs();
			font.addGlyphs(400, 600);
			font.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			font.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void assignFonts() {
		Font awtFont = new Font("Arial", Font.BOLD, (int) (awtFontSize * lastUIZoom));
		Font awtFont2 = new Font("Helvetica", Font.PLAIN, (int) (awt2FontSize * lastUIZoom));
		Font awtBackpackFont = new Font("Helvetica", Font.BOLD, (int) (awtBackpackFontSize * lastUIZoom));
		Font awtDamageFont = new Font("Verdana", Font.BOLD, (int) (awtDamageFontSize * lastUIZoom));
		font = new UnicodeFont(awtFont);
		font2 = new UnicodeFont(awtFont2);
		backpackFont = new UnicodeFont(awtBackpackFont);
		damageFont = new UnicodeFont(awtDamageFont);
		setupFont(font);
		setupFont(font2);
		setupFont(backpackFont);
		setupFont(damageFont);
		try {
			float fontSize = buttonFontSize;// * (float)lastUIZoom;
			Font awtButtonFont = Font.createFont(Font.TRUETYPE_FONT, new File("textures/fonts/buttons.otf")).deriveFont((float) (fontSize * lastUIZoom));
			buttonFont = new UnicodeFont(awtButtonFont);
			setupFont(buttonFont);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadManagers() {
		textureManager = new TextureManager();
		launchScreenManager = new LaunchScreenManager(dataManager);
		dataManager.menuButtonManager = new MenuButtonManager(dataManager);
		assignFonts();
	}

	private double oldX = 800;
	private double oldY = 600;
	private boolean wasVSync = true;
	private DisplayMode oldDisplayMode;
	public void drawFrame() {
		if (lastUIZoom != dataManager.system.uiZoom) {
			lastUIZoom = dataManager.system.uiZoom;
			assignFonts();
		}
		if (dataManager.settings.vSyncOn && !wasVSync) {
			wasVSync = true;
			Display.setVSyncEnabled(true);
		} else if (!dataManager.settings.vSyncOn && wasVSync) {
			wasVSync = false;
			Display.setVSyncEnabled(false);
		}
		try {
			if (dataManager.settings.isFullScreen && !Display.isFullscreen()) {
				Display.setFullscreen(true);
				oldDisplayMode = Display.getDisplayMode();
				int largest = 0;
				int largestPos = 0;
				for (int i = 0; i < Display.getAvailableDisplayModes().length; i++) {
					if (Display.getAvailableDisplayModes()[i].getWidth() > largest) {
						largestPos = i;
						largest = Display.getAvailableDisplayModes()[i].getWidth();
					}
				}
				Display.setDisplayMode(Display.getAvailableDisplayModes()[largestPos]);
			} else if (!dataManager.settings.isFullScreen && Display.isFullscreen()) {
				Display.setFullscreen(false);
				Display.setDisplayMode(oldDisplayMode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		if (oldX != Display.getWidth() || oldY != Display.getHeight()) {
			glScaled(oldX/Display.getWidth(), oldY/Display.getHeight(), 1);
			oldX = Display.getWidth();
			oldY = Display.getHeight();
		}
		glClear(GL_COLOR_BUFFER_BIT);

		Layers.draw(this);

		Display.update();
		if (Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
	}


	public void fillRect(float x, float y, float width, float height, float r, float g, float b, float a) {
		Color.white.bind();
		glColor4f (r,g,b,a);
		glBegin(GL_QUADS);
		glVertex2f(x,y);
		glVertex2f(x+width,y);
		glVertex2f(x+width,y+height);
		glVertex2f(x,y+height);
		glEnd();
	}

	public void drawTexture(Texture texture, float x, float y, float width, float height) {
		Color.white.bind();
		texture.bind();
		float paddingX = texture.getImageWidth();
		paddingX /= nearestPower2(paddingX);
		float paddingY = texture.getImageHeight();
		paddingY /= nearestPower2(paddingY);
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x,y);
		glTexCoord2f(1f*paddingX,0);
		glVertex2f(x+width,y);
		glTexCoord2f(1f*paddingX,1f*paddingY);
		glVertex2f(x+width,y+height);
		glTexCoord2f(0,1f*paddingY);
		glVertex2f(x,y+height);
		glEnd();
	}
	public void drawTexture(Texture texture, float x, float y, float width, float height, float angle) {
		Color.white.bind();
		glPushMatrix();
		float halfWidth = width/2f;
		float halfHeight = height/2f;
		glTranslatef(x+halfWidth,y+halfHeight, 0);
		glRotatef( angle * 57.2958f, 0, 0, 1 );
		float paddingX = texture.getImageWidth();
		paddingX /= nearestPower2(paddingX);
		float paddingY = texture.getImageHeight();
		paddingY /= nearestPower2(paddingY);
		texture.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(-halfWidth,-halfHeight);
		glTexCoord2f(1*paddingX,0);
		glVertex2f(+halfWidth,-halfHeight);
		glTexCoord2f(1*paddingX,1*paddingY);
		glVertex2f(+halfWidth,+halfHeight);
		glTexCoord2f(0,1*paddingY);
		glVertex2f(-halfWidth,+halfHeight);
		glEnd();

		glPopMatrix();
	}
	private static float nearestPower2(float size) {
		int i = 1;
		for (; i < size; i *= 2);
		return i;
	}


	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome 3 - lighting
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
		if (dataManager.world.isGroundBlock(charX, charY)) {
			blockInfo[0] = dataManager.world.getGroundBlock(charX, charY);
		}
		if (dataManager.world.isStructBlock(charX, charY)) {
			blockInfo[1] = dataManager.world.getStructBlock(charX, charY);
		}
		if (dataManager.world.isLight(charX, charY)) {
			blockInfo[3] = dataManager.world.getLight(charX, charY);
		}
		return blockInfo;
	}
}