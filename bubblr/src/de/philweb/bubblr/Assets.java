package de.philweb.bubblr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Assets {

	
	//------ cool menu ------------
		
	private final static String FILE_IMAGE_ATLAS = "data/imageatlas/pages.atlas";
	private final static String FILE_UI_SKIN = "skin/uiskin.json";
	public static TextureAtlas imageAtlas;
	public static Skin skin;
	
	public static TextureRegion backgroundRegion;
//	
	public static Texture items;

	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion musicOn;
	public static TextureRegion musicOff;
	public static TextureRegion optionsOff;
	public static TextureRegion optionsOn;
	public static TextureRegion control_restart;
	public static TextureRegion icon_play;
	public static TextureRegion icon_credits;
	public static TextureRegion icon_options;
	public static TextureRegion icon_hints;
	public static TextureRegion icon_help;
	public static TextureRegion icon_scores;
	public static TextureRegion icon_rank;
	public static TextureRegion icon_contact;
	public static TextureRegion icon_music;
	public static TextureRegion icon_www;
    public static TextureRegion whitePixel;
    public static TextureRegion menuButton1;
    public static TextureRegion ninePatch_black;
    public static TextureRegion ninePatch_white;
	public static TextureRegion textarea;
	public static TextureRegion pause;
	public static TextureRegion play;
	public static TextureRegion quit;
	public static TextureRegion life_pl1;
	public static TextureRegion fire1;
	public static TextureRegion j1;
	public static TextureRegion j2;
	public static TextureRegion u1;
	public static TextureRegion u2;
	public static TextureRegion dummyIdle;


	public static BitmapFont font;
	
	

	
	//===================================================
	

	public static BitmapFont font10_en;
	public static BitmapFont font12_en;
	public static BitmapFont font15_en;
	public static BitmapFont font17_en;
	public static BitmapFont font20_en;
	
	public static BitmapFont font10_de;
	
	public static BitmapFont font10_pl;
	
	public static BitmapFont font10_es;

	
	
	
	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	
    public static Skin getSkin(){
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal(FILE_UI_SKIN);
            skin = new Skin( skinFile );
        }
        return skin;
    }
	
 
		
	private static void releaseResources() {
		skin = null;
		
		// TODO: unload assetmgr. ?
	}
	
	
	public static void loadRessources (AssetManager m_assetManager) {
		
		//----- BACKGROUND -------------------------------
		TextureParameter param;
		param = new TextureParameter();
		param.minFilter = TextureFilter.Linear;
//		param.genMipMaps = true;
		m_assetManager.load("data/background.png", Texture.class, param);
		
	    
		//----- ITEMS (ImageAtlas) -------------------------------
		m_assetManager.load(FILE_IMAGE_ATLAS, TextureAtlas.class);		
		
		
		//------- FONTS ------------------------------------
//		m_assetManager.load("data/font2.fnt", BitmapFont.class);
		m_assetManager.load("data/font.fnt", BitmapFont.class);
		
		
	}	
	
	
	
	
	public static void assignRessources (AssetManager m_assetManager) {
				
		
		//---------------------- BACKGROUND -------------------------------
		if (m_assetManager.isLoaded("data/background.png")) {
			
			backgroundRegion = new TextureRegion(m_assetManager.get("data/background.png", Texture.class), 0, 0, 800, 480);	
		}
		
		//--------------- ITEMS ----------------------------------------------------
		if (m_assetManager.isLoaded(FILE_IMAGE_ATLAS)) {
					
			ninePatch_black 	= m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("ninePatch_black");
			ninePatch_white 	= m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("ninePatch_white");
			musicOff 			= m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("musicOff");
			musicOn 			= m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("musicOn");
			soundOff = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("soundOff");
			soundOn = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("soundOn");
			control_restart = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("control_restart");
			icon_play = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_play");
			icon_credits = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_credits");
			icon_options = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_options");
			icon_hints = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_hints");
			icon_help = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_help");
			icon_scores = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_scores");
			icon_rank = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_rank");
			icon_contact = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_contact");
			icon_music = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_music");
			icon_www = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("icon_www");
			optionsOff = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("optionsOff");
			optionsOn = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("optionsOn");
			textarea = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("textarea");
			pause = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("pause");
			play = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("play");
			quit = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("quit");
			life_pl1 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("life_pl1");
			fire1 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("fire1");
			j1 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("j1");
			j2 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("j2");
			u1 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("u1");
			u2 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("u2");
			whitePixel = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("whitePixel");
			menuButton1 = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("menuButton1");


			dummyIdle = m_assetManager.get(FILE_IMAGE_ATLAS, TextureAtlas.class).findRegion("dummyIdle1");

			

			
			//---------- FONTS ----------------------------------------------			
			font = m_assetManager.get("data/font.fnt", BitmapFont.class);
			font.setUseIntegerPositions(false);			//----------------------------------------------------------------- bringt das �berhaupt was?
			font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);	//--------------------- bringt das �berhaupt was?
			
			//---- generate freetype font dynamically -----------------
			
			loadFont();
		}
	}

 
	public static void loadFont () {

		FreeTypeFontGenerator generator;
		
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		int fontSize = 20;
		
		if (screenWidth >= 600)		fontSize = 26;
		if (screenWidth >= 800) 	fontSize = 26;
		if (screenWidth >= 1280) 	fontSize = 26;
			
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ARIAL.TTF"));
		font10_en = generator.generateFont(fontSize);

//		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ltromatic.ttf"));
//		font12_en = generator.generateFont(Math.round(fontSize / 1.2f));
//		
//		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ltromatic.ttf"));
//		font15_en = generator.generateFont(Math.round(fontSize / 1.5f));
//		
//		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ltromatic.ttf"));
//		font17_en = generator.generateFont(Math.round(fontSize / 1.7f));
//		
//		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ltromatic.ttf"));
//		font20_en = generator.generateFont(Math.round(fontSize / 2f));

		
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ARIAL.TTF"));
		font10_de = generator.generateFont(fontSize);

		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ARIAL.TTF"));
		font10_pl = generator.generateFont(fontSize);
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ARIAL.TTF"));
		font10_es = generator.generateFont(fontSize);
		
		
		//---------------------------------------------------------------------------------
		
		
//			generator = new FreeTypeFontGenerator(Gdx.files.internal("data/ARIAL.TTF"));
//			font24_de = generator.generateFont(fontSize);
//			
//			generator = new FreeTypeFontGenerator (Gdx.files.internal("data/ARIAL.TTF"));
//			font24_pl = generator.generateFont(fontSize , "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdęfghijklmnopqrstuvwxyź1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*" , false );
//	//		font24_pl = generator.generateFont(fontSize);
		
		generator.dispose();


	}
	
	
}






