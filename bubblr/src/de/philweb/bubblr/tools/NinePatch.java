package de.philweb.bubblr.tools;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class NinePatch {
        
  
        
        public static void drawBoxes(boolean white, SpriteBatch batcher, int numberOfTextboxes, float TextboxWidth, float TextboxHeight, float TextboxSpacing, float offsetX, float offsetY) {
            	
        	float startTextbox_x;
        	float startTextbox_y;
        	
        	startTextbox_x = (800 / 2) - (TextboxWidth / 2) - offsetX;
        	startTextbox_y = (480 / 2) - (( (numberOfTextboxes * TextboxHeight) + ((numberOfTextboxes - 1) * TextboxSpacing)) / 2) - offsetY;
        	
        	
        	
        	if (white == true) {
        		NinePatchWhite nine;	//--- zum zeichnen der textboxen
        		nine = NinePatchWhite.getInstance();
        		
            	//--- draw text boxes
            	for (int i = 0; i < numberOfTextboxes; i++) {
    	    		
    	    		nine.draw(batcher, startTextbox_x, startTextbox_y + ((TextboxHeight + TextboxSpacing) * i), TextboxWidth, TextboxHeight);
//    	    		Gdx.app.log("box" +i, "startTextbox_x: " + startTextbox_x + " // startTextbox_y: " +(startTextbox_y + ((TextboxHeight + TextboxSpacing) * i)));
            	}
        	}
        	
        	if (white == false) {
        		NinePatchBlack nine;	//--- zum zeichnen der textboxen
        		nine = NinePatchBlack.getInstance();
        		
            	//--- draw text boxes
            	for (int i = 0; i < numberOfTextboxes; i++) {
    	    		
    	    		nine.draw(batcher, startTextbox_x, startTextbox_y + ((TextboxHeight + TextboxSpacing) * i), TextboxWidth, TextboxHeight);
//    	    		Gdx.app.log("box" +i, "startTextbox_x: " + startTextbox_x + " // startTextbox_y: " +(startTextbox_y + ((TextboxHeight + TextboxSpacing) * i)));
            	}
        	}
        }
        //---------------------------------------------------------
        
        
        public static void drawSingleBox(boolean white, SpriteBatch batcher, float TextboxWidth, float TextboxHeight, float PositionY, float offsetX) {
        	
        	float startTextbox_x;

        	startTextbox_x = (800 / 2) - (TextboxWidth / 2) - offsetX;
    	
        	
        	
        	if (white == true) {
        		NinePatchWhite nine;	//--- zum zeichnen der textboxen
        		nine = NinePatchWhite.getInstance();
    	    	nine.draw(batcher, startTextbox_x, PositionY, TextboxWidth, TextboxHeight);
        	}
        	
        	if (white == false) {
        		NinePatchBlack nine;	//--- zum zeichnen der textboxen
        		nine = NinePatchBlack.getInstance();
    	    	nine.draw(batcher, startTextbox_x, PositionY, TextboxWidth, TextboxHeight);
        	}
        }
        
        
}