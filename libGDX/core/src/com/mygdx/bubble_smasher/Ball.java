package com.mygdx.bubble_smasher;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Ball {
	Vector2 position = new Vector2();
	TextureRegion image;
	boolean counted;
	Body ballBody;
	
	public Ball(float x, float y, TextureRegion image, Body body) {
		this.position.x = x;
		this.position.y = y;
		this.image = image;
		this.ballBody = body;
	}
}