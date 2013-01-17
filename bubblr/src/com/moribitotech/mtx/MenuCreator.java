package com.moribitotech.mtx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuCreator {    
    public static Table createTable(boolean fillParent, Skin skin){
        Table table = new Table(skin );
        table.setFillParent( fillParent );        
        return table;
    }    
    
    public static ButtonLevel createCustomLevelButton(TextureRegion up, TextureRegion down){
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonLevel(dUp, dDown);
    }
    
    public static ButtonGame createCustomGameButton(TextureRegion up, TextureRegion down){
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonGame(dUp, dDown);
    }

    public static ButtonGame createCustomGameButton(TextureRegion up, TextureRegion down, TextureRegion icon, float iconPosX, float iconPosY){
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonGame(dUp, dDown, icon, iconPosX, iconPosY);
    }

    public static ButtonGame createCustomGameButton(TextureRegion up, TextureRegion down, float wrap){
        Drawable dUp = new TextureRegionDrawable(up);
        Drawable dDown = new TextureRegionDrawable(down);
        return new ButtonGame(dUp, dDown, wrap);
    }
    
    public static ButtonToggle createCustomToggleButton(TextureRegion tOn, TextureRegion tOff, boolean isToggleActive){
        Drawable dUp = new TextureRegionDrawable(tOn);
        Drawable dDown = new TextureRegionDrawable(tOff);
        return new ButtonToggle(dUp, dDown, tOn, tOff,  isToggleActive);
    }
}
