package de.philweb.bubblr.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.OverlapTester;
import de.philweb.bubblr.tools.NinePatch;

public class OptionsScreen extends Screen {
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle soundBounds;
	Rectangle musicBounds;
	Rectangle quitBounds;
	
	Rectangle pointsBounds;
	Rectangle particleBounds;
	Rectangle vibrationBounds;
	Rectangle lightsBounds;
	Rectangle restoreBounds;
	Rectangle backgroundBounds;
	Rectangle rudemodeBounds;
	Rectangle soundFXBounds;
	Rectangle musicFXBounds;
	
	Rectangle adminModeBounds;
	
	Vector3 touchPoint;
	
	String pointsText;
	String particlesText;
	String lightsText;
	String backgroundText;
	String vibrationText;
	String rudemodeText;
	String soundsText;
	String musicText;
	String restoreText;

	float pointsTextWidth;
	float particlesTextWidth;
	float lightsTextWidth;
	float backgroundTextWidth;
	float rudemodeTextWidth;
	float vibrationTextWidth;
	float soundsTextWidth;
	float musicTextWidth;
	float restoreTextWidth;

	float switchOffset;
	float textboxWidth;
	float offsetX;
	float textOffsetX;
	float row1X;
	float row2X;
	
	float scoreWidth;
	float oldScaleX;
	float oldScaleY;
	Color color;
	float oldRed;
	float oldGreen;
	float oldBlue;
	float oldAlpha;
	
	float startTextbox_x;
	float startTextbox_y;
	
	boolean backKeyPressed;	
	float offsetY;
	
	//========= languages ================================
	public static final ArrayList<String> languages = new ArrayList<String>();
	int languageCounter = 0; 
	
	
	public OptionsScreen (Bubblr bubblr) {
		super(bubblr);
		guiCam = new OrthographicCamera(800, 480);
		guiCam.position.set(800 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		soundBounds = new Rectangle(50, 416, 60, 64);
		musicBounds = new Rectangle(0, 480 - 64, 60, 64);

		offsetY = 0f;
		
		
		//========= languages ==== add new languages here !!! ========================
		languages.add("en_UK");
		languages.add("de_DE");
		languages.add("pl_PL");
		languages.add("es_ES");
//		languages.add("fr_FR");
		
		
		//---- TODO: generate freeType font and register in assetmanager ?
		
		
		

		languageCounter = languages.indexOf(bubblr.lang.getLanguage());
		//----------------------------------------------------


		
		pointsBounds = new Rectangle(80, 370 - offsetY, 320, 80); 
		particleBounds = new Rectangle(401, 370 - offsetY, 320, 80);
		
		lightsBounds = new Rectangle(80, 283 - offsetY, 320, 80);
		backgroundBounds = new Rectangle(80, 30 - offsetY, 320, 80);
		rudemodeBounds = new Rectangle(80, 198 - offsetY, 320, 80); 
		musicFXBounds = new Rectangle(80, 113 - offsetY, 320, 80); 
		soundFXBounds = new Rectangle(401, 113 - offsetY, 320, 80);
		vibrationBounds = new Rectangle(401, 283 - offsetY, 320, 80);
		restoreBounds = new Rectangle(401, 30 - offsetY, 320, 80);
		quitBounds = new Rectangle(714, 32, 64, 64); 
		adminModeBounds = new Rectangle(750, 450, 50, 30);
		
		touchPoint = new Vector3();
		   

		
		Gdx.input.setCatchBackKey(true);	// back key abfangen
		backKeyPressed = false;	
	
	}

	
	@Override
	public void update (float deltaTime) {

		
		if (Gdx.input.isKeyPressed(Keys.BACK) == true) {
			
			backKeyPressed = true;		// back-key: zur�ck
			return;
		}
		
		
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			
			//---- change language -----------
			if (OverlapTester.pointInRectangle(backgroundBounds, touchPoint.x, touchPoint.y)) {
				
//				Gdx.app.log("touched", "backgroundBounds");
				
				
				languageCounter++;
				if (languageCounter > languages.size()-1) languageCounter = 0;
				bubblr.lang.loadLanguage(languages.get(languageCounter));	// load
				bubblr.prefs.setLanguage(languages.get(languageCounter));	// save to prefs
				
    			if (bubblr.lang.getLanguage().equals("en_UK")) Assets.font = Assets.font10_en;
    			if (bubblr.lang.getLanguage().equals("de_DE")) Assets.font = Assets.font10_de;
    			if (bubblr.lang.getLanguage().equals("pl_PL")) Assets.font = Assets.font10_pl;
    			if (bubblr.lang.getLanguage().equals("es_ES")) Assets.font = Assets.font10_es; 
    			
    			
				Gdx.app.log("bubblr.lang.getLanguage()", "" + bubblr.lang.getLanguage());
				return;
			}
	
			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
				
				Gdx.app.log("touched", "quitBounds");
				
				backKeyPressed = true;
				return;
			}
		}
		
		
		if (backKeyPressed == true) {


			bubblr.changeScreenToScene2DScreen(new CoolMainMenu(bubblr, "MainMenu"));
			return; 
		}
		
	}

	
	
	
	
	@Override
	public void present (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.disableBlending();
		batcher.begin();
			ScreenControls.drawBackground(batcher);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();


		ScreenControls.drawButtons(bubblr, batcher, true, false, false, false, false, false, true, false);
		
		//--------------
		
	
		//--- save normal scale-values
		oldScaleX = Assets.font.getScaleX();
		oldScaleY = Assets.font.getScaleY();
		Assets.font.setScale(oldScaleX * 1f);	
		
		pointsText = bubblr.lang.getString("points");
		particlesText = bubblr.lang.getString("particles");
		lightsText = bubblr.lang.getString("lights");
		vibrationText = bubblr.lang.getString("vibration");
		rudemodeText = bubblr.lang.getString("rude mode");
		soundsText = bubblr.lang.getString("sounds");
		restoreText = bubblr.lang.getString("restore data");
		musicText = bubblr.lang.getString("music");
		backgroundText = bubblr.lang.getString("language");
		
		
		pointsTextWidth = Assets.font.getBounds(pointsText).width;
		particlesTextWidth = Assets.font.getBounds(particlesText).width;
		vibrationTextWidth = Assets.font.getBounds(vibrationText).width;
		lightsTextWidth = Assets.font.getBounds(lightsText).width;
		backgroundTextWidth = Assets.font.getBounds(backgroundText).width;
		soundsTextWidth = Assets.font.getBounds(soundsText).width;
		musicTextWidth = Assets.font.getBounds(musicText).width;
		restoreTextWidth = Assets.font.getBounds(restoreText).width;
		rudemodeTextWidth = Assets.font.getBounds(rudemodeText).width;
		
		//---- save original font color for later resetting
		color = Assets.font.getColor();//get current Color, you can't modify directly  
		oldAlpha = color.a; //save its alpha
		oldRed = color.r;
		oldGreen = color.g; //save its green
		oldBlue = color.b; //save its blue
		
		//--- set color for text
		color.r = 1f;
		color.g = 1f;
		color.b = 1f;
		color.a = 1.0f;
		Assets.font.setColor(color.r, color.g, color.b, color.a);
		
		
		
		//--- draw text boxes
		textboxWidth = 300f;
		textOffsetX = 30f;
		offsetX = 160f;
		row1X = (800/2) - offsetX - textOffsetX;
		row2X = (800/2) + offsetX - textOffsetX;
		switchOffset = 120;
		
		NinePatch.drawBoxes(false, batcher, 5, textboxWidth, 70f, 15f, offsetX, offsetY);

		
		//--- draw text -----		
		Assets.font.drawMultiLine(batcher, pointsText, row1X - pointsTextWidth / 2, 420 - offsetY);
			batcher.draw(Assets.optionsOff, row1X + switchOffset, 388 - offsetY);
		
		
		Assets.font.draw(batcher, lightsText, row1X - lightsTextWidth / 2, 335 - offsetY);
			batcher.draw(Assets.optionsOff, row1X + switchOffset, 302 - offsetY);
		
		
		
		Assets.font.draw(batcher, rudemodeText, row1X - rudemodeTextWidth / 2, 250 - offsetY);
			batcher.draw(Assets.optionsOff, row1X + switchOffset, 219 - offsetY);

		
		Assets.font.draw(batcher, musicText, row1X - musicTextWidth / 2, 165 - offsetY);
			batcher.draw(Assets.optionsOff, row1X + switchOffset, 133 - offsetY);
		
		
		Assets.font.draw(batcher, backgroundText, row1X + textOffsetX - backgroundTextWidth / 2, 80 - offsetY);

		
		
		//===================================================================================================
		//--- draw text boxes coloumn 2
		NinePatch.drawBoxes(false, batcher, 5, textboxWidth, 70f, 15f, -offsetX, offsetY);
		
		//--- draw text -----		
		Assets.font.draw(batcher, particlesText, row2X - particlesTextWidth / 2, 420 - offsetY);				// 420
			batcher.draw(Assets.optionsOff, row2X + switchOffset, 388 - offsetY);
		
			
		Assets.font.draw(batcher, vibrationText, row2X - vibrationTextWidth / 2, 335 - offsetY);				// 335
			batcher.draw(Assets.optionsOff, row2X + switchOffset, 302 - offsetY);
		
		
		Assets.font.draw(batcher, soundsText, row2X - soundsTextWidth / 2, 165 - offsetY);
			batcher.draw(Assets.optionsOff, row2X + switchOffset, 133 - offsetY);
	
		
		Assets.font.draw(batcher, restoreText, row2X + textOffsetX - restoreTextWidth / 2, 80 - offsetY);		// 80
		
		//-------------
		
		//--- reset colors 
		color.a = oldAlpha;
		color.r = oldRed;
		color.g = oldGreen;
		color.b = oldBlue;
		Assets.font.setColor(color.r, color.g, color.b, color.a);
		
		//--- reset font scale 
		Assets.font.setScale(oldScaleX, oldScaleY);
				
		//-------------
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

