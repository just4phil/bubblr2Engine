package de.philweb.bubblr.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.moribitotech.mtx.ButtonGame;
import com.moribitotech.mtx.MenuCreator;
import com.swarmconnect.Swarm;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Bubblr;
import de.philweb.bubblr.OverlapTester;
import de.philweb.bubblr.Welt;


public class CoolMainMenu extends AbstractScreen {
	
	Table table0;
	Table table;
	Table table2;
	Table tableQUIT;
	
	String text;
	TextureRegion icon;
	float textLength;
	float buttonLength;
	float iconLength;
	
	public int screenMethodID;
	public Screen newScreen;
	public AbstractScreen newScene2dScreen;
	public String screenName;
	
	Rectangle soundBounds;
	Rectangle musicBounds;
	Rectangle quitBounds;
	String url; 
	
	boolean backKeyPressed;	
	
	
	
	public CoolMainMenu(Bubblr bubblr, String screenName) {
		super(bubblr, screenName);
		

		soundBounds = new Rectangle(50, 416, 60, 64);
		musicBounds = new Rectangle(0, 480 - 64, 60, 64);
		quitBounds = new Rectangle(714, 32, 64, 64);    

		//-------------------------------
		
		setUpMainMenu();
		
		//-------------------------------
		
		
		backKeyPressed = false;	// sollte helfen, dass nach back auf helpscreen der mainmenuscreen nicht auch gleich die app beendet... klappt aber nicht
		
		
	}

	
	
	@Override
	public void setUpScreenElements() {
		super.setUpScreenElements();

		setBackgroundTexture(Assets.backgroundRegion);
		setBackButtonActive(true);
		
	}
	
	
	private void setUpMainMenu() {

		
		buttonLength = Assets.menuButton1.getRegionWidth();
		
		
		// ##### table ##########################################################################
		table0 = MenuCreator.createTable(true, Assets.getSkin());
		table = MenuCreator.createTable(true, Assets.getSkin());
		table2 = MenuCreator.createTable(true, Assets.getSkin());
		tableQUIT = MenuCreator.createTable(true, Assets.getSkin());
		
		
		
		// ##### table quit ##########################################################################
//		tableQUIT.debug();
		tableQUIT.setPosition(346, -176);
		
		tableQUIT.row();
		getStage().addActor(tableQUIT);
		
		
		tableQUIT.row();
		
		
		ButtonGame btnExit = MenuCreator.createCustomGameButton(Assets.quit, Assets.quit);
		btnExit.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				backKeyPressed = true;		// back-key: zur�ck
				return;
			}});
		
		tableQUIT.add(btnExit).size(64, 64).uniform().pad(2);
		
		//------------------------------------------------
		
		table0.setPosition(0, 630);
		table0.addAction(Actions.sequence(		//moveTo(0, 160, 0.5f));
		
			Actions.moveTo(0, 200, 0.5f), 		// 430px / 0.5 		= 860 px/s
			Actions.moveTo(0, 190, 0.01515f),  	// 10 px / 0.01515 	= 660 px/s
			Actions.moveTo(0, 180, 0.02173f), 	// 10 px / 0.02173 	= 460 px/s
			Actions.moveTo(0, 170, 0.03846f), 	// 10 px / 0.03846 	= 260 px/s
			Actions.moveTo(0, 165, 0.05f), 		// 5 px / 0.05 		= 100 px/s
			Actions.moveTo(0, 160, 0.166666f)));// 5 px / 0.166666 	= 30 px/s
		
		table0.row();
		getStage().addActor(table0);
		
 
		
		text = getGame().lang.getString("play");
		icon = Assets.icon_play;
		textLength = Assets.font.getBounds(text).width;
		iconLength = icon.getRegionWidth();
		ButtonGame btnPlay = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
		btnPlay.setText(Assets.font, text, true);
		btnPlay.setTextPosXY((640 / 2) - (textLength / 2), 45);	
		btnPlay.addListener(new ActorGestureListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				getGame().changeScene2DScreenToScreen(new GameScreen(getGame(), 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, false));
				
				return;					
						
			}});
		
		
		table0.add(btnPlay).size(640, 70).uniform().pad(5);
		
		
		table0.row();
		
		
		
				// ##### table ##########################################################################
//				table = MenuCreator.createTable(true, Assets.getSkin());
//				table.debug();
				table.setPosition(-getStage().getWidth() - 50, -50);
//				table.addAction(Actions.moveTo(-180, -50, 0.5f));
				table.addAction(Actions.sequence(
						Actions.moveTo(-220, -50, 0.5f), 		// 430px / 0.5 		= 860 px/s
						Actions.moveTo(-210, -50, 0.01515f),  	// 10 px / 0,01515 	= 660 px/s
						Actions.moveTo(-200, -50, 0.02173f), 	// 10 px / 0.02173 	= 460 px/s
						Actions.moveTo(-190, -50, 0.03846f), 	// 10 px / 0.03846 	= 260 px/s
						Actions.moveTo(-185, -50, 0.05f), 		// 5  px / 0.05 	= 100 px/s
						Actions.moveTo(-180, -50, 0.16666f)));	// 5  px / 0.16666 	= 30 px/s
				table.row();
				getStage().addActor(table);
							
						
				
				text = getGame().lang.getString("scores");
				icon = Assets.icon_scores;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnScores = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnScores.setText(Assets.font, text, true);
				btnScores.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);
				btnScores.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						

						
					}});
				table.add(btnScores).size(280, 70).uniform().pad(5);		
				
				
				table.row();
				
				
				text = getGame().lang.getString("help");
				icon = Assets.icon_help;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnHelp = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnHelp.setText(Assets.font, text, true);
				btnHelp.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);
				btnHelp.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						

					}});
				table.add(btnHelp).size(280, 70).uniform().pad(5);
				
				
				table.row();
				
				
				text = getGame().lang.getString("credits");
				icon = Assets.icon_credits;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnCredits = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnCredits.setText(Assets.font, text, true);
				btnCredits.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);
				btnCredits.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						

						
					}});
				table.add(btnCredits).size(280, 70).uniform().pad(5);
				
				
				table.row();
				
				
				text = getGame().lang.getString("GEIST");
				icon = Assets.icon_music;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnGeist = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnGeist.setText(Assets.font, text, true);
				btnGeist.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);	
				btnGeist.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						
					
					}});
				table.add(btnGeist).size(280, 70).uniform().pad(5);
				
								
				//----------------------------------------------------
				
				// ##### table ##########################################################################
//				table2 = MenuCreator.createTable(true, Assets.getSkin());
//				table2.debug();
				table2.setPosition(getStage().getWidth() + 50, -50);
				table2.addAction(Actions.sequence(			//180, -50, 0.5f));
				
					Actions.moveTo(220, -50, 0.5f), 		// 430px / 0.5 		= 860 px/s
					Actions.moveTo(210, -50, 0.01515f),  	// 10 px / 0,01515 	= 660 px/s
					Actions.moveTo(200, -50, 0.02173f), 	// 10 px / 0.02173 	= 460 px/s
					Actions.moveTo(190, -50, 0.03846f), 	// 10 px / 0.03846 	= 260 px/s
					Actions.moveTo(185, -50, 0.05f), 		// 5  px / 0.05		= 100 px/s
					Actions.moveTo(180, -50, 0.16666f)));	// 5  px / 0.16666 	= 30 px/s
				
				table2.row();
				getStage().addActor(table2);
				
				
				table2.row();
				
				
				text = getGame().lang.getString("rank");
				icon = Assets.icon_rank;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnRank = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnRank.setText(Assets.font, text, true);
				btnRank.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);	
				btnRank.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						
						
					}});
				table2.add(btnRank).size(280, 70).uniform().pad(5);
				
				
				table2.row();

				
				text = getGame().lang.getString("hints");
				icon = Assets.icon_hints;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnHints = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnHints.setText(Assets.font, text, true);
				btnHints.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);	
				btnHints.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						
					}});
				table2.add(btnHints).size(280, 70).uniform().pad(5);
				
				
				table2.row();
					 
				
				text = getGame().lang.getString("contact");
				icon = Assets.icon_contact;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnContact = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnContact.setText(Assets.font, text, true);
				btnContact.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);	
				btnContact.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);

					}});
				table2.add(btnContact).size(280, 70).uniform().pad(5);
				
				
				table2.row();
				
				
				text = getGame().lang.getString("options");
				icon = Assets.icon_options;
				textLength = Assets.font.getBounds(text).width;
				iconLength = icon.getRegionWidth();
				ButtonGame btnOptions = MenuCreator.createCustomGameButton(Assets.menuButton1,Assets.menuButton1, icon, 12, 10);
				btnOptions.setText(Assets.font, text, true);
				btnOptions.setTextPosXY((buttonLength / 2) - (textLength / 2) + (iconLength / 2), 45);
				btnOptions.addListener(new ActorGestureListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);

						
					}});
				table2.add(btnOptions).size(280, 70).uniform().pad(5);
	}
	
		
	
	
	
	
	@Override
	public void keyBackPressed() {
		super.keyBackPressed();

	}
	
	
	
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		
		if (Gdx.input.isKeyPressed(Keys.BACK) == true) {
			
			backKeyPressed = true;		// back-key: zur�ck
			return;
		}
		
		
		


		if (backKeyPressed == true) {

			backKeyPressed = false;
			
			getGame().exit();

		}
		

		
	}
}

