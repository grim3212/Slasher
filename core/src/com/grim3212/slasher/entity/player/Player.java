package com.grim3212.slasher.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.grim3212.slasher.entity.Entity;
import com.grim3212.slasher.input.KeyManager;
import com.grim3212.slasher.util.GameUtil;
import com.grim3212.slasher.world.WorldManager;

public class Player extends Entity {

	public Texture texture;
	public boolean onGround = true;
	public boolean secondJump = false;
	public float MAX_WALK_SPEED = 10f;
	private Fixture bodyFixture;
	private Fixture footFixture;

	public Player(String name) {
		super(name);
		this.setTexture(new Texture("player.png"));

		body.setUserData("player");
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public Body createBody() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0.2F, 96.28F);

		Body body = WorldManager.getWorld().createBody(bodyDef);
		body.setFixedRotation(true);

		/**
		 * Unneeded right now || body.setBullet(true);
		 **/

		// Create a circle shape and set its radius to 6
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5F, 0.95F);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0f;

		// Create our fixture and attach it to the body
		bodyFixture = body.createFixture(fixtureDef);
		bodyFixture.setUserData("player|body");
		box.dispose();

		// Create a circle shape and set its radius to 6
		CircleShape sensor = new CircleShape();
		sensor.setRadius(0.5F);
		sensor.setPosition(new Vector2(0, -0.95F));

		// Create a fixture definition to apply our shape to
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensor;
		sensorFixture.isSensor = true;
		// Create our fixture and attach it to the body
		footFixture = body.createFixture(sensorFixture);
		footFixture.setUserData("player|foot");
		sensor.dispose();

		return body;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (Math.abs(body.getLinearVelocity().x) > MAX_WALK_SPEED) {
			body.getLinearVelocity().x = Math.signum(body.getLinearVelocity().x) * MAX_WALK_SPEED;
			body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y);
		}

		// calculate stilltime & damp
		if (!KeyManager.isMovementKeyPressed()) {
			body.setLinearVelocity(body.getLinearVelocity().x * 0.9f, body.getLinearVelocity().y);
		}

		// Change friction depending on in air or on ground
		if (this.onGround) {
			bodyFixture.setFriction(0.2f);
		} else {
			bodyFixture.setFriction(0f);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (facingRight)
			batch.draw(this.texture, this.getPosition().x - 0.5F, this.getPosition().y - 1F, GameUtil.convertToMapUnits(this.texture.getWidth()), GameUtil.convertToMapUnits(this.texture.getHeight()));
		else
			batch.draw(this.texture, this.getPosition().x - 0.5F + GameUtil.convertToMapUnits(this.texture.getWidth()), this.getPosition().y - 1F, -GameUtil.convertToMapUnits(this.texture.getWidth()), GameUtil.convertToMapUnits(this.texture.getHeight()));
	}

	public void movePlayer(boolean right) {
		if (right) {
			facingRight = true;
			if (body.getLinearVelocity().x < MAX_WALK_SPEED) {
				body.applyLinearImpulse(1F, 0, getPosition().x, getPosition().y, true);
			}
		} else {
			facingRight = false;
			if (body.getLinearVelocity().x > -MAX_WALK_SPEED) {
				body.applyLinearImpulse(-1F, 0, getPosition().x, getPosition().y, true);
			}
		}
	}

	public void jump() {
		if (onGround) {
			body.applyLinearImpulse(0, 10f, getPosition().x, getPosition().y, true);
			secondJump = false;
		} else if (!onGround && !secondJump) {
			body.applyLinearImpulse(0, 5.5f, getPosition().x, getPosition().y, true);
			secondJump = true;
		}
	}
}
