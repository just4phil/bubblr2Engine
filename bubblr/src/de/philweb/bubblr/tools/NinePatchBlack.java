package de.philweb.bubblr.tools;


import com.badlogic.gdx.graphics.g2d.NinePatch;

import de.philweb.bubblr.Assets;

public class NinePatchBlack extends NinePatch {
        

	
	
	private static NinePatchBlack instance;
        
        private NinePatchBlack(){
        	super(Assets.ninePatch_black, 8, 8, 8, 8);    
        }
        
        public static NinePatchBlack getInstance(){
                if(instance == null){
                        instance = new NinePatchBlack();
                }
                return instance;
        }
       
}