package de.philweb.bubblr;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;

public class character_goodGuy extends Character {

	int touchState;
	public float startpoint_x;	// um nach dem tod zum ausgangspunkt zur�ck zu kehren
	public float startpoint_y;	// um nach dem tod zum ausgangspunkt zur�ck zu kehren
	
	public int lifes;
	public long score;
		
	public static final float width = 44f; //32f;	// pixel
	public static final float height = 50f; //32f;	// pixel
	public static final int fix_verkleinerung_x = 36; //20;	// pixel -- damit das fixture-rechteck etwas kleiner ist als das sprite
	public static final int fix_verkleinerung_y = 27; //20;	// pixel -- damit das fixture-rechteck etwas kleiner ist als das sprite
	
	public static final float RENDERVERSATZ_X = 0.28f; 	// versatz beim rendering
	public static final float RENDERVERSATZ_Y = - 0.55f; 	// versatz beim rendering
	
	
	public float stateTime;
	public int State_Animation;
	
	public static final float angularDamping = 1f;
	public static final float rumwirbelImpuls = 1.0f; //1.5f;
	public static final float maxAngularVelocity = 30f;
	
	Random rand;
	float randomFloat;
	int richtungsFaktor;	// zum bestimmen wohin eine abzulegende waffe gelegt wird
	
	long now;
	
	
	Welt world;
	
	
	
    
	public character_goodGuy(Bubblr bubblr, String characterName, String typ, float x, float y, float JUMP_VELOCITY, float MOVE_VELOCITY, boolean facingRight, World box2dwelt, Welt world) {
		
		super(bubblr, characterName, typ, x, y, width, height, fix_verkleinerung_x, fix_verkleinerung_y, facingRight, box2dwelt, JUMP_VELOCITY, MOVE_VELOCITY);
		
		this.facingRight = facingRight;
		this.world = world;
		
		lifes = Welt.PLAYERLIFES;
		score = 0;
		
		startpoint_x = x;
		startpoint_y = y;
	

	}



	
	public void update (float deltaTime, long timeDiff, OrthographicCamera guiCam, Welt world, float alpha) {
	

		
		super.update(deltaTime, guiCam, world, false, alpha);
				

		
		
		State_Animation = getAnim();
		
		stateTime += deltaTime;

	}

	
	

	
}


