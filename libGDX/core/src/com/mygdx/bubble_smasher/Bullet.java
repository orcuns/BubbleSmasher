package com.mygdx.bubble_smasher;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	Vector2 position = new Vector2();
	TextureRegion image;
	
	
	public Bullet(float x, float y, TextureRegion image) {
		this.position.x = x;
		this.position.y = y;
		this.image = image;
	}
}