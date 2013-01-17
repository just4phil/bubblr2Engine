package de.philweb.bubblr.tools;


import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.philweb.bubblr.Assets;

public class NinePatchWhite extends NinePatch {
        

	
	
	private static NinePatchWhite instance;
        
        private NinePatchWhite(){
        	super(Assets.ninePatch_white, 8, 8, 8, 8);    
        }
        
        public static NinePatchWhite getInstance(){
                if(instance == null){
                        instance = new NinePatchWhite();
                }
                return instance;
        }
       
}