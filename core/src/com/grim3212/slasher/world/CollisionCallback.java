package com.grim3212.slasher.world;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.grim3212.slasher.entity.EntityManager;

public class CollisionCallback implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.getUserData() != null && fA.getUserData().equals("player|foot")) {
			EntityManager.getPlayer().onGround = true;
			EntityManager.getPlayer().secondJump = false;
		}

		if (fB.getUserData() != null && fB.getUserData().equals("player|foot")) {
			EntityManager.getPlayer().onGround = true;
			EntityManager.getPlayer().secondJump = false;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fA = contact.getFixtureA();
		Fixture fB = contact.getFixtureB();

		if (fA.getUserData() != null && fA.getUserData().equals("player|foot")) {
			EntityManager.getPlayer().onGround = false;
		}

		if (fB.getUserData() != null && fB.getUserData().equals("player|foot")) {
			EntityManager.getPlayer().onGround = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
