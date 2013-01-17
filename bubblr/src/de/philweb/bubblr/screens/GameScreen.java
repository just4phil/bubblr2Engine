package de.philweb.bubblr.screens;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.widget.Toast;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmLeaderboardScore;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.Character;
import de.philweb.bubblr.GameObject;
import de.philweb.bubblr.Level;
import de.philweb.bubblr.OverlapTester;
import de.philweb.bubblr.Welt;
import de.philweb.bubblr.WorldRenderer;
import de.philweb.bubblr.tools.BubblrPreferences;
import de.philweb.bubblr.tools.HelperFunctions;
import de.philweb.bubblr.tools.NinePatch;



public class GameScreen extends Screen {
	
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	static final int GAME_SOLVED = 5;
	static final int GAME_LEVEL_END_POINTS = 6;
	
	boolean soundOn;
	boolean musicOn;
	boolean backKeyPressed;
	
	float accel;
	
	int state;
	int touchState;
	float jumpPower;
	float game_monsterSpeed;
	public int levelNumber;

	
	OrthographicCamera guiCam;
	Vector3 touchPoint;
	
	public Level level;
	
	int numberOfLevels;

	SpriteBatch batcher;
	WorldRenderer renderer;
	
	Rectangle readyBounds;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	
	String topText;
	String bottomText;
	String bottomText2;
	String levelTimeString;
	String FPSString;
	String levelString;	
	String mapName;	
	float topWidth;
	float topHeight;
	float bottomWidth;
	float bottom2Width;
	float scoreWidth;
	float oldScaleX;
	float oldScaleY;
	
	ArrayList<Vector2> players = new ArrayList<Vector2>();


	Color color;
	float oldRed;
	float oldGreen;
	float oldBlue;
	float oldAlpha;
//	
	// zum messen der zeitdifferenz, wenn spiel pausiert wird (f�r bubbles und lifetimes etc)
	public long timeDiff; 	
	public long timeDiff_start;
	boolean timeDiff_done;	
	
	
	//------ STRINGS --------
	final String scoreString = bubblr.lang.getString("SCORE");
	
	StringBuffer textPuffer;
	
	//--------- prefs ------------------------
	BubblrPreferences prefs;
	boolean prefsSaved;
	

	
	//--- for fixed-timestep box2d simulation:

	float frameTime;
	int stepsPerformed;
	float delta;
	
	
//	private float accum = 0f;
//	private float accumRatio;
//	private final float step = 1f / 60f;
//	private final float maxDelta = 1f / 20f; // keep from spiraling to death
//	static final int VELOCITY_ITERATIONS = 10; //6;	// 3
//	static final int POSITION_ITERATIONS = 8; //6;	// 3
	
//	static float FIXED_TIMESTEP = 0.008f;
//	static float MINIMUM_TIMESTEP = 0.001f;
//	static final int MAXIMUM_NUMBER_OF_STEPS = 20; // maximum number of steps per tick to avoid spiral of death
//	public static float LAST_PHYSICS_STEP = 0;
//	public static float STEPS_PERFORMED = 0;
//	public static float FRAME_TIME = 0;
//	public static float DELTA_TIME = 0;
//	float startTime;
//	float frameTime;
//	int stepsPerformed;
//	float endTime;
//	float delta;

	


    
    //---- to limit fps
    private long now, diff, start;
    
    
    
    
 
	//------------------------------------------------------------------------------------------------
	
	
	public GameScreen (Bubblr bubblr, int levelNumber, int lastLifes, long lastPoints, int lastMonsterKisses, int lastHattrickTwo, int lastHattrickThree, int lastHattrickFour, int lastHattrickFive, int lastMonsterKissesForExtraLifeCounter, int lastlaserCounter, boolean cameFromLevelSelector) {
		
		super(bubblr);
	
					
		this.levelNumber = levelNumber;

	    
	    //----determine number of levels
	    if (bubblr.isDemoVersion == true) {
	    	numberOfLevels = Welt.NUMBEROFLEVELS_DEMO;
	    }
	    else {
	    	numberOfLevels = Welt.NUMBEROFLEVELS_FULL;
	    }
		touchState = 0;
		

		state = GAME_READY;
		
//		int screenWidth = Gdx.graphics.getWidth();	
//		int screenHeight = Gdx.graphics.getHeight();
//		guiCam = new OrthographicCamera(screenWidth, screenHeight); // (800, 480);
//		guiCam.position.set(screenWidth / 2, screenHeight / 2, 0); // (800 / 2, 480 / 2, 0);
		
		guiCam = new OrthographicCamera(800, 480); // (800, 480);
		guiCam.position.set(800 / 2, 480 / 2, 0); // (800 / 2, 480 / 2, 0);
		
		
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		textPuffer = new StringBuffer();
		

		
		
		//---------------------------------------------------
		Gdx.input.setCatchBackKey(true);	// back key abfangen
		
   

		System.gc();	//------------------ FORCE GC
		

		game_monsterSpeed = 1.5f;	
	
		level = new Level(bubblr, game_monsterSpeed);	
		renderer = new WorldRenderer(batcher, level, levelNumber);


		
		//--------- liest die objekte aus der map aus und �bergibt diese an das weltobjekt -----
		level.mapName = bubblr.lang.getString(renderer.tiledMapHelper.getMapPropertyString("mapName"));
		level.mapToast = bubblr.lang.getString(renderer.tiledMapHelper.getMapPropertyString("mapToast"));
		players = renderer.tiledMapHelper.getCharacterList("player");	

		level.world.characterSetup(players, level.world.mapTimeLimit);
		
//--------------------------------------

		readyBounds = new Rectangle(800 - 86, 480 - 64, 64, 64);
		pauseBounds = new Rectangle(800 - 86, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(800 - 86, 480 - 64, 64, 64);
		quitBounds = new Rectangle(800 - 86, 32, 64, 64);

   
		
		bubblr.myRequestHandler.showToast(bubblr.lang.getString("Press PLAY to start"), Toast.LENGTH_SHORT);
		
		
	}

	
	//--------------------------------------------------------------------------------------------------
	
	@Override
	public void update (float deltaTime) {
		
		//---------------------------------------
		
		super.update(deltaTime);	//-------- important for update music ---------------
		
		//--------------------------------------
		
//		if (deltaTime > 0.1f) deltaTime = 0.1f;		// avoid spiral of death?

		
		renderer.GameState = state;		// wof�r war das denn nochmal ??? // TODO 
		
		
		switch (state) {
		
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused(deltaTime);
			break;
		}
	}

	private void updateReady () {			

		
		if (Gdx.input.justTouched()) {
		
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(readyBounds, touchPoint.x, touchPoint.y)) {
								
				
				System.gc();	//------------------ FORCE GC
				
				state = GAME_RUNNING;
				return;
			}
			
			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
				
				backKeyPressed = true;		// back-key: zur�ck
				return;
			}
		}
		
		if (backKeyPressed == true) {	
			
			
			backKeyPressed = false;
			

			bubblr.changeScreenToScene2DScreen(new CoolMainMenu(bubblr, "PlayMenu"));
			return;
		}
	}
//----------------------------------------------------------------------------
	
	private void updateRunning (float deltaTime) {
		
		accel = 0;
		touchState = 0;		// touch im spiel zur�ck setzen

		
		if (level.started == false) {
			level.levelTime_start = System.nanoTime();
			level.started = true;
			
		}

	
		
		
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) {
						
				
				state = GAME_PAUSED;
				return;
			}		
		}
		if (Gdx.input.isKeyPressed(Keys.BACK)){		// back-key abfangen damit man nicht aus der app fliegt wenn man daneben klickt
//			state = GAME_PAUSED;
		}
	//---------------------------------------------------	
		
		
		
		
		//------ alternative 0: without fixed-timestep
		
		level.world.update(deltaTime, timeDiff, accel, touchState, guiCam, renderer);
		level.world.updateBox2d(deltaTime, Welt.VELOCITY_ITERATIONS, Welt.POSITION_ITERATIONS);

		

		
//=============================================================================================================================
//========================= take this one :) ====================================================================================================
		
		//------ for fixed-timestep ----------------------------
	

//			frameTime = deltaTime;
//			stepsPerformed = 0;
//
//			while ( (frameTime > 0.0f) && (stepsPerformed < Welt.MAXIMUM_NUMBER_OF_STEPS)) {
//				
//				delta = Math.min( frameTime, Welt.FIXEDTIMESTEP );
//				
//				frameTime -= delta;
//				if (frameTime < (Welt.MINIMUM_TIMESTEP)) {
//					delta += frameTime;
//					frameTime = 0.0f;
//				}
//				
//				   level.world.update(delta, timeDiff, accel, touchState, guiCam, renderer);
//				   level.world.box2dwelt.step(delta, Welt.VELOCITY_ITERATIONS, Welt.POSITION_ITERATIONS);
//	        	 
//				stepsPerformed++;
//
//			}
//			level.box2dwelt.clearForces();
			

//=============================================================================================================================
//=============================================================================================================================
			
			
			
//		Gdx.app.log("stepsPerformed", "" + stepsPerformed);
		
		
		//---------------------------------------------
//	    double t = 0.0;
//
//	    double currentTime = System.currentTimeMillis() * 1000f;
//	    
//	   
//
////	    while ( !quit )
////	    {
//	         double newTime = System.currentTimeMillis() * 1000f;
//	         double frameTime = newTime - currentTime;
//	         if ( frameTime > 0.25 )
//	              frameTime = 0.25;	  // note: max frame time to avoid spiral of death
//	         currentTime = newTime;
//
//	         level.world.accum += frameTime;
//
//	         while ( level.world.accum >= Welt.FIXEDTIMESTEP )
//	         {
//	        	 
//				   level.world.update(Welt.FIXEDTIMESTEP, timeDiff, accel, touchState, guiCam);
//				   level.world.box2dwelt.step(Welt.FIXEDTIMESTEP, Welt.VELOCITY_ITERATIONS, Welt.POSITION_ITERATIONS);
//	        	 
//	              t += Welt.FIXEDTIMESTEP;
//	              level.world.accum -= Welt.FIXEDTIMESTEP;
//	         }
//
//	         level.world.alpha = level.world.accum / Welt.FIXEDTIMESTEP;
//	         
////	    }
	    
	
		//---- alternative 1 ------------ physic-engine mit fixed-timestep simulieren lassen --------------------------
		
//			level.world.accum += Math.min(deltaTime, Welt.MAXTIMESTEP);	 // avoid spiral of death!
//			level.world.alpha = level.world.accum / Welt.FIXEDTIMESTEP;
//		   
//		   while (level.world.accum > Welt.FIXEDTIMESTEP) {
//			  	    
//			   level.world.update(Welt.FIXEDTIMESTEP, timeDiff, accel, touchState, guiCam);
//			   level.world.box2dwelt.step(Welt.FIXEDTIMESTEP, Welt.VELOCITY_ITERATIONS, Welt.POSITION_ITERATIONS); //---------------- physic-engine simulieren lassen --------------------------
//			   level.world.accum -= Welt.FIXEDTIMESTEP;
//		   }
		     
		
	//---------------------------------------------------	
		
//			http://code.google.com/p/box2dlights/source/browse/trunk/box2dLight/src/testCase/Box2dLightTest.java
				
//	        private final static int MAX_FPS = 30;
//	        private final static int MIN_FPS = 15;
//	        public final static float TIME_STEP = 1f / MAX_FPS;
//	        private final static float MAX_STEPS = 1f + MAX_FPS / MIN_FPS;
//	        private final static float MAX_TIME_PER_FRAME = TIME_STEP * MAX_STEPS;
//	        private final static int VELOCITY_ITERS = 6;
//	        private final static int POSITION_ITERS = 2;
//
//	        float physicsTimeLeft;
//	        long aika;
//	        int times;
//
//	        private boolean fixedStep(float delta) {
//	                physicsTimeLeft += delta;
//	                if (physicsTimeLeft > MAX_TIME_PER_FRAME)
//	                        physicsTimeLeft = MAX_TIME_PER_FRAME;
//
//	                boolean stepped = false;
//	                while (physicsTimeLeft >= TIME_STEP) {
//	                        world.step(TIME_STEP, VELOCITY_ITERS, POSITION_ITERS);
//	                        physicsTimeLeft -= TIME_STEP;
//	                        stepped = true;
//	                }
//	                return stepped;
//	        }

	        //=============================================================================================
		
		
		// zum messen der zeitdifferenz, wenn spiel pausiert wird (f�r bubbles und lifetimes etc)
		if (timeDiff_done == true) {
			timeDiff_done = false;
			
			level.levelTime_start = level.levelTime_start + (timeDiff);	// levelzeit nach pause korrigieren
			renderer.timeDiff = timeDiff;
			
			timeDiff = 0L;
		}
	
		
		//--------------------------
		
		
	}

	//----------------------------------------------------------------------------------------
	
	private void updatePaused (float deltaTime) {
		
		
		// zum messen der zeitdifferenz, wenn spiel pausiert wird (f�r bubbles und lifetimes etc)
		if (timeDiff_done == false) {
			timeDiff_start = System.nanoTime();
			timeDiff_done = true;
		}
		timeDiff = (System.nanoTime() - timeDiff_start);
		renderer.timeDiff = timeDiff;
		
		//--------------------------------------------------------
				
		
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			
			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x, touchPoint.y)) {
		
				
				System.gc();	//------------------ FORCE GC
				
				state = GAME_RUNNING;
				return;
			}

			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
				

	
				System.gc();	//------------------ FORCE GC
				
				
				backKeyPressed = true;		// back-key: zur�ck
				return;
			}

			
		}
		
		if (backKeyPressed == true) {	
			
			backKeyPressed = false;
			bubblr.changeScreenToScene2DScreen(new CoolMainMenu(bubblr, "PlayMenu"));
			return;
		}
	}

	
//-----------------------------------------------------------------------
	
	
	
	
	
	//=================================================================================
	
	
	@Override
	public void present (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderer.render(level, batcher);				// -------------- render --------------------

		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.enableBlending();
		batcher.begin();
		
		
		switch (state) {
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		}
		
		presentLifes();
		presentData();

		batcher.end();
		
		
        
        //------------- to limit fps ------------------------
        if (Welt.RENDERER_SLEEP_MS > 0) {
        
        	now = System.currentTimeMillis();
        	diff = now - start;
		
        	if (diff < Welt.RENDERER_SLEEP_MS) {
        		try {
        			Thread.sleep(Welt.RENDERER_SLEEP_MS - diff);
        		} catch (InterruptedException e) {
        		}
        	}

        	start = System.currentTimeMillis();
        }
        //-----------------------------------------------------
         
	}

	
	
	
	private void presentReady () {
		
		ScreenControls.drawButtons(bubblr, batcher, true, musicOn, true, soundOn, true, false, true, false);  // check this out!!
		
		presentButtons();
		
		presentMapname();
		
		if (level.mapToast != "" || level.world.mapTimeLimit > 0) {
						
			color = Assets.font.getColor();//get current Color, you can't modify directly  
			oldAlpha = color.a; //save its alpha
			oldRed = color.r;
			oldGreen = color.g; //save its green
			oldBlue = color.b; //save its blue
			
			//--- save normal scale-values
			oldScaleX = Assets.font.getScaleX();
			oldScaleY = Assets.font.getScaleY();
			Assets.font.setScale(oldScaleX / 1.2f);	 //1.5
			//--------------------------------------------------------
			
			
			//---------------------------------------------------
			color.r = 0f;
			color.g = 0f;
			color.b = 0f;
			color.a = 1.0f;
			Assets.font.setColor(color.r, color.g, color.b, color.a);
			
			
			textPuffer.delete(0, textPuffer.length());
			
			
			//---- check if mapToast is set and append to toast ------------------
			if (level.mapToast != "") {
			
				textPuffer.append(level.mapToast);		// bubblr.lang.getString... not neccessary... directly in load-method
			}
			//---------------------------------------------------------------------------
			
			
			//---- check if both is set and append linebreak ------------------
			if (level.mapToast != "" && level.world.mapTimeLimit > 0) {
			
				textPuffer.append("\n");
			}
			//---------------------------------------------------------------------------
			

			//---- check if maptimelimit is set and append to toast ------------------
			if (level.world.mapTimeLimit > 0) {
			
				textPuffer.append(bubblr.lang.getString("TimeLimit")).append(": ").append(level.world.mapTimeLimit).append(" ").append(bubblr.lang.getString("seconds")).append("!");
			}
			//---------------------------------------------------------------------------
			
			
			topWidth = Assets.font.getWrappedBounds(textPuffer, 400f).width;
			topHeight = Assets.font.getWrappedBounds(textPuffer, 400f).height;	
			
//			Gdx.app.log("", "" +topHeight);
			
			if (topWidth < 300f) topWidth = 300f;
			if (topHeight < 100f) topHeight = 100f;
			
			NinePatch.drawBoxes(true, batcher, 1,  topWidth + 50f, topHeight + 50f, 0f, 0f, 0f);

			topHeight = Assets.font.getWrappedBounds(textPuffer, 400f).height;		// nochmal berechnen da text in y sonst falsch platziert
			Assets.font.drawWrapped(batcher, textPuffer, 200f, 240f + (topHeight / 2), 400f, HAlignment.CENTER);

			//---------------------------------------------------
			
			color.a = oldAlpha;
			color.r = oldRed;
			color.g = oldGreen;
			color.b = oldBlue;
			Assets.font.setColor(color.r, color.g, color.b, color.a);
			
			Assets.font.setScale(oldScaleX, oldScaleY);
		}
	}

	private void presentRunning () {
		
		ScreenControls.drawButtons(bubblr, batcher, true, musicOn, true, soundOn, false, true, false, false);  // check this out!! TODO: GC avoid getPrefs evry loop
		presentButtons();
	}

	private void presentPaused () {
		ScreenControls.drawButtons(bubblr, batcher, true, musicOn, true, soundOn, true, false, true, true);  // check this out!!
		presentMapname();
	}
//---------------------------------------------------------------------------------------------
	
	//=========================================================================
	
	
	//---- leben am unteren rand darstellen--- in allen statess
	private void presentLifes() {
		//---- anzahl leben player 1 mit icon unten einblenden ---
		for (int i = 0; i < level.world.player1.lifes; i++) {
			 
			batcher.draw(Assets.life_pl1, 2 + (18 * i), 480-83, 18, 18);
		}
		

	}
	
	
	private void presentData() {
	
		//--- save normal scale-values
		oldScaleX = Assets.font.getScaleX();
		oldScaleY = Assets.font.getScaleY();
		
		color = Assets.font.getColor();//get current Color, you can't modify directly  
		oldAlpha = color.a; //save its alpha
		oldRed = color.r;
		oldGreen = color.g; //save its green
		oldBlue = color.b; //save its blue
		
		
		Assets.font.setScale(oldScaleX / 1.2f);
		
		textPuffer.delete(0, textPuffer.length());
		textPuffer.append(level.world.player1.score);
		
		Assets.font.draw(batcher, scoreString, 5, 480 - 90);
		topWidth = Assets.font.getBounds(textPuffer).width;
		Assets.font.draw(batcher, textPuffer, 800 - 690 - topWidth, 480 - 115);

		//-------------------------------------------------------------------------
		
		Assets.font.setScale(oldScaleX / 1.6f);
		
		textPuffer.delete(0, textPuffer.length());
		textPuffer.append(bubblr.lang.getString("Kisses")).append(": ");

		Assets.font.draw(batcher, textPuffer, 5, 480 - 145);
//		Assets.font15_en.draw(batcher, textPuffer, 5, 480 - 145);
		
		textPuffer.delete(0, textPuffer.length());
		textPuffer.append(bubblr.lang.getString("Hattricks")).append('\n')
		.append("H2: ").append('\n')
		.append("H3: ").append('\n')
		.append("H4: ").append('\n')
		.append("H5: ");
		
		Assets.font.drawMultiLine(batcher, textPuffer, 5, 480 - 175);
		
		Assets.font.setScale(oldScaleX, oldScaleY);
		
	}
	

	
	//---- fire und jump button in allen statess
	private void presentButtons() {

		if (level.world.player1.multiTouchCommands[0] == Character.FIRE || level.world.player1.multiTouchCommands[1] == Character.FIRE) { //Character.FIRE) {
			batcher.draw(Assets.j2, 10, 110, 95, 95);
		}
		else {
			batcher.draw(Assets.fire1, 10, 110, 95, 95);
		}
		
		//-- alle 3 jump butons zeichnen ---
		batcher.draw(Assets.j1, 800-56-48, 224, 95, 95);
		batcher.draw(Assets.u1, 800-56-48, 95, 95, 68);
		
		//--- dann je einen �berzeichnen mit effekt --
		batcher.draw(Assets.u1, 800-56-48, 95, 95, 68);
	}
	
	
	//--------------------------------------------------------------
	
	
	
	private void presentMapname() {
	
		
		//------- draw mapname (only when not empty) --------------------
		if (level.mapName != "") {		
						
			color = Assets.font.getColor();//get current Color, you can't modify directly  
			oldAlpha = color.a; //save its alpha
			oldRed = color.r;
			oldGreen = color.g; //save its green
			oldBlue = color.b; //save its blue
			
			//--- save normal scale-values
			oldScaleX = Assets.font.getScaleX();
			oldScaleY = Assets.font.getScaleY();
			Assets.font.setScale(oldScaleX / 1.2f);	 //1.5
			//--------------------------------------------------------
			
			
			color.r = 0f;
			color.g = 0f;
			color.b = 0f;
			color.a = 1.0f;
			Assets.font.setColor(color.r, color.g, color.b, color.a);
			Assets.font.setScale(oldScaleX / 1.5f);
			
			textPuffer.delete(0, textPuffer.length());
			textPuffer.append(level.mapName);
			
			topWidth = Assets.font.getBounds(textPuffer).width;
			topHeight = Assets.font.getWrappedBounds(textPuffer, 400f).height;
			
			//---- draw white background for levelname -------------------------			
			NinePatch.drawBoxes(true, batcher, 1, topWidth + 50f, 40f, 0f, 0f, -150f);
			Assets.font.draw(batcher, textPuffer, (800f/2) - (topWidth/2), 480f - 83f);
			
			//---------------------------------------------------
			
			color.a = oldAlpha;
			color.r = oldRed;
			color.g = oldGreen;
			color.b = oldBlue;
			Assets.font.setColor(color.r, color.g, color.b, color.a);
			
			Assets.font.setScale(oldScaleX, oldScaleY);
		}
	}
	
	

//---------------------------------------------------------------
	
	
	@Override
	public void pause () {
		
		//---------------------------------------
		
		super.pause();	//-------- important to pause music ---------------
		
		//--------------------------------------
		
		if (state == GAME_RUNNING) {
			

			// zum messen der zeitdifferenz, wenn spiel pausiert wird (f�r bubbles und lifetimes etc)
			if (timeDiff_done == false) {
				timeDiff_start = System.nanoTime();
				timeDiff_done = true;
			}
			timeDiff = (System.nanoTime() - timeDiff_start);
			renderer.timeDiff = timeDiff;
			
			
			
			state = GAME_PAUSED;
		}
		
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

  
}

