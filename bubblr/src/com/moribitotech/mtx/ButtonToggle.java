package com.moribitotech.mtx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ButtonToggle extends AbstractButton {
	// Toggle textures & condition
	private TextureRegion textureToggleOn;
	private TextureRegion textureToggleOff;
	private boolean isToggleActive = false;

	public ButtonToggle(Drawable up, Drawable down, TextureRegion toggleOn, TextureRegion toggleOff, boolean isToggleActive) {
		super(up, down);
		textureToggleOn = toggleOn;
		textureToggleOff = toggleOff;
		this.isToggleActive = isToggleActive;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// If button locked
		// ##################################################################
		if (isToggleActive && textureLocked != null) {
			drawLocked(batch);
		}

		// If text set and intented to be used
		// ##################################################################
		else if (text != "" && bitMapFont != null && isTextActive) {
			//super.draw(batch, parentAlpha);
			drawToggle(batch);
			drawText(batch);
			drawExternalTexture(batch);
		}

		// Draw default
		// ##################################################################
		else {
			//super.draw(batch, parentAlpha);
			drawToggle(batch);
			drawExternalTexture(batch);
		}
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if(!isLockActive){
			// If not locked detect the inputs
			
			// FIXME ButtonToggle requires manually switch each time, fix this
			// Otherwise hit method being called every frame so it swritches every frame
			//setToggleSwith();
			return super.hit(x, y, touchable);
		} else {
			// If locked do not detect any hit by returning NULL
			return null;
		}
	}	

	private void drawToggle(SpriteBatch batch) {
		if(isToggleActive){
			batch.draw(textureToggleOn, getX(), getY(), getWidth(), getHeight());
		} else{
			batch.draw(textureToggleOff, getX(), getY(), getWidth(), getHeight());

		}
	}

	private void drawExternalTexture(SpriteBatch batch) {
		if(isExternalTextureActive && textureExternal != null){
			batch.draw(textureExternal, getX() + externalTexturePosX, getY() + externalTexturePosY, externalTextureSizeW, externalTextureSizeH);
		}
	}

	private void drawLocked(SpriteBatch batch) {
		batch.draw(textureLocked, getX(), getY(), getWidth(), getHeight());
	}
	
	private void drawText(SpriteBatch batch) {
		bitMapFont.draw(batch, text, getX() +  textPosX, getY() + textPosY);
	}

	public TextureRegion getTextureToggleOn() {
		return textureToggleOn;
	}

	public void setTextureToggleOn(TextureRegion textureToggleOn) {
		this.textureToggleOn = textureToggleOn;
	}

	public TextureRegion getTextureToggleOff() {
		return textureToggleOff;
	}

	public void setTextureToggleOff(TextureRegion textureToggleOff) {
		this.textureToggleOff = textureToggleOff;
	}

	public boolean isToggleActive() {
		return isToggleActive;
	}

	public void setToggleActive(boolean isToggleActive) {
		this.isToggleActive = isToggleActive;
	}

	public void setToggleSwitch(){
		if(isToggleActive){
			isToggleActive = false;
		} else{
			isToggleActive = true;
		}
	}
}
