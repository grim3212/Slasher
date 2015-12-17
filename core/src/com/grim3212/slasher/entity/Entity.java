package com.grim3212.slasher.entity;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

	private Vector2 position;
	private Vector2 velocity;
	private boolean inAir = false;
	private boolean walking = false;
	private boolean facingRight = false;
	private String entityID = "";

	public Entity(String id) {
		position = new Vector2();
		velocity = new Vector2();
		entityID = id;
	}

	public String getEntityID() {
		return entityID;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public boolean isInAir() {
		return inAir;
	}

	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}
}
