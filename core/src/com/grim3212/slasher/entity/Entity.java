package com.grim3212.slasher.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Entity extends Actor {

	public Body body;
	public boolean onGround = true;
	public boolean walking = false;
	public boolean facingRight = false;
	public String entityID = "";

	public Entity(String id) {
		entityID = id;

		body = createBody();
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public abstract Body createBody();
}
