package com.moribitotech.mtx;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonGame extends AbstractButton {

	float wrap;
	
	
	public ButtonGame(Drawable up, Drawable down) {
		super(up, down);
	}
	
	public ButtonGame(Drawable up, Drawable down, float wrap) {
		super(up, down);
		this.wrap = wrap;
	}
	
	public ButtonGame(Drawable up, Drawable down, TextureRegion icon, float iconPosX, float iconPosY) {
		super(up, down, icon, iconPosX, iconPosY);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// If button locked
		// ##################################################################
		if (isLockActive && textureLocked != null) {
			drawLocked(batch);
		}
			

		// If text set and intented to be used
		// ##################################################################
		else if (text != "" && bitMapFont != null && isTextActive) {
			super.draw(batch, parentAlpha);
			if (wrap > 0) {
				drawTextMultiLine(batch, wrap);
			}
			else {
				drawText(batch);
			}
			drawExternalTexture(batch);
		}

		// Draw default
		// ##################################################################
		else {
			super.draw(batch, parentAlpha);
			drawExternalTexture(batch);
		}
		
		if (icon != null) drawIcon(batch, icon, iconPosX, iconPosY);
	}


	private void drawExternalTexture(SpriteBatch batch) {
		if(isExternalTextureActive && textureExternal != null){
			batch.draw(textureExternal, getX() + externalTexturePosX, getY() + externalTexturePosY, externalTextureSizeW, externalTextureSizeH);
		}
	}

	private void drawLocked(SpriteBatch batch) {
		batch.draw(textureLocked, getX(), getY(), getWidth(), getHeight());
	}
	
	private void drawIcon(SpriteBatch batch, TextureRegion icon, float iconPosX, float iconPosY) {
		batch.draw(icon, getX() +  iconPosX, getY() + iconPosY);
	}
	
	private void drawText(SpriteBatch batch) {
		bitMapFont.draw(batch, text, getX() +  textPosX, getY() + textPosY);
	}
	
	private void drawTextMultiLine(SpriteBatch batch, float wrap) {
	
//		bitMapFont.drawMultiLine(batch, text, getX() +  textPosX, getY() + textPosY, wrap, BitmapFont.HAlignment.CENTER);
		bitMapFont.drawWrapped(batch, text, getX() +  textPosX, getY() + textPosY, wrap, BitmapFont.HAlignment.CENTER);
	}
	
	
}
