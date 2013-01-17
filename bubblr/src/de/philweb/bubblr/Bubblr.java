package de.philweb.bubblr;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import de.philweb.bubblr.screens.LoadingAsyncScreen;
import de.philweb.bubblr.screens.Screen;
import de.philweb.bubblr.tools.BubblrPreferences;
import de.philweb.bubblr.tools.IActivityRequestHandler;
import de.philweb.bubblr.tools.LanguageManager;


public class Bubblr extends Game {
	

	
	//============= Admin-Functions ============================================================
	
	public boolean adminMode = false;	// set to false!!
	public final boolean debugMode = false;	// for log output => set to false!!
	
	
	//==========================================================================================
	
	public boolean isDemoVersion;
	public BubblrPreferences prefs;


    public LanguageManager lang; 
    
    protected static AssetManager m_assetManager;
    
  
    
    
	//--------- handler f�r ads etc an superklasse �bergeben -------
	public Bubblr(IActivityRequestHandler handler, boolean isDemoVersion) {
	
		super(handler);
		
		this.isDemoVersion = isDemoVersion;
		
		
		m_assetManager = new AssetManager(); 	// must be initialized before super.create (else: = null)

	}
	//---------------------------------------------------
	
	@Override
	public Screen getStartScreen () {
		

		return new LoadingAsyncScreen(this);
		
	}
	
	
	@Override
	public void create () {
		
	
		prefs = new BubblrPreferences(isDemoVersion, this);

				
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
			

		//-----------------------
		
		super.create();
		
		
		//-----------------------
			
			
	}

	

	//-----------------------------------------------------------
	
	public void exit() {
		
		if (Gdx.app.getType() == Application.ApplicationType.Android) {												// crasht wenn back button gedr�ckt wird :(
			
			
			myRequestHandler.askExit(lang.getString("Exit?"), lang.getString("Do you really want to exit?"));

				 
		}
		else {
			
			exit_quit();
		}
	}
	
	//----- gets called from handler after dialog ------------
	
	public void exit_quit() {
	
		
		Gdx.app.exit();
	}
	//--------------------------------------------------
	

	
	  public BubblrPreferences getPreferences()
	  {
	      return prefs;
	  }
	  
	  
	  
	/**
	 * @return resource management system
	 */
	public static AssetManager getAssetManager() {
		return m_assetManager;
	}
		
		
		
	
	//----- inputProcessor ---------------
	
	@Override
	public boolean keyDown(int keycode) {

	      return false;
	 }
	
	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
	       
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	

}

