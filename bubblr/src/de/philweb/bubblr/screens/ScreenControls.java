package de.philweb.bubblr.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Game;

public class ScreenControls {



	public static void drawButtons(Game game, SpriteBatch batcher, boolean displayMusic, boolean musicOn, boolean displaySound, boolean soundOn, boolean displayPlay, boolean displayPause, boolean displayQuit, boolean displayRestart)  {
		

		if (displayMusic == true) batcher.draw(musicOn ? Assets.musicOn : Assets.musicOff, 0, 480 - 64, 64, 64);
		if (displaySound == true) batcher.draw(soundOn ? Assets.soundOn : Assets.soundOff, 0 + 55, 480 - 64, 64, 64);
		
		if (displayPlay == true) batcher.draw(Assets.play, 					714, 480 - 64, 64, 64);
		if (displayPause == true) batcher.draw(Assets.pause, 				714, 480 - 64, 64, 64);
		if (displayQuit == true) batcher.draw(Assets.quit, 					714, 32, 64, 64);
		
		if (displayRestart == true) batcher.draw(Assets.control_restart, 	714 - 75, 480 - 64, 64, 64);
	}


	
	public static void drawBackground(SpriteBatch batcher)  {
		
		batcher.draw(Assets.backgroundRegion, 0, 0, 800, 480);
	}
	

}