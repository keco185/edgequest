/*
 * Add light sources and control color of the light
 */

package com.mtautumn.edgequest.dataObjects;
import java.util.ArrayList;
import com.mtautumn.edgequest.updates.UpdateRayCast.Triangle;
public class LightSource {
	public double posX;
	public double posY;
	public int level;
	public final double range;
	public ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	public float r = 1.0f;
	public float g = 0.95f;
	public float b = 0.8f;
	public boolean onEntity = false;
	public boolean flicker = true;
	public float maxBrightness = 1.0f;
	public float brightness = 0.0f;
	private float brightnessMultiplier = 0.0f;
	public float warmUpSpeed = 0.01f;
	public float flickerMultiplier = 1.0f;
	
	public int flickerLowPassTimeLeft = 0;
	public int flickerHighPassTimeLeft = 0;
	public float flickerLowPassIntensity = 0.0f;
	public float flickerHighPassIntensity = 0.0f;
	public float flickerNoiseLevel = 0.005f;
	public LightSource(double posX, double posY, double range, int level) {
		this.posX = posX;
		this.posY = posY;
		this.range = range;
		this.level = level;
	}
	public void assignColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public void removeZeroTriangles() {
		for (int i = 0; i < triangles.size(); i++) {
			if (triangles.get(i).isZeroTriangle())
				triangles.remove(i);
		}
	}
	public void update() {
		float oldFlickerMultiplier = flickerMultiplier;
		flickerMultiplier = 1.0f;

		if (flicker) {
			if (flickerLowPassTimeLeft == 0) {
				if (Math.random() > 0.99) {
					flickerLowPassTimeLeft = (int) (Math.random() * 100 + 40);
					flickerLowPassIntensity = (float) (Math.random() / 18.0);
					flickerMultiplier -= flickerLowPassIntensity;
				}
			} else {
				flickerLowPassTimeLeft--;
				flickerMultiplier -= flickerLowPassIntensity;
			}
			if (flickerHighPassTimeLeft == 0) {
				if (Math.random() > 0.95) {
					flickerHighPassTimeLeft = (int) (Math.random() * 20 + 10);
					flickerHighPassIntensity = (float) (Math.random() / 25.0);
					flickerMultiplier -= flickerHighPassIntensity;
				}
			} else {
				flickerHighPassTimeLeft--;
				flickerMultiplier -= flickerHighPassIntensity;
			}
			flickerMultiplier -= (float) Math.random() * flickerNoiseLevel;
			flickerMultiplier = (float) Math.sqrt(flickerMultiplier);
			flickerMultiplier = 0.9f * oldFlickerMultiplier + 0.1f * flickerMultiplier;
		}
		
		
		if (brightnessMultiplier < 1.0f) {
			brightnessMultiplier += warmUpSpeed;
			if (brightnessMultiplier > 1.0f) {
				brightnessMultiplier = 1.0f;
			}
		}
		
		brightness = maxBrightness * flickerMultiplier * brightnessMultiplier;
	}
}
