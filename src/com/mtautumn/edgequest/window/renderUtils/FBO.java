package com.mtautumn.edgequest.window.renderUtils;

import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class FBO {
    int colorTextureID;
    int framebufferID;
    int width;
    int height;
	public FBO(int width, int height) {
		this.width = nearestPower2(width);
		this.height = nearestPower2(height);
		framebufferID = glGenFramebuffersEXT();
		colorTextureID = glGenTextures();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		
		glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.width, this.height, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
	}
	
	public void enableBuffer() {
		glBindTexture(GL_TEXTURE_2D, 0);                                // unlink textures because if we dont it all is gonna fail
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);        // switch to rendering on our FBO
        glClearColor (0.0f, 0.0f, 0.0f, 1.0f);
        glClear (GL_COLOR_BUFFER_BIT);            // Clear Screen And Depth Buffer on the fbo to red
	}
	public void disableBuffer() {
		glEnable(GL_TEXTURE_2D);                                        // enable texturing
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                    // switch to rendering on the framebuffer

	}
	public void drawBuffer(float x, float y, float width, float height) {
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, framebufferID);
		float paddingX = 1;
		float paddingY = 1;
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
	
	private static int nearestPower2(int size) {
		int i = 1;
		for (; i < size; i *= 2);
		return i;
	}
}
