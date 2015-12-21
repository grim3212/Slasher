package com.grim3212.slasher.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager {

	private static World world;

	public static void initWorld() {
		Box2D.init();

		world = new World(new Vector2(0, -10F), true);
		world.setContactListener(new CollisionCallback());
	}

	public static World getWorld() {
		return world;
	}

}
