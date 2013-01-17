package de.philweb.bubblr;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.philweb.bubblr.tools.IActivityRequestHandler;

public class BubblrDesktop implements IActivityRequestHandler {
	
	// this is for cheating the desktop-app to make it work with the handler in constructor (see: http://code.google.com/p/libgdx/wiki/AdMobInLibgdx)
	private static BubblrDesktop application;	
	
	public static void main (String[] argv) {
		
		// this is for cheating the desktop-app to make it work with the handler in constructor (see: http://code.google.com/p/libgdx/wiki/AdMobInLibgdx)
        if (application == null) {
            application = new BubblrDesktop();
        }

        
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "bubblr";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 768;
//		cfg.width = 800;
//		cfg.height = 480;
		new LwjglApplication(new Bubblr(application, false), cfg);
	}

	


	@Override
	public void swarmInit() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void showToast(String string) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void openLink(String url) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void askSwarmLogin(String string, String string2) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void askExit(String dialogSubject, String dialogText) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void showToast(String toastText, int duration) {
		// TODO Auto-generated method stub
		
	}



}
