package de.philweb.bubblr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.tools.LanguageManager;


public class LoadingAsyncScreen extends Screen {
	
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Vector3 touchPoint;
	
	Texture background;
	TextureRegion backgroundRegion;
	TextureRegion whitePixel;
	
	Color color;
	float oldRed;
	float oldGreen;
	float oldBlue;
	float oldAlpha;
	
	boolean assetsLoaded = false;
	
	public boolean areImagesReady = false;

	
	float progress = 0f;
	

	 
	public LoadingAsyncScreen (Bubblr bubblr) {
		super(bubblr);

		
		guiCam = new OrthographicCamera(800, 480);
		guiCam.position.set(800 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		
		background = new Texture(Gdx.files.internal("data/splash.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		backgroundRegion = new TextureRegion(background, 0, 0, 800, 480);		//------ TODO: diese texturen auch in assetmanager aufnehmen! ?
		whitePixel = new TextureRegion(background, 0, 600, 1, 1);
	
		Gdx.input.setCatchBackKey(true);	// back key abfangen

		
		
		//--------- setup Assetmanager --------------------------
		Assets.loadRessources(Bubblr.getAssetManager());
		
	}


    
	
	@Override
	public void update (float deltaTime) {		
			
		if (Gdx.input.isKeyPressed(Keys.BACK)){		// back-key: zur�ck
			
//			background.dispose();
			Gdx.app.exit();
			return;
		}
		
		//----- get assetmanager progress ------------------------
		
	      if(Bubblr.getAssetManager().update() == true) {
	          
	    	  // we are done loading
	    	  
	    	  if (areImagesReady == false) {
	    		  
	    		  Assets.assignRessources(Bubblr.getAssetManager());
	    		  
	    		  areImagesReady = true;
	    	  }
	    	  
	    	  else {
	    	  	    		  

	    		  bubblr.lang = new LanguageManager(bubblr.prefs.getLanguage());
	    		  
	    		  //----- set font to language ---------------------
	    		  
	    			if (bubblr.lang.getLanguage().equals("en_UK")) Assets.font = Assets.font10_en;
	    			if (bubblr.lang.getLanguage().equals("de_DE")) Assets.font = Assets.font10_de;
	    			if (bubblr.lang.getLanguage().equals("pl_PL")) Assets.font = Assets.font10_pl;
	    			if (bubblr.lang.getLanguage().equals("es_ES")) Assets.font = Assets.font10_es; 
	    		  
	    			
	    		  bubblr.changeScreenToScene2DScreen(new CoolMainMenu(bubblr, "MainMenu"));
	    	  }
	       }
	      
    	  progress = Bubblr.getAssetManager().getProgress();	
	}

	
	
	@Override
	public void present (float deltaTime) {
		
		GLCommon gl = Gdx.gl;
//			gl.glClearColor(0, 0, 0, 0);
//			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		
		batcher.enableBlending();
		batcher.begin();
		
		batcher.draw(backgroundRegion, 0, 0, 800, 480);


    	//======== progress bar zeichnen ===========================
    	  
		//--- zuerst originalfarben speichern
		color = batcher.getColor();//get current Color, you can't modify directly
		oldAlpha = color.a;
		oldRed = color.r;
		oldGreen = color.g; //save its green
		oldBlue = color.b; //save its blue
		
    	//---- color auf gr�n
		color.r = 0.0f; 
		color.g = 1.0f; 
		color.b = 0.0f;
		color.a = 1.0f;
		batcher.setColor(color); //set it
		
	  
		batcher.draw(whitePixel, 400f - (390f / 2) +2f, 146f, 392f * progress, 20f);	//---- gr�nen balken in abh�ngigkeit von progress stretchen
		
	  
	  	//---- farbe wieder auf original (weiss)
		color.r = oldRed;
		color.g = oldGreen;
		color.b = oldBlue;
		color.a = oldAlpha;
			
		batcher.setColor(color); //set it
	    	  
			
	    batcher.end();
	}

	
	
	
	
	
	
	@Override
	public void pause () {
		
		//---------------------------------------
		
		super.pause();	//-------- important to pause music ---------------
		
		//--------------------------------------
		
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}


//=============================================================

