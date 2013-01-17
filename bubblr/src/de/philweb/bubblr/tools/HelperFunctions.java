package de.philweb.bubblr.tools;

import de.philweb.bubblr.Welt;


public class HelperFunctions {

	

	
	public static float meterToPixels(float meter) {
		
		return meter * Welt.PIXELS_PER_METER;
	}
	
	
	public static float pixelsToMeter(float pixels) {
		
		return pixels / Welt.PIXELS_PER_METER;		
	}
	
	
	
	
	//---------return random integer ---------------------------------------
	
	public static int getRandomInt(int anzahl, float randomFloat) {
	
		
		return (int) ((randomFloat * anzahl) + 1);
	}

	
}
