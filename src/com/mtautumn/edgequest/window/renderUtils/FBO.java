package com.mtautumn.edgequest.window.renderUtils;

import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

public class FBO {
   public int colorTextureID;
    int framebufferID;
    public int width;
    public int height;
    public int intendedWidth;
    public int intendedHeight;
	public FBO(int width, int height) {
		this.width = nearestPower2(width);
		this.height = nearestPower2(height);
		intendedWidth = width;
		intendedHeight = height;
		framebufferID = glGenFramebuffersEXT();
		colorTextureID = glGenTextures();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		
		glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, this.width, this.height, 0,GL_RGB, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
	}
	public FBO(int width, int height, boolean grayscale) {
		this.width = nearestPower2(width);
		this.height = nearestPower2(height);
		intendedWidth = width;
		intendedHeight = height;
		framebufferID = glGenFramebuffersEXT();
		colorTextureID = glGenTextures();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		
		glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd

		if (grayscale) {
	        glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE8, this.width, this.height, 0,GL_LUMINANCE, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
		} else {
	        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, this.width, this.height, 0,GL_RGB, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
		}
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                                    // Swithch back to normal framebuffer rendering
	
	}
	
	public void enableBuffer() {
		glBindTexture(GL_TEXTURE_2D, 0);                                // unlink textures because if we dont it all is gonna fail
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);        // switch to rendering on our FBO
        glClearColor (0.0f, 0.0f, 0.0f, 1.0f);
        glClear (GL_COLOR_BUFFER_BIT);         
	}
	public void enableBuffer(float r, float g, float b) {
		glBindTexture(GL_TEXTURE_2D, 0);                                // unlink textures because if we dont it all is gonna fail
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);        // switch to rendering on our FBO
        glClearColor (r, g, b, 1.0f);
        glClear (GL_COLOR_BUFFER_BIT);    
	}
	public void disableBuffer() {
		glEnable(GL_TEXTURE_2D);                                        // enable texturing
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);                    // switch to rendering on the framebuffer

	}
	public void drawBuffer(float x, float y, float width, float height) {
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		float paddingX = (float) (Double.valueOf(intendedWidth)/Double.valueOf(this.width));
		float paddingY = (float) (Double.valueOf(intendedHeight)/Double.valueOf(this.height));
		glBegin(GL_QUADS);
		glTexCoord2f(0,paddingY);
		glVertex2f(x,y);
		glTexCoord2f(paddingX,paddingY);
		glVertex2f(x+width,y);
		glTexCoord2f(paddingX,0);
		glVertex2f(x+width,y+height);
		glTexCoord2f(0,0);
		glVertex2f(x,y+height);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	private static int nearestPower2(int size) {
		int i = 1;
		for (; i < size; i *= 2) {
			;
		}
		return i;
	}
}
