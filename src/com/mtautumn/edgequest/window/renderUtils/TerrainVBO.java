package com.mtautumn.edgequest.window.renderUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.mtautumn.edgequest.data.DataManager;

public class TerrainVBO {
	FloatBuffer verticies;
	FloatBuffer textCoords;
	ArrayList<Float> verticiesArray = new ArrayList<Float>();
	ArrayList<Float> textCoordsArray = new ArrayList<Float>();
	int vaoHandle;
	float invAtlasWidth;
	float invAtlasHeight;
	public TerrainVBO(DataManager dm) {
		invAtlasWidth = 1f/dm.settings.atlasMap.get("dimensions")[0];
		invAtlasHeight = 1f/dm.settings.atlasMap.get("dimensions")[1];
	}
	public void addVertex(float x, float y) {
		verticiesArray.add(x);
		verticiesArray.add(y);
	}
	public void addTexture(float x, float y) {
		textCoordsArray.add(x);
		textCoordsArray.add(y);
	}
	public void addQuad(float x, float y, float width, float height) {
		addVertex(x,y);
		addVertex(x+width,y);
		addVertex(x+width,y+height);
		addVertex(x,y+height);
	}
	public void addTextureQuad(int[] pos) {
		float x = pos[0] * invAtlasWidth + 0.001f;
		float y = pos[1] * invAtlasHeight + 0.001f;
		addTexture(x,y);
		addTexture(x+invAtlasWidth - 0.002f,y);
		addTexture(x+invAtlasWidth - 0.002f,y+invAtlasHeight - 0.002f);
		addTexture(x,y+invAtlasHeight - 0.002f);
	}
	public void preWrite() {
		verticies = BufferUtils.createFloatBuffer(verticiesArray.size());
		textCoords = BufferUtils.createFloatBuffer(textCoordsArray.size());
		for (int i = 0; i < verticiesArray.size(); i++) {
			verticies.put(verticiesArray.get(i));
			textCoords.put(textCoordsArray.get(i));
		}
		verticies.flip();
		textCoords.flip();
	}
	public void write(Texture texture) {	
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

		int verticiesBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticiesBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticies, GL15.GL_STATIC_DRAW);
		glVertexPointer(2, GL_FLOAT, 0, 0L);

		int textureBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textCoords, GL15.GL_STATIC_DRAW);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, verticiesArray.size());

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);


	}
}
