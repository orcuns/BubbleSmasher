package com.mygdx.bubble_smasher;

/**
 * Copyright 2013 Robin Stumm (serverkorken@googlemail.com, http://dermetfan.bplaced.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import net.dermetfan.utils.libgdx.box2d.Box2DUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * A {@link FixtureSprite} is a {@link Sprite} with additional drawing information and the abililty to draw itself on a given {@link Fixture}.
 * It is supposed to be put in the user data of a {@link Fixture}.
 * 
 * @author dermetfan
 */
public class FixtureSprite extends Sprite {

	/** if the width and height should be adjusted to those of the {@link Fixture} this {@link FixtureSprite} is attached to (true by default) */
	private boolean adjustWidth = true, adjustHeight = true;

	/** if the origin of this {@link FixtureSprite} should be used when it's being drawn (false by default) */
	private boolean useOriginX = false, useOriginY = false;

	/** a user data object replacing the user data that this {@link FixtureSprite} replaces if it's set as {@link Fixture#getUserData() user data} */
	private Object userData;

	/** @see Sprite#Sprite(Texture) */
	public FixtureSprite(Texture texture) {
		super(texture);
	}

	/** @see Sprite#Sprite(Sprite) */
	public FixtureSprite(Sprite sprite) {
		super(sprite);
	}

	/** temporary variable used in {@link #draw(SpriteBatch, World)} */
	private static Array<Body> tmpBodies = new Array<Body>(0);

	/** draws all the {@link FixtureSprite FixtureSprites} on the {@link Fixture Fixtures} that hold them in their {@link Fixture#getUserData() user data} in the given {@link World} */
	public static void draw(SpriteBatch batch, World world) {
		world.getBodies(tmpBodies);
		for(Body body : tmpBodies)
			for(Fixture fixture : body.getFixtureList())
				if(fixture.getUserData() instanceof FixtureSprite)
					((FixtureSprite) fixture.getUserData()).draw(batch, fixture);
	}

	/** draws this {@link FixtureSprite} on the given {@link Fixture} */
	public void draw(SpriteBatch batch, Fixture fixture) {
		batch.setColor(getColor());
		batch.draw(this, Box2DUtils.position(fixture).x - Box2DUtils.width(fixture) / 2 + getX(), Box2DUtils.position(fixture).y - Box2DUtils.height(fixture) / 2 + getY(), isUseOriginX() ? getOriginX() : Box2DUtils.width(fixture) / 2 - Box2DUtils.positionRelative(fixture).x, isUseOriginY() ? getOriginY() : Box2DUtils.height(fixture) / 2 - Box2DUtils.positionRelative(fixture).y, isAdjustWidth() ? Box2DUtils.width(fixture) : getWidth(), isAdjustHeight() ? Box2DUtils.height(fixture) : getHeight(), getScaleX(), getScaleY(), fixture.getBody().getAngle() * MathUtils.radiansToDegrees + getRotation());
	}

	/** @return if the width should be adjusted to those of the {@link Fixture} this {@link FixtureSprite} is attached to */
	public boolean isAdjustWidth() {
		return adjustWidth;
	}

	/** @param adjustWidth if the width should be adjusted to those of the {@link Fixture} this {@link FixtureSprite} is attached to */
	public void setAdjustWidth(boolean adjustWidth) {
		this.adjustWidth = adjustWidth;
	}

	/** @return if the height should be adjusted to those of the {@link Fixture} this {@link FixtureSprite} is attached to */
	public boolean isAdjustHeight() {
		return adjustHeight;
	}

	/** @param adjustHeight if the height should be adjusted to those of the {@link Fixture} this {@link FixtureSprite} is attached to */
	public void setAdjustHeight(boolean adjustHeight) {
		this.adjustHeight = adjustHeight;
	}

	/** @return the if the x origin of this {@link FixtureSprite} should be used when it's being drawn */
	public boolean isUseOriginX() {
		return useOriginX;
	}

	/** @param useOriginX if the x origin of this {@link FixtureSprite} should be used when it's being drawn */
	public void setUseOriginX(boolean useOriginX) {
		this.useOriginX = useOriginX;
	}

	/** @return if the y origin of this {@link FixtureSprite} should be used when it's being drawn */
	public boolean isUseOriginY() {
		return useOriginY;
	}

	/** @param useOriginY if the y origin of this {@link FixtureSprite} should be used when it's being drawn */
	public void setUseOriginY(boolean useOriginY) {
		this.useOriginY = useOriginY;
	}

	/** @see Sprite#setSize(float, float) */
	public void setWidth(float width) {
		setSize(width, getHeight());
	}

	/** @see Sprite#setSize(float, float) */
	public void setHeight(float height) {
		setSize(getWidth(), height);
	}

	/** @return the userData */
	public Object getUserData() {
		return userData;
	}

	/** @param userData the userData to set */
	public void setUserData(Object userData) {
		this.userData = userData;
	}

}