package de.philweb.bubblr.screens;


import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.tools.BubblrPreferences;

public abstract class Screen {

	protected Bubblr bubblr;
	
	BubblrPreferences prefs;
	int i;
	int len;
	

	

	public Screen (Bubblr bubblr) {

		this.bubblr = bubblr;
		prefs = bubblr.getPreferences();
	}

	public void update (float deltaTime) {
		
		
	}

	
	
	public abstract void present (float deltaTime);
		



	
	public void pause () {
		

		
	}
	
	
	public Bubblr getGame() {
		return bubblr;
	}
	
	
	public abstract void resume ();

	public abstract void dispose ();
}

