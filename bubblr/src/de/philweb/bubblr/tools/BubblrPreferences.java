
package de.philweb.bubblr.tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

import de.philweb.bubblr.Assets;
import de.philweb.bubblr.Bubblr;


public class BubblrPreferences
{

    public String PREFS_NAME; // = "bubblr";
    private static final String PREF_language = "PREF_language";
    private Preferences prefs;
    private Bubblr bubblr;
    

    
    public BubblrPreferences(boolean isDemoVersion, Bubblr bubblr)    {
    
    	this.bubblr = bubblr;
    	
    	if (isDemoVersion == true) {
    		
    		PREFS_NAME = ".bubblrdemo";
    	}
    	else {
    		
    		PREFS_NAME = ".bubblrfull";
    	}
    	
    	
    	prefs = Gdx.app.getPreferences( PREFS_NAME );
    	
    }


   
    public String getPrefsName()    {
	    return prefs.getString( PREFS_NAME );
    }
    

   
    public int getInteger(String ID)    {
	    return prefs.getInteger( ID, 0); 
    }
    
    public void setInteger(String ID, int number)       {
    	prefs.putInteger( ID, number );
    	prefs.flush();
    }
    
    
    
    //------- language --------------
    
    public String getLanguage()    {
	    return prefs.getString( PREF_language, ""); 
    }
    
    public void setLanguage(String lang)       {
    	prefs.putString( PREF_language, lang );
    	prefs.flush();
    }
    
    
    
}



