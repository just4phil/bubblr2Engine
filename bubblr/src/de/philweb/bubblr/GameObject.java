package de.philweb.bubblr;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {

	Bubblr bubblr;
	
	public static final int IDdefault = 0;
	public static final int IDbubble = 1;
	public static final int IDmonster = 2;
	public static final int IDcollectable = 3;
	public static final int IDplayer = 4;
	public static final int IDbarriers = 5;
	public static final int IDplattform = 6;
	public static final int IDfireball = 7;
	public static final int IDcannonball = 8;

	
	public int ID;
	
	public float x;
	public float y;
	public float width;	// kommt immer in pixeln rein!
	public float height;	// kommt immer in pixeln rein!
	public Body body;
	public String objectName;
	public String typ;
	public float fix_verkleinerung_x;
	public float fix_verkleinerung_y;
	public Fixture fix;
	public float stateTime;
	public String assetName;
	public static final float friction = 1000.0f;
	public boolean eliminate;	// for barriers that get shooted away
	public boolean eliminated;	// for barriers that get shooted away

	Vector2 vektor;
	
	
	public void initialize_GameObject (Bubblr bubblr, String objectName, String typ, float x, float y, float width, float height, float fix_verkleinerung_x, float fix_verkleinerung_y, boolean fixedRotation, World box2dwelt) {

		this.bubblr = bubblr;
		
//		this.position = new Vector2(x, y);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.fix_verkleinerung_x = fix_verkleinerung_x;
		this.fix_verkleinerung_y = fix_verkleinerung_y;
//		this.bounds = new Rectangle(x - width / 2, y - height / 2, width, height);
		this.objectName = objectName;
		this.typ = typ;
		eliminate = false;	// for barriers that get shooted away
		eliminated = false;	// for barriers that get shooted away

		vektor = new Vector2();
		
		//--------- object-IDs vergeben ---------- (damit wird in dyn.gameobject �ber case abgefragt wie unten/oben/rechts/links behandelt werden soll
		ID = IDdefault;
		if (objectName.equals("bubble")) ID = IDbubble;
		if (objectName.equals("monster")) ID = IDmonster;
		if (objectName.equals("player1")) ID = IDplayer;
		if (objectName.equals("player2")) ID = IDplayer;
		if (objectName.equals("collectable")) ID = IDcollectable;
		if (objectName.equals("fireball")) ID = IDfireball;
		if (objectName.equals("cannonball")) ID = IDcannonball;
		if (objectName.equals("plattform")) ID = IDplattform;
		
			
			
		//---- bodies & fixtures erzeugen ---------------------
		if (typ == "none") {		//--- es gibt auch gameObjects ohne box2d-body/fixture (z.b. VisualsPoints, Spawnpoint) => irgendwie buggy -> crashed (z.b. wenn monster-spawnpoint "none" sind und dann ein collectable gespawnt werden soll! // TODO

		}
		else {
		
			
			//---------body-definitionen f�r box2d---------------------------------------
			
			BodyDef characterBodyDef = new BodyDef();
			FixtureDef characterFixtureDef = new FixtureDef();
			characterBodyDef.position.set(x, y);					// problematisch, da hier "none" objekte keine position bekommen!! -> crash
	
			
			if (typ == "static") {
				
				characterBodyDef.type = BodyDef.BodyType.StaticBody;
				body = box2dwelt.createBody(characterBodyDef);
				
				PolygonShape characterShape = new PolygonShape();
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x), bubblr.pixelsToMeter(height) - bubblr.pixelsToMeter(fix_verkleinerung_y));		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 1.0f;
				body.createFixture(characterFixtureDef);
				characterShape.dispose();	
				fix = body.getFixtureList().get(0);
				fix.setUserData(objectName);
				body.setFixedRotation(fixedRotation);
			}
			
			if (typ == "box") {
				characterBodyDef.type = BodyDef.BodyType.DynamicBody;
				body = box2dwelt.createBody(characterBodyDef);
				
				PolygonShape characterShape = new PolygonShape();
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x), bubblr.pixelsToMeter(height) - bubblr.pixelsToMeter(fix_verkleinerung_y));		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 1.0f;
				characterFixtureDef.friction = 0.0f; 
				
				if (objectName.equals("collectable")) {
					characterFixtureDef.filter.categoryBits = Welt.CATEGORY_COLLECTABLE;
					characterFixtureDef.filter.maskBits = Welt.MASK_COLLECTABLE;			// mask collectables and bubbles
				}
				
				body.createFixture(characterFixtureDef);
				characterShape.dispose();
				fix = body.getFixtureList().get(0);
				fix.setUserData(objectName);
				body.setFixedRotation(fixedRotation);
			}
			
			
			if (typ == "character") {
				characterBodyDef.type = BodyDef.BodyType.DynamicBody;
				body = box2dwelt.createBody(characterBodyDef);
				
				PolygonShape characterShape = new PolygonShape();
				
				//---normal fixture
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x), bubblr.pixelsToMeter(height) - bubblr.pixelsToMeter(fix_verkleinerung_y));		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 1.0f;
				characterFixtureDef.friction = 0.0f;
				body.createFixture(characterFixtureDef);
				fix = body.getFixtureList().get(0);
				fix.setUserData(objectName);
				
				//---- sensor for ground
				vektor.set(0.0f, -0.35f); //---offset to make fixture at feet for ground
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x + 0.9f), 0.1f, vektor, 0.0f);		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
																										//=====->>> ACHTUNG: diese 0.9f sind n�tig, damit groundsensor etwas schmaler ist als player und dann bei herabgleiten an mauern keine bodenkontakte ausl�st!!
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 0f;
				characterFixtureDef.friction = 0.0f; 
				characterFixtureDef.isSensor = true;
				body.createFixture(characterFixtureDef);
				fix = body.getFixtureList().get(1);
//				fix.setUserData(objectName + "_groundsensor");
				fix.setUserData("groundsensor");
				//----------------------------
				
				//---- sensor for RADAR (wow that was close)
				vektor.set(0.0f, 0.05f); //---offset to make fixture at feet for ground
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - 0.4f, bubblr.pixelsToMeter(height) - 0.3f, vektor, 0.0f);		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 0f;
				characterFixtureDef.friction = 0.0f; 
				characterFixtureDef.isSensor = true;
				body.createFixture(characterFixtureDef);
				characterShape.dispose();
				fix = body.getFixtureList().get(2);
				fix.setUserData("radarsensor");
				//----------------------------
				
				body.setFixedRotation(fixedRotation);
			}
			
			
			if (typ == "monster") {
				characterBodyDef.type = BodyDef.BodyType.DynamicBody;
				body = box2dwelt.createBody(characterBodyDef);
				
				PolygonShape characterShape = new PolygonShape();
				
				//---normal fixture
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x), bubblr.pixelsToMeter(height) - bubblr.pixelsToMeter(fix_verkleinerung_y));		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 1.0f;
				characterFixtureDef.friction = 0.0f;
				characterFixtureDef.filter.groupIndex = Welt.GROUP_MONSTER;
				
				body.createFixture(characterFixtureDef);
				fix = body.getFixtureList().get(0);
				fix.setUserData(objectName);
				
				//---- sensor for ground
				vektor.set(0.0f, -0.35f); //---offset to make fixture at feet for ground
				characterShape.setAsBox(bubblr.pixelsToMeter(width) - bubblr.pixelsToMeter(fix_verkleinerung_x), 0.1f, vektor, 0.0f);		// besser quadrat damit die fixture nicht nach dem "rumwirbeln" 90 grad verkehrt ist
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 0f;
				characterFixtureDef.friction = 0.0f; 
				characterFixtureDef.isSensor = true;
				body.createFixture(characterFixtureDef);
				characterShape.dispose();
				fix = body.getFixtureList().get(1);
				fix.setUserData("monster_groundsensor");
				//----------------------------
				
				body.setFixedRotation(fixedRotation);
			}
			
			
			if (typ == "circle") {
				characterBodyDef.type = BodyDef.BodyType.DynamicBody;

				characterBodyDef.bullet = true;			
				
				body = box2dwelt.createBody(characterBodyDef);
				
				CircleShape characterShape = new CircleShape();
				characterShape.setRadius(bubblr.pixelsToMeter((width / 2) - fix_verkleinerung_x)); // durch 2 da radius (�bergeben wird kantenl�nge (width) = durchmesser)
				characterFixtureDef.shape = characterShape;
				characterFixtureDef.density = 1.0f;
				characterFixtureDef.friction = 0.0f;		// 0 -> kein h�ngenbleiben an seiten, aber daf�r sliden �ber boden :(
				 
				
				if (objectName.equals("bubble")) {
					characterFixtureDef.filter.categoryBits = Welt.CATEGORY_BUBBLE;
					characterFixtureDef.filter.maskBits = Welt.MASK_BUBBLE;			// mask collectables and bubbles
				}
				
				
				body.createFixture(characterFixtureDef);
				characterShape.dispose(); 
				fix = body.getFixtureList().get(0);
				fix.setUserData(objectName);
				body.setFixedRotation(fixedRotation);
			}
		}
		
		
		
//			body.setMassData(masse);	// klappt hier irgendwie nicht mehr... wird jetzt in Welt-> character-setup erledigt
		
			

		//------------------------------
	}
	
	
	
	
	public float getWidth_px() {
		return width;
	}

	public float getHeight_px() {
		return height;
	}
	
	
	public float getWidth_mtr() {
		return bubblr.pixelsToMeter(width);
	}

	public float getHeight_mtr() {
		return bubblr.pixelsToMeter(height);
	}
	
	
}
