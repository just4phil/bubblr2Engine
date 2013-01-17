package de.philweb.bubblr.screens;

//package com.moribitotech.mtx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.tools.BubblrPreferences;


public abstract class AbstractScreen implements Screen {

	private static final int SCREEN_W = 800;
	private static final int SCREEN_H = 480;
	
	//----------------------------------------------------------------
	
	
	private Bubblr bubblr;
	BubblrPreferences prefs;
	Vector3 touchPoint;
	int i;
	int len;
	
	//---------------------------------------------------------------
	
	// Initial
	private String screenName = "Untitled Screen";
	private final Stage stage;
	
	// Screen second counter (1 second tick)
	private float startTime = System.nanoTime();
	private float secondsTime = 0;
	
	// Animation timer (If any animation is used)
	private float stateTime = 0;
	
	// Custom back button
	private boolean isBackButtonActive = false;
	

	public AbstractScreen(Bubblr bubblr, String screenName) {
		
		super();
		
		this.bubblr = bubblr;
		this.screenName = screenName;
		
		prefs = bubblr.getPreferences();
		
		touchPoint = new Vector3();
		
		//
		stage = new Stage(SCREEN_W, SCREEN_H, false);
		stage.getCamera().position.set(SCREEN_W / 2 ,SCREEN_H / 2, 0);
		
		// Receive inputs from stage
		Gdx.input.setInputProcessor(stage);	

		setUpScreenElements();

	}
	
	
	/**
	 * Called from constructor. Good place set your views or all screen related initial elements such as
	 * background texture.
	 * */
	public void setUpScreenElements() {
		// TODO Set mandatory screen elements such as background textures, initial parameter settings
	}


	@Override
	public void render(float delta) {  
				
		//-------- update music ---------------
		
//		bubblr.musicManager.update();
		
		//--------------------------------------
		
		
		// Update screen clock (1 second tick) 
      // ############################################################
		if (System.nanoTime() - startTime >= 1000000000) {
			secondsTime++;
			startTime = System.nanoTime();
		}
		
		// Update animation times
		// ############################################################
		stateTime +=  delta;
		
	    // Snippet (Clear screen and give red color)
		// ############################################################
		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		

		stage.getCamera().update();
		
		stage.getSpriteBatch().setProjectionMatrix(stage.getCamera().combined);
		
		
		// Update stage/actors logic (update() method in previous games)
		// ############################################################
		stage.act(delta);
		
		
		// Render drawings (draw()/render() methods in previous games)
		// ############################################################
		stage.draw();
		
		
		
		// Custom back button
		// ############################################################
		if(isBackButtonActive){
			if(Gdx.input.isKeyPressed(Keys.BACK)){
				keyBackPressed();
			}
		}
	}
	
	/**
	 * Set stage background. Sets the image (Adds to stage as image)
	 * 
	 * @param backgroundTextureRegion
	 * 
	 * */
	public void setBackgroundTexture(TextureRegion textureBackground){
		Drawable tBg = new TextureRegionDrawable(textureBackground);
		Image imgbg = new Image( tBg, Scaling.stretch );
		imgbg.setFillParent( true );
		stage.addActor(imgbg);
//		Gdx.app.log("ScreenLog", "SCREEN BACKGROUND SETTED: " + screenName);
	}
	
	/**
	 * Set the back button active for the screen. Sets  "Gdx.input.setCatchBackKey(true)" and override
	 * the method "keyBackPressed" to add desired functionality to back button
	 * 
	 * @param isBackButtonActive to use or not to use the back button
	 * @see keyBackPressed
	 * 
	 * */
	public void setBackButtonActive(boolean isBackButtonActive){
      Gdx.input.setCatchBackKey(true);
      this.isBackButtonActive = isBackButtonActive;
//      Gdx.app.log("ScreenLog", "CUSTOM BACK BUTTON SETTED");
	}
	
	/**
	 * Override this method to do some function when back button pressed
	 * */
	public void keyBackPressed(){}

	/**
	 * Get the game class
	 * */
	public Bubblr getGame() {
		return bubblr;
	}

	/**
	 * Set the game class
	 * */
	public void setGame(Bubblr bubblr) {
		this.bubblr = bubblr;
	}

	/**
	 * Get screen name
	 * */
	public String getScreenName() {
		return screenName;
	}


	/**
	 * Set screen name
	 * */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}


	/**
	 * Get screen name
	 * */
	public float getStartTime() {
		return startTime;
	}


	/**
	 * Get seconds since this screen constructed (EX: 3345 seconds)
	 * */
	public float getSecondsTime() {
		return secondsTime;
	}

	/**
	 * Set or reset sceconds
	 * */
	public void setSecondsTime(float secondsTime) {
		this.secondsTime = secondsTime;
	}

	/**
	 * Get delta added state time (Generally used for animations)
	 * */
	public float getStateTime() {
		return stateTime;
	}

	/**
	 * Set state time
	 * */
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	/**
	 * Get if back button active
	 * */
	public boolean isBackButtonActive() {
		return isBackButtonActive;
	}

	/**
	 * Get stage of the screen
	 * */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Get screen time from start in format of HH:MM:SS. It is calculated from "secondsTime" parameter, reset that
	 * to get resetted time.
	 * */
	public String getScreenTime() {
		int seconds = (int)(secondsTime % 60);
		int minutes = (int)((secondsTime/60) % 60);
		int hours = (int)((secondsTime/3600) % 24);
		String secondsStr = (seconds<10 ? "0" : "")+ seconds;
		String minutesStr = (minutes<10 ? "0" : "")+ minutes;
		String hoursStr = (hours<10 ? "0" : "")+ hours;
		return new String(hoursStr + ":" + minutesStr + ":" + secondsStr);
	}

	@Override
	public void resize(int width, int height) {
//		 Gdx.app.log("ScreenLog", "SCREEN RESIZE: " + screenName);
	}

	@Override
	public void show() {
//		 Gdx.app.log("ScreenLog", "SCREEN SHOW: " + screenName);
	}

	@Override
	public void hide() {
//		 Gdx.app.log("ScreenLog", "SCREEN HIDE: " + screenName);
	}

	@Override
	public void pause() {
//		 Gdx.app.log("ScreenLog", "SCREEN PAUSE: " + screenName);
		 
			//-------- pause music ---------------
			
//			bubblr.musicManager.pause();
			
			//--------------------------------------
	}

	@Override
	public void resume() {
		 Gdx.app.log("ScreenLog", "SCREEN RESUME: " + screenName);	 
	}

	@Override
	public void dispose() {
		 Gdx.app.log("ScreenLog", "SCREEN DISPOSE: " + screenName);
		 
		 // Dispose stage
//		 Gdx.app.log("ScreenLog", "SCREEN DISPOSING THESE: stage");
		 stage.dispose();
	}	
	
}
