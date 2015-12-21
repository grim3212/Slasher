package com.grim3212.slasher.entity.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.grim3212.slasher.entity.Entity;
import com.grim3212.slasher.world.WorldManager;

public class Player extends Entity {

	public Texture texture;
	public boolean secondJump = false;

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
		body.setBullet(true);

		// Create a circle shape and set its radius to 6
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5F, 0.95F);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0f;

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);
		box.dispose();

		// Create a circle shape and set its radius to 6
		CircleShape sensor = new CircleShape();
		sensor.setRadius(0.45F);
		sensor.setPosition(new Vector2(0, -0.95F));

		// Create a fixture definition to apply our shape to
		FixtureDef sensorFixture = new FixtureDef();
		sensorFixture.shape = sensor;
		sensorFixture.isSensor = true;
		// Create our fixture and attach it to the body
		Fixture fx = body.createFixture(sensorFixture);
		fx.setUserData("bottom");
		sensor.dispose();

		return body;
	}
}
