package com.grim3212.slasher.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.grim3212.slasher.entity.Entity;

public class Player extends Entity {

	private Texture texture;
	private float jumpVelocity = 30F;
	private float maxVelocity = 10F;
	private float velocityDamping = 0.87F;

	public Player(String name) {
		super(name);
		this.setTexture(new Texture("player.png"));
		this.getPosition().set(0, 0);
	}

	public Player(String name, float x, float y) {
		super(name);
		this.setTexture(new Texture("player.png"));
		this.getPosition().set(x, y);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getJumpVelocity() {
		return jumpVelocity;
	}

	public void setJumpVelocity(float jumpVelocity) {
		this.jumpVelocity = jumpVelocity;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

	public float getVelocityDamping() {
		return velocityDamping;
	}

	public void setVelocityDamping(float velocityDamping) {
		this.velocityDamping = velocityDamping;
	}

}
