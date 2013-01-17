package de.philweb.bubblr;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Level {

	public Welt world;
	public World box2dwelt;
	public static final float box2dWelt_gravitation = -10.0f;
	public String mapName;	
	public int levelID;
	public long levelMaxTime;
	public long levelTime_start;
	public boolean started = false;
	public String mapToast;						// should give a hint for the specialCollectable
		
	public Bubblr bubblr;
	


	public Level(Bubblr bubblr, float game_monsterSpeed) {	
		
		this.bubblr = bubblr;

		
		box2dwelt = new World(new Vector2(0.0f, box2dWelt_gravitation), true);

		
//		box2dwelt.setAutoClearForces(false);	//--- do clearforces manually f�r substepping in gamescreen (fixed timestep) 	// TODO
		
		world = new Welt(bubblr, box2dwelt, game_monsterSpeed);

		world.accum = 0.0f; // for fixed timestep physics
	}
	
	
	
	//--------------- setters & getters ------------------------------------------
	

	public Welt getWorld() {
	
		return world;
	}

	
	public Bubblr getBubblr() {
		
		return bubblr;
	}
	
	
}
