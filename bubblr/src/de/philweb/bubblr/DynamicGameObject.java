package de.philweb.bubblr;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class DynamicGameObject extends GameObject {

	public float MAX_VELOCITY_y = -4.5f;
	public float width;
	public float height;
	boolean coming_from_above = false;
	float vel;
	Vector2 vektor;
	
	//---- for fixed-timestep physics interpolation ---------
//	public Vector2 lastPosition;
//	public Vector2 actualPosition;
//	public Vector2 lastVelocity;
//	public Vector2 actualVelocity;
//	public Vector2 interpolatedPosition;
//	public Vector2 interpolatedVelocity;
	
//	float alpha;
	//-----------------------------------------------
	
	
	public DynamicGameObject() {
		
//		lastPosition = new Vector2(0f, 0f);
//		actualPosition = new Vector2(0f, 0f);
//		lastVelocity = new Vector2(0f, 0f);
//		actualVelocity = new Vector2(0f, 0f);
//		interpolatedPosition = new Vector2(0f, 0f);
//		interpolatedVelocity = new Vector2(0f, 0f);
	}
	
	
	public void initialize_DynamicGameObject (String characterName, String typ, float x, float y, float width, float height, float fix_verkleinerung_x, float fix_verkleinerung_y, boolean fixedRotation, World box2dwelt) {
		
		this.width = width;
		this.height = height;
		
		initialize_GameObject(characterName, typ, x, y, width, height, fix_verkleinerung_x, fix_verkleinerung_y, fixedRotation, box2dwelt);
		
		
	}
	
	
	public void update (Body body, boolean bounceOnWalls, float alpha) {

		//---- for fixed-timestep physics interpolation --------------
//		lastPosition.set(actualPosition);
//		actualPosition.set(body.getPosition().x, body.getPosition().y);
//		lastVelocity.set(actualVelocity);
//		actualVelocity.set(body.getLinearVelocity().x, body.getLinearVelocity().y);
//		
//		interpolatedPosition.set(actualPosition.x * alpha + lastPosition.x * (1.0f - alpha ), actualPosition.y * alpha + lastPosition.y * (1.0f - alpha ));
		//------------------
		
        
	}
	
	
}
