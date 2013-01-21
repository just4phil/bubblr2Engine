package de.philweb.bubblr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import de.philweb.bubblr.tools.HelperFunctions;


public class WorldRenderer  {
	
	Bubblr bubblr;
	World box2dwelt;
	Welt world;
	public TiledMapHelper tiledMapHelper;		//--------------------------------------
	private Box2DDebugRenderer debugRenderer;
	Vector3 tmp = new Vector3();
	
	public int GameState;		// wird von Gamescreen �bergeben, um im renderer zu pr�fen, ob die farben der bubbles ge�ndert werden d�rfen (sonst laufen sie w�hrend level end durch
	
	static final int tiles_horizontally = 32; 
	static final int tiles_vertically = 26;
	
//	public static final int tilesize_x = 18; 
//	public static final int tilesize_y = 18;
//	static final float FRUSTUM_WIDTH = Welt.WORLD_WIDTH; 
//	static final float FRUSTUM_HEIGHT = Welt.WORLD_HEIGHT;	
//	public static int screenWidth;
//	public static int screenHeight;
//	public static float PIXELS_PER_METER_x;
//	public static float PIXELS_PER_METER_y;
	
	TextureRegion background;
	boolean gotKeyframe; //--- zum checken ob ein keyframe gefunden wurde, falls nicht -> default (siehe renderCollectables)
	 
	private TextureRegion keyFrame;
	public int frameCount = 0;
	public int lastFPS = 0;
	public int minFPS = 100;
	public int maxFPS = 0;
	
	long now;
	String mapString;
	float textWidth;
	int i;
	int len;
	float winkel;
	float x;
	float y;
	character_goodGuy player;
	float oldScaleX;
	float oldScaleY;
	String toastText;
	float toastWidth;
	
	//---- fuer bubble-rendering
	Color color;
	float oldRed;
	float oldGreen;
	float oldBlue;
	float oldAlpha;
	float faktor;
	float scale;

	public long timeDiff;		// zum tracken von pausenzeiten durch PAUSE button

	
	
	//---- for Strings ---
	StringBuffer textPuffer;
	

	OrthographicCamera gameplayCamera;

	//--------------------------------
	
	
	
	public WorldRenderer (SpriteBatch batch, Level level, int levelNumber, Bubblr bubblr) {
		
//		screenWidth = Gdx.graphics.getWidth();			// sp�ter zum resizen f�r unterschiedliche screen-gr��en nutzen?!
//		screenHeight = Gdx.graphics.getHeight();		// sp�ter zum resizen f�r unterschiedliche screen-gr��en nutzen?!
	
//		setPixPerMeter();
	
		this.bubblr = bubblr;
		this.world = level.world;
		this.box2dwelt = level.box2dwelt;
		this.gameplayCamera = bubblr.getCamera();
		
		textPuffer = new StringBuffer();
			
		
		//----------- tiled map ----------------------------------------------------
		
		mapString = "level" + levelNumber +".tmx";

		debugRenderer = new Box2DDebugRenderer();
		
		tiledMapHelper = new TiledMapHelper();
		tiledMapHelper.setPackerDirectory("data/packer");
		tiledMapHelper.loadMap("data/world/level1/" + mapString);
		tiledMapHelper.loadCollisions("data/collisions.txt", level.box2dwelt, Welt.PIXELS_PER_METER); // 60 = PIXELS_PER_METER
		//------------------------------------------------------	
		
		
	}

	
//===========================================================================================	
	
	
	public void render (Level level, SpriteBatch batch) {
				
		gameplayCamera.update();
		gameplayCamera.apply(Gdx.gl10);
		
		// --- render background: hintergrund auf schwarz setzen --------
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//-------------------------------------------
		
		
		
		//----------- cam position for tiled map ----------------------------------------------------
		gameplayCamera.position.x = 288;	 // (800 / 2) - ((800 - 512) / 2)	
		gameplayCamera.position.y = 234;
		gameplayCamera.update();
				
		tiledMapHelper.tileMapRenderer.getProjectionMatrix().set(gameplayCamera.combined);
		tmp.set(0, 0, 0);
		gameplayCamera.unproject(tmp);
		
		//------------- render tiledmap -----------
		tiledMapHelper.render((int) tmp.x, (int) tmp.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		//---- prepare cam for gameplay view -------------		
		batch.setProjectionMatrix(gameplayCamera.combined);

		
		//------ render gameplay -------------------------
		batch.enableBlending();
		
		batch.begin();
		
		
		
		//---------- render gameobjects, here: player ------------
		len = world.players.size();
		for (i = 0; i < len; i++) {
			player = world.players.get(i);

			keyFrame = Assets.dummyIdle; 
			
		
			//--- sprite ggf. wenden -----
			if (player.flip == true) keyFrame.flip(true,  false);
			
			//--- sprite an die position des box2d- jumper-bodies setzen
			batch.draw(keyFrame, 
				(HelperFunctions.meterToPixels(player.body.getPosition().x)) - (player.getWidth_px() / 2), 
				(HelperFunctions.meterToPixels(player.body.getPosition().y)) - (player.getHeight_px() / 2) +2f);
			
			//--- wenn sprite oben gewendet wurde, dann wieder zur�ck wenden -----
			if (player.flip == true) {
				keyFrame.flip(true,  false);
				player.flip = false;
			}
		}
		//--------------------------------------------
		
		
		
		batch.end();
	   
		
		
		
		//----- Debugging ---------------------------
//		debugRenderer.render(box2dwelt, tiledMapHelper.getCamera().combined.scale(Welt.PIXELS_PER_METER, Welt.PIXELS_PER_METER, Welt.PIXELS_PER_METER));
		
		
	}

	
	
//===========================================================================
	


//		public static void setPixPerMeter() {
//			
//			screenWidth = Gdx.graphics.getWidth();			// sp�ter zum resizen f�r unterschiedliche screen-gr��en nutzen?!
//			screenHeight = Gdx.graphics.getHeight();		// sp�ter zum resizen f�r unterschiedliche screen-gr��en nutzen?!
//			
//			--- pixel pro meter dynamisch aus der aufl�sung errechnen ---> screen wird ggf. gestretcht
//			PIXELS_PER_METER_x = screenWidth / FRUSTUM_WIDTH;
//			PIXELS_PER_METER_y = screenHeight / FRUSTUM_HEIGHT;
//			
//			Gdx.app.log("Resolution", "" + screenWidth + " x " + screenHeight + " // AspectRatio: " + ((float)screenWidth/screenHeight) + " // PIXELS_PER_METER: " + PIXELS_PER_METER_x + " x " +PIXELS_PER_METER_y);
//			Gdx.app.log("Welt.WORLD_WIDTH", "" + Welt.WORLD_WIDTH + "" + PIXELS_PER_METER_x + " // Welt.WORLD_WIDTH * PIXELS_PER_METER_x: " + Welt.WORLD_WIDTH * PIXELS_PER_METER_x);
//		}
//---------------------	
	
}

