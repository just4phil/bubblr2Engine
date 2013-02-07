package de.philweb.bubblr;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import de.philweb.bubblr.screens.AbstractScreen;
import de.philweb.bubblr.screens.Screen;
import de.philweb.bubblr.tools.IActivityRequestHandler;



public abstract class Game implements ApplicationListener, InputProcessor {
	
    
	Screen screen;
	AbstractScreen screenScene2D;
	int renderID = 1;
		
	
	protected OrthographicCamera _camera;		//our camera
	
	public Rectangle _viewport = null;
	
	public static final int VIRTUAL_WIDTH = 800; //25;		// in meters (px: 800)		//----- should be in meters???
	public static final int VIRTUAL_HEIGHT = 480; //15;	// in meters (px: 480)			//----- should be in meters???
	private static final float ASPECT_RATIO = 1.666667f;
	public float pixelPerMeter;

    
    //=========== Handler f�r Ads etc ==================
    
    public IActivityRequestHandler myRequestHandler;

    public Game(IActivityRequestHandler handler) {
    	
        myRequestHandler = handler;
       
	      
    }
    //==================================================
    
	
	public void setScreen (Screen helpScreen2) {
		screen = helpScreen2;
		renderID = 1;
	}

	public void setScene2DScreen (AbstractScreen scene2DScreen) {
		screenScene2D = scene2DScreen;
		renderID = 2;
	}
	
	public void changeScene2DScreenToScreen (Screen helpScreen2) {
		screen = helpScreen2;
		renderID = 1;
	}
	
	public void changeScreenToScene2DScreen (AbstractScreen scene2DScreen) {
		screen.dispose();
		screenScene2D = scene2DScreen;
		renderID = 2;
	}
	
	
	
	public abstract Screen getStartScreen ();

	
	@Override
	public void create () {
			
		// Ortographic camera
		_camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		_camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		
		
		screen = getStartScreen();
	}

	  
	
	@Override
	public void resume () {
		screen.resume();
	}

	@Override
	public void render () {
		
		if (renderID == 1) {
			screen.update(Gdx.graphics.getDeltaTime());
			screen.present(Gdx.graphics.getDeltaTime());
		}
		
		if (renderID == 2) screenScene2D.render(Gdx.graphics.getDeltaTime());
		
	}

	
	
	@Override
	public void resize (int arg0, int arg1) {			//---- code from siondream -> thx david! ---------------
		
		// calculate new viewport
        float aspectRatio = (float)arg0 / (float)arg1;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)arg1 / (float)VIRTUAL_HEIGHT;
            crop.x = (arg0 - VIRTUAL_WIDTH * scale) / 2.0f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)arg0 / (float)VIRTUAL_WIDTH;
            crop.y = (arg1 - VIRTUAL_HEIGHT * scale) / 2.0f;
        }
        else
        {
            scale = (float)arg0 / (float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH * scale;
        float h = (float)VIRTUAL_HEIGHT * scale;
        
        _viewport = new Rectangle(crop.x, crop.y, w, h);

        pixelPerMeter = 32.0f; //Gdx.graphics.getWidth() / VIRTUAL_WIDTH;	// 32.0f;			//------ ??? static or dynamic??
        Gdx.app.log("ppm", "" + pixelPerMeter);
	}

	
	
	
	@Override
	public void pause () {
		screen.pause();
	}

	@Override
	public void dispose () {
		
//		m_batch.dispose();
//		m_HUDBatch.dispose();
		
		screen.dispose();
	}
//--------------------------------

	public OrthographicCamera getCamera() {
		return _camera;
	}
}
