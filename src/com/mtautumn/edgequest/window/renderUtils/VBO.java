package com.mtautumn.edgequest.window.renderUtils;

public interface VBO {
	
	public void addQuad(float x, float y, float width, float height);
	
	public void addVertex(float x, float y);

	public void preWrite();
	
	// public void write( :thinking: );
	
}
