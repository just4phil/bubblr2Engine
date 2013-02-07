package de.philweb.bubblr;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;



public class Character extends DynamicGameObject {
	
	public int state;
	public boolean state_Fire;
	public float stateTime;
	public int State_Animation;
	public boolean facingRight;
	public static final int IDLE = 0;
	public static final int JUMP = 1;
	public static final int FALL = 2;
	public static final int WALK_RIGHT = 3;
    public static final int WALK_LEFT = 4;
    public static final int FIRE = 5;
    public static final int HIT = 6;
    public static final int LOOK_LEFT = 7;
    public static final int LOOK_RIGHT = 8;
    public static final int IMMORTAL = 9;
    public static final int HALT = 10;
    public static final int WEAPON_DOWN = 11;
    public static final int KILLSWITCH = 12;
    public static final int SUICIDE = 13;
    public static final int FLY_UP = 14;
    public static final int FLY_DOWN = 15;
    
	
	public float JUMP_VELOCITY; 
	public float MOVE_VELOCITY;
	
	float calculatedVELOCITY;
	
	public static float width;	// gr��e in metern (60 pixel = 1 meter)
	public static float height;	// 32 px / 60 px/m = 0,5333 meter
	public static int fix_verkleinerung_x;	// pixel -- damit das fixture-rechteck etwas kleiner ist als das sprite
	public static int fix_verkleinerung_y;	// pixel -- damit das fixture-rechteck etwas kleiner ist als das sprite
	
	private World box2dwelt;
	public String characterName;
	public MassData masse = new MassData();
	
	protected static int nextId = 0;
	
	public boolean doRender = true;
	public boolean dead = false;
	

	public int moveCommand;

	
	public int multiTouchCommands[] = new int[2];

	public int firstCommand;
	public int secondCommand;
	
	public int flyCommand;
	public int lastMoveCommand;
	public int lastTouchCommand;
	public int lastFlyCommand;
	
	public int direction;
	boolean flip;
	public int last_direction;
	

	
	public boolean standingOnGround;
	public boolean standingOnPlattform;
	public boolean standingOnBarrier;
	public boolean standingOnBubble;
	public boolean mayJump;					// set to true if standing on ground or plattform etc
	
	
	float TouchCoord_Y;
	float jumpPower;
	float jumpPower_in;
	float vel_y;
	float vel_x;	
	boolean done;


	public long hattrickTime_start;
	public int hattrickCounter;
	boolean isJumping;
	public boolean gotHattrick;
	public int monsterKissesForExtraLifeCounter;	//--- helpercounter f�r extralifes
	
	
	Vector2 vektor;
	//----------------------------------------
	

	
	
	public Character (Bubblr bubblr, String characterName, String typ, float x, float y, float width, float height, float fix_verkleinerung_x, float fix_verkleinerung_y, boolean facingRight, World box2dwelt, float JUMP_VELOCITY, float MOVE_VELOCITY) {
		
		initialize_DynamicGameObject(bubblr, characterName, typ, x, y, width, height, fix_verkleinerung_x, fix_verkleinerung_y, true, box2dwelt);
		
		this.characterName = characterName;
		this.facingRight = facingRight;
		this.JUMP_VELOCITY = JUMP_VELOCITY;
		this.MOVE_VELOCITY = MOVE_VELOCITY;
		
		vektor = new Vector2();
		
		standingOnGround = false;
		standingOnPlattform = false;
		standingOnBarrier = false;
		standingOnBubble = false;
		mayJump = false;
		
		state = IDLE;
		State_Animation = IDLE;
		stateTime = 0;
		last_direction = 1;		// alle character schauen im spriteatlas nach rechts!														//TODO: CHECK PLAYER SPEED AM BODEN!!
		hattrickCounter = 0;
		isJumping = false;
		

		gotHattrick = false;
		monsterKissesForExtraLifeCounter = 0;	//--- helpercounter f�r extralifes
		

		this.box2dwelt = box2dwelt;
		
	}
//====================================================================================
	


	public String getCharacterName() {
		return characterName;
	}
	

	
	
	public void update (float deltaTime, OrthographicCamera guiCam, Welt world, boolean bounceOnWalls, float alpha) {
		

		
		super.update(body, bounceOnWalls, alpha);
		
		
		state_Fire = false;		//-- state fire deaktivieren, da sonst immer anim_fire aktiv (weil h�chste priorit�t)
		
			
		vel_y = body.getLinearVelocity().y;
		vel_x = body.getLinearVelocity().x;

		

		//------------------------------------------------------------------------
		
		


		

		
	    stateTime += deltaTime;
	}
	

     
	
	
	public int getAnim() {
		return State_Animation;
	}
	
	
	
	
	
	
	
}
      
      


