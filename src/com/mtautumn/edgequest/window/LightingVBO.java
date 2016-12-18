package com.mtautumn.edgequest.window;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;

public class LightingVBO {
	FloatBuffer verticies;
	FloatBuffer locations;
	FloatBuffer opacities;
	ArrayList<Float> verticiesArray = new ArrayList<Float>(); //2 per vertex
	ArrayList<Float> locationAttributeArray = new ArrayList<Float>(); //2 per vertex
	ArrayList<Float> opacityAttributeArray = new ArrayList<Float>(); //4 per vertex
	public void addVertex(float x, float y) {
		verticiesArray.add(x);
		verticiesArray.add(y);
	}
	public void addLocation(float x, float y) {
		locationAttributeArray.add(x);
		locationAttributeArray.add(y);
	}
	public void addQuad(float x, float y, float width, float height) {
		addVertex(x,y);
		addLocation(0,0);
		addVertex(x+width,y);
		addLocation(1,0);
		addVertex(x+width,y+height);
		addLocation(1,1);
		addVertex(x,y+height);
		addLocation(0,1);
	}
	public void addOpacity(float a, float b, float c, float d) {
		for (int i = 0; i < 4; i++) {
			opacityAttributeArray.add(a);
			opacityAttributeArray.add(b);
			opacityAttributeArray.add(c);
			opacityAttributeArray.add(d);
		}
	}
	public void preWrite() {
		verticies = BufferUtils.createFloatBuffer(verticiesArray.size());
		locations = BufferUtils.createFloatBuffer(locationAttributeArray.size());
		opacities = BufferUtils.createFloatBuffer(opacityAttributeArray.size());
		for (int i = 0; i < verticiesArray.size(); i++) {
			verticies.put(verticiesArray.get(i));
			locations.put(locationAttributeArray.get(i));
			opacities.put(opacityAttributeArray.get(i));
		}
		for (int i = verticiesArray.size(); i < opacityAttributeArray.size(); i++) {
			opacities.put(opacityAttributeArray.get(i));
		}
		verticies.flip();
		locations.flip();
		opacities.flip();
	}
	public void write(Renderer r) {
		glBindTexture(GL_TEXTURE_2D, 0);
		Color.white.bind();

		int verticiesBufferHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, verticiesBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticies, GL15.GL_STATIC_DRAW);
		glVertexPointer(2, GL_FLOAT, 0, 0L);

		int locationsBufferHandle = GL15.glGenBuffers();
		int opacityBufferHandle = GL15.glGenBuffers();
		int locationPos = GL20.glGetAttribLocation(r.shader.getProgramId(),"posIn");
		int opacityPos = GL20.glGetAttribLocation(r.shader.getProgramId(),"opacity");
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationsBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, locations, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, opacityBufferHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, opacities, GL15.GL_STATIC_DRAW);
		GL20.glEnableVertexAttribArray(locationPos);
		GL20.glEnableVertexAttribArray(opacityPos);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, locationsBufferHandle);
		GL20.glVertexAttribPointer(locationPos, 2, GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, opacityBufferHandle);
		GL20.glVertexAttribPointer(opacityPos, 4, GL_FLOAT, false, 0, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glDrawArrays(GL_QUADS, 0, verticiesArray.size());	
		GL20.glDisableVertexAttribArray(locationPos);
		GL20.glDisableVertexAttribArray(opacityPos);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);


	}
}
