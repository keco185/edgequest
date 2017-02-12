package com.mtautumn.edgequest.window;

import java.awt.Font;
import java.io.File;
import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.TextureManager;
import com.mtautumn.edgequest.data.DataManager;
import com.mtautumn.edgequest.window.layers.Layers;
import com.mtautumn.edgequest.window.renderUtils.FBO;
import com.mtautumn.edgequest.window.renderUtils.LightingVBO;
import com.mtautumn.edgequest.window.renderUtils.ShaderProgram;
import com.mtautumn.edgequest.window.renderUtils.TerrainVBO;

public class Renderer {
	public DataManager dataManager;
	public TextureManager textureManager;
	public LaunchScreenManager launchScreenManager;
	public UnicodeFont font;
	public UnicodeFont font2;
	public UnicodeFont backpackFont;
	public UnicodeFont buttonFont;
	public UnicodeFont damageFont;
	public UnicodeFont tooltipFont;
	private double lastUIZoom;
	private final double awtFontSize = 12;
	private final double awt2FontSize = 36;
	private final float buttonFontSize = 42;
	private final double awtBackpackFontSize = 14;
	private final double awtDamageFontSize = 14;
	private final double awtTooltipFontSize = 12;
	public ShaderProgram lightingShader;
	public ShaderProgram lightingColorShader;
	public ShaderProgram terrainDrawShader;
	public TerrainVBO terrainVBO;
	public LightingVBO lightingVBODarkness;
	public LightingVBO lightingVBOBrightness;
	public FBO lightingFBO;
	public FBO preLightingFBO;
	public FBO lightingColorFBO;
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

		glViewport(0,0,width,height);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		lightingShader = new ShaderProgram();
		lightingColorShader = new ShaderProgram();
		terrainDrawShader = new ShaderProgram();
		lightingFBO = new FBO(width, height);
		preLightingFBO = new FBO(width, height);
		lightingColorFBO = new FBO(width, height);
		try {
			terrainDrawShader.init("shaders/terrainDraw.vert", "shaders/terrainDraw.frag", TextureManager.getLocal());
			lightingColorShader.init("shaders/lightingColor.vert", "shaders/lightingColor.frag", TextureManager.getLocal());
			lightingShader.init("shaders/lighting.vert", "shaders/lighting.frag", TextureManager.getLocal());
		} catch (URISyntaxException e) {
			dataManager.system.running = false;
			e.printStackTrace();
			System.err.println("Fatal Error: Could not load lighting shaders");
		}
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
		Font awtTooltipFont = new Font("Arial", Font.PLAIN, (int) (awtTooltipFontSize * lastUIZoom));
		font = new UnicodeFont(awtFont);
		font2 = new UnicodeFont(awtFont2);
		backpackFont = new UnicodeFont(awtBackpackFont);
		damageFont = new UnicodeFont(awtDamageFont);
		tooltipFont = new UnicodeFont(awtTooltipFont);
		setupFont(font);
		setupFont(font2);
		setupFont(backpackFont);
		setupFont(damageFont);
		setupFont(tooltipFont);
		try {
			float fontSize = buttonFontSize;// * (float)lastUIZoom;
			Font awtButtonFont = Font.createFont(Font.TRUETYPE_FONT, new File(textureManager.jarLocal + "/textures/fonts/buttons.otf")).deriveFont((float) (fontSize * lastUIZoom));
			buttonFont = new UnicodeFont(awtButtonFont);
			setupFont(buttonFont);
		} catch (Exception e) {
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
			lightingFBO = new FBO(Display.getWidth(), Display.getHeight());
			preLightingFBO = new FBO(Display.getWidth(), Display.getHeight());
			lightingColorFBO = new FBO(Display.getWidth(), Display.getHeight());
		}
		glClear(GL_COLOR_BUFFER_BIT);

		try {
			Layers.draw(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Display.update();
		if (Display.isCloseRequested()) {
			Display.destroy();
			System.exit(0);
		}
	}


	public void fillRect(float x, float y, float width, float height, float r, float g, float b, float a) {
		glBindTexture(GL_TEXTURE_2D, 0);
		Color.white.bind();
		glColor4f (r,g,b,a);
		glBegin(GL_QUADS);
		glVertex2f(x,y);
		glVertex2f(x+width,y);
		glVertex2f(x+width,y+height);
		glVertex2f(x,y+height);
		glEnd();
	}
	public void fillLightingTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float alpha1, float alpha2, float alpha3) {
		int location = GL20.glGetAttribLocation(lightingShader.getProgramId(),"radius");
		glColor4f (1.0f,1.0f,1.0f,alpha1);
		glBegin(GL_TRIANGLES);
		GL20.glVertexAttrib1f(location, alpha1);
		glVertex2f(x1,y1);
		glColor4f (1.0f,1.0f,1.0f,alpha2);
		GL20.glVertexAttrib1f(location, alpha2);
		glVertex2f(x2,y2);
		glColor4f (1.0f,1.0f,1.0f,alpha3);
		GL20.glVertexAttrib1f(location, alpha3);
		glVertex2f(x3,y3);
		glEnd();
	}
	public void fillLightingColorTriangle(float x1, float y1, float x2, float y2, float x3, float y3, float alpha1, float alpha2, float alpha3) {
		int location = GL20.glGetAttribLocation(lightingColorShader.getProgramId(),"radius");
		glColor4f (1.0f,1.0f,1.0f,alpha1);
		glBegin(GL_TRIANGLES);
		GL20.glVertexAttrib1f(location, alpha1);
		glVertex2f(x1,y1);
		glColor4f (1.0f,1.0f,1.0f,alpha2);
		GL20.glVertexAttrib1f(location, alpha2);
		glVertex2f(x2,y2);
		glColor4f (1.0f,1.0f,1.0f,alpha3);
		GL20.glVertexAttrib1f(location, alpha3);
		glVertex2f(x3,y3);
		glEnd();
	}
	public void fillRect(float x, float y, float width, float height) {
		Color.white.bind();
		int location = GL20.glGetAttribLocation(lightingShader.getProgramId(),"posIn");
		glBegin(GL_QUADS);
		GL20.glVertexAttrib2f(location,0.0f,0.0f);
		//glColor4f (r,g,b,a1);
		glVertex2f(x,y);
		GL20.glVertexAttrib2f(location,1.0f,0.0f);
		//glColor4f (r,g,b,a2);
		glVertex2f(x+width,y);
		GL20.glVertexAttrib2f(location,1.0f,1.0f);
		//glColor4f (r,g,b,a3);
		glVertex2f(x+width,y+height);
		GL20.glVertexAttrib2f(location,0.0f,1.0f);
		//glColor4f (r,g,b,a4);
		glVertex2f(x,y+height);
		glEnd();
	}
	public void drawTexture(Texture texture, float x, float y, float width, float height) {
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		float paddingX = texture.getImageWidth();
		paddingX /= nearestPower2(paddingX);
		float paddingY = texture.getImageHeight();
		paddingY /= nearestPower2(paddingY);
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(x,y);
		glTexCoord2f(paddingX,0);
		glVertex2f(x+width,y);
		glTexCoord2f(paddingX,paddingY);
		glVertex2f(x+width,y+height);
		glTexCoord2f(0,paddingY);
		glVertex2f(x,y+height);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
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
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glBegin(GL_QUADS);
		glTexCoord2f(0,0);
		glVertex2f(-halfWidth,-halfHeight);
		glTexCoord2f(paddingX,0);
		glVertex2f(+halfWidth,-halfHeight);
		glTexCoord2f(paddingX,paddingY);
		glVertex2f(+halfWidth,+halfHeight);
		glTexCoord2f(0,paddingY);
		glVertex2f(-halfWidth,+halfHeight);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}
	private static float nearestPower2(float size) {
		int i = 1;
		for (; i < size; i *= 2);
		return i;
	}


	public double[] getCharaterBlockInfo() {
		double[] blockInfo = {0.0,0.0,0.0}; //0 - terrain block 1 - structure block 2 - biome
		int charX = (int) Math.floor(dataManager.characterManager.characterEntity.getX());
		int charY = (int) Math.floor(dataManager.characterManager.characterEntity.getY());
		if (dataManager.world.isGroundBlock(dataManager.characterManager.characterEntity, charX, charY)) {
			blockInfo[0] = dataManager.world.getGroundBlock(dataManager.characterManager.characterEntity, charX, charY);
		}
		if (dataManager.world.isStructBlock(dataManager.characterManager.characterEntity, charX, charY)) {
			blockInfo[1] = dataManager.world.getStructBlock(dataManager.characterManager.characterEntity, charX, charY);
		}
		return blockInfo;
	}
}