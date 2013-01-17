package de.philweb.bubblr;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

import de.philweb.bubblr.tools.BubblrPreferences;
import de.philweb.bubblr.tools.LanguageManager;

public class Welt {
	
	protected Game game;
	public World box2dwelt;
	ContactListener ContactListener;
	
	
	 
//	//============= Admin-Functions ============================================================
	public boolean adminMode;	// wird gesetzt in Bubblr.java!! (hier nur temp. gespeichert)
	  

	//===== reduce FPS =======================
	public static final long RENDERER_SLEEP_MS = 0; // 34 -> 30 fps, 30 -> 34 fps, 22 gives ~46 FPS, 20 = 100, 10 = 50
	
	//================= ... ====================
	public static final float WORLD_WIDTH = 15; // 10;
	public static final float WORLD_HEIGHT = 10; // 15 * 20;
	
	public static final float mapgrenze_rechts = 8.79f;	// in metern (60 px pro meter)
	public static final float mapgrenze_links = 0.85f;
	public static final float mapgrenze_oben = 8.0f;
	public static final float mapgrenze_unten = 0.0f;
	
	public static final int OFFSET_X_MAP_SCREEN_PX = 115;		// to display mapName in renderer (was formerly in gamescreen)
	public static final int OFFSET_Y_MAP_SCREEN_PX = 6;			// to display mapName in renderer (was formerly in gamescreen)
	

	
	//====== for fixed-timestep box2d simulation =========
	public float accum = 0f;
	public static final float FIXEDTIMESTEP = 1f / 60f;
	public static final float MAXTIMESTEP = 1f / 20f; // keep from spiraling to death
	public static final int MAXIMUM_NUMBER_OF_STEPS = 25;
	public static final float MINIMUM_TIMESTEP = 1.0f / 600.0f;  
	public static final int VELOCITY_ITERATIONS = 10; //6;	// 3
	public static final int POSITION_ITERATIONS = 8; //6;	// 3
	public float alpha;
	

	//================= DEFAULTS ====================
	public static final int PLAYERLIFES = 5;
	public static final int NUMBEROFLEVELS_FULL = 20;		// TODO: anpassen
	public static final int NUMBEROFLEVELS_DEMO = 20;		// TODO: anpassen	
	public static final float PLAYER_JUMP_VELOCITY = 5.7f;
	public static final float PLAYER_MOVE_VELOCITY = 4; // 3.5f; 
	public static final int MAPTIMELIMIT = 45;
	public static final String mapToast = "";


	//================= Points ====================
	public static final int POINTS4bubble = 			2;
	public static final int MONSTERPOINTS4KILL = 		5; 	//100;
	public static final int COLLECTABLEPOINTS = 		10;
	public static final int POINTS4strawberry = 		10;
	public static final int POINTS4banana =				20;
	public static final int POINTS4berry = 				30;
	public static final int POINTS4coin = 				40;
	public static final int POINTS4LEVELTIMEBONUS = 	50;	//--- bonus points +/- per second under/over MapMaxTime
	public static final int POINTS4diamond = 			100;
	public static final int POINTS4specialCollectable = 500;	// diamond
	public static final int POINTS4mine = 				0; // -500;
	


	//-----------------------------------------------------
	public static final short CATEGORY_BUBBLE = 0x0002; // 0000000000000010 in binary
	public static final short CATEGORY_COLLECTABLE = 0x0004; // 0000000000000100 in binary
 
	public static final short GROUP_MONSTER = -2;
	
	public static final short MASK_COLLECTABLE = ~CATEGORY_BUBBLE;
	public static final short MASK_BUBBLE = ~CATEGORY_COLLECTABLE;
	
	public static float PIXELS_PER_METER = 60.0f;

	
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);
	
	public static final String groundFixtureString = "groundFixture";
	

	public ArrayList<character_goodGuy> players = new ArrayList<character_goodGuy>();

	
	public character_goodGuy player1;

		
	public int mapTimeLimit;
	
	public float monsterSpeed;
	public int state;

	OrthographicCamera guiCam;

	float diff_x;
	float diff_y;

	Random rand;
	float randomFloat;
	int randomInt;
	float pos_x;	//--- f�r ermittlung der pos. der spawnpoints in presentCollectable
	float pos_y;	//--- f�r ermittlung der pos. der spawnpoints in presentCollectable
	
	boolean found;
	
	int i;
	int j;
	int len;
	int monster_count;
	character_goodGuy player;
	character_goodGuy collidedPlayer;
	
	Vector2 vektor2;
	
	
	BubblrPreferences prefs;
	LanguageManager lang; 

	
	//------ for character setup: monster re-sizing -----------
	float width;
	float height;
	
//----------------------------------------------------------------

	
	public Welt (Bubblr bubblr, final World box2dwelt, float monsterSpeed) {
	
		this.lang = bubblr.lang;
		this.prefs = bubblr.prefs;
		this.box2dwelt = box2dwelt;
		this.monsterSpeed = monsterSpeed;
		this.state = WORLD_STATE_RUNNING;
		
		adminMode = bubblr.adminMode;
		
		
		rand = new Random();
		vektor2 = new Vector2();
		

		
		
		//=============== cause a crash... FOR ACRA TESTING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		String sCrashString = null;
//		Gdx.app.log("MyApp", sCrashString.toString() );
		//===========================================================================================
		


		
		//---- ab hier f�r collisiondetection mit box2d ---------------------------------------------------------------
		
				ContactListener = new ContactListener() {

					Fixture fixA;
					Fixture fixB;
					
					Body bodyA;
					Body bodyB;
					Body getBody;				
					World box2dworld;

					character_goodGuy collidedPlayer;
					String collidedWith;
				
					
					
					@Override
					public void beginContact(Contact b2Contact) {	
					
						fixA = b2Contact.getFixtureA();
						fixB = b2Contact.getFixtureB();
						
						bodyA = fixA.getBody();
						bodyB = fixB.getBody();
						
					
						//------------ beginContact mit groundsensor pr�fen -------------------------
						if (fixA.getUserData() == "groundsensor" || fixB.getUserData() == "groundsensor") {
						
							
							//------------ beginContact mit Tilemap pr�fen -------------------------
							if (fixA.getUserData() == groundFixtureString || fixB.getUserData() == groundFixtureString) {
								
								if (fixA.getUserData() == groundFixtureString) collidedPlayer = getPlayer_for_Body(bodyB);
								if (fixB.getUserData() == groundFixtureString) collidedPlayer = getPlayer_for_Body(bodyA);
								
								collidedPlayer.standingOnGround = true;	

							}

						}

						
					}

					
					//==============================================================
					@Override
					public void endContact(Contact b2Contact) {
						
						fixA = b2Contact.getFixtureA();
						fixB = b2Contact.getFixtureB();
						
						bodyA = fixA.getBody();
						bodyB = fixB.getBody();
						
					
						//------------ endcontact mit groundsensor pr�fen -------------------------
						if (fixA.getUserData() == "groundsensor" || fixB.getUserData() == "groundsensor") {
						
							
							//------------ endcontact mit Tilemap pr�fen -------------------------
							if (fixA.getUserData() == groundFixtureString || fixB.getUserData() == groundFixtureString) {
								
								if (fixA.getUserData() == groundFixtureString) collidedPlayer = getPlayer_for_Body(bodyB);
								if (fixB.getUserData() == groundFixtureString) collidedPlayer = getPlayer_for_Body(bodyA);
								
								collidedPlayer.standingOnGround = false;

							}

						}	
	
					}
					//===========================================================================================
					
					
					@Override
					public void postSolve(Contact b2Contact, ContactImpulse arg1) {

					
					}

					@Override
					public void preSolve(Contact b2Contact, Manifold arg1) {

						fixA = b2Contact.getFixtureA();
						fixB = b2Contact.getFixtureB();
						
						bodyA = fixA.getBody();
						bodyB = fixB.getBody();
		
						
						box2dworld = bodyA.getWorld();
	
//	=====================================================================================================================
						
						//--- Kollisionen mit player 1 pr�fen ----------------
						if (fixA.getUserData() == "player1" || fixB.getUserData() == "player1") {
							
							
							if (fixA.getUserData() == "player1") collidedPlayer = getPlayer_for_Body(bodyA);	
							if (fixB.getUserData() == "player1") collidedPlayer = getPlayer_for_Body(bodyB);
							
							
							//---- player 1 gegen player 2 -------------------
							if (fixA.getUserData() == "player2" || fixB.getUserData() == "player2") {
								b2Contact.setEnabled(false);
							}
								
							
							//--- player 1 durch decken ------------------------
							if (fixA.getUserData() == groundFixtureString || fixB.getUserData() == groundFixtureString) {
								
								if (fixA.getUserData() == "player1") {
									
									if (bodyA.getPosition().y < 7f) {		// player darf nicht durch obere decke
										if (bodyA.getLinearVelocity().y > 0.2f) {
											b2Contact.setEnabled(false);
										}
									}
								}
								if (fixB.getUserData() == "player1") {
									
									if (bodyB.getPosition().y < 7f) {		// player darf nicht durch obere decke
							
										if (bodyB.getLinearVelocity().y > 0.2f) {
											b2Contact.setEnabled(false);
										}
									}
								}
							}
							
							
							
					}
					//-------------------------------------------------------------------
					
					
				}		
			};
					
				
			box2dwelt.setContactListener(ContactListener);		//------  world contact listener um collisionen ausserhalb von box2d bearbeiten zu k�nnen
				
	//------------------------------------------		
	
		
	}

	
	
//====================================================================================================
	
	
	
	public void characterSetup(ArrayList<Vector2> playerList, int mapTimeLimit) {

		this.mapTimeLimit = mapTimeLimit;
		
		final MassData masse = new MassData();
		masse.mass = 1;
		
		
					//--- setup Player
		player1 = new character_goodGuy("player1", "character", playerList.get(0).x + character_goodGuy.RENDERVERSATZ_X, playerList.get(0).y + character_goodGuy.RENDERVERSATZ_Y, PLAYER_JUMP_VELOCITY, PLAYER_MOVE_VELOCITY, true, box2dwelt, this);	// 4. parameter = blickrichtung
		player1.body.setMassData(masse);
		players.add(player1);
			
		
	}
	

	
	
//==============================================================================================
	
	public void update (float deltaTime, long timeDiff, float accelY, int touchState, OrthographicCamera guiCam, WorldRenderer renderer) {
		
		this.guiCam = guiCam;
		
//		alpha = accum / FIXEDTIMESTEP;	// for fixed-timestep-interpolation in dynamicGameObject
		
		
		updatePlayers(deltaTime, timeDiff, accelY, touchState, guiCam, this);
		
//		updateBox2d(deltaTime);	//---- physic-engine simulieren lassen ---- (im gamescreen) -------


	}
//==============================================================================================	
	
	
	
	public void updateBox2d(float deltaTime, int VELOCITY_ITERATIONS, int POSITION_ITERATIONS) {
		
//		box2dwelt.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);	
		box2dwelt.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS);		// The second and third argument specify the number of iterations of velocity and position tests to perform -- higher is more accurate but is also slower.
	}
	
	

	//========================================================================
	
	
	
	
	public void updatePlayers (float deltaTime, long timeDiff, float accelY, int touchState, OrthographicCamera guiCam, Welt world) {
		len = players.size();
		for (i = 0; i < len; i++) {
			player = players.get(i);
			
		
			
			//---- check if player may jump -----------
			player.mayJump = false;
			if (player.standingOnGround == true) player.mayJump = true;
			if (player.standingOnPlattform == true) player.mayJump = true;
			if (player.standingOnBarrier == true) player.mayJump = true;
//			if (player.standingOnBubble == true) player.mayJump = true;
			
			
			
			if (player.dead == false) player.update(deltaTime, timeDiff, guiCam, world, alpha);	// accelY, touchState, guiCam);
					
			
		}
	}
	


//========================================================================


	


//------ Helper Functions ---------------------------------------------------------
	
	
	//--- player zu einem body identifizieren (f�r kollision von monstern mit bubbles)
	
	public character_goodGuy getPlayer_for_Body(Body body) {
		
		found = false;
		collidedPlayer = null;
		
		for (int j = 0; j < players.size(); j++) {
			collidedPlayer = players.get(j);
			
			if (collidedPlayer.body.getPosition() == body.getPosition() && found == false) {
				
				found = true;
				break;
			}
		}
		return collidedPlayer;
	}
	
	
}



