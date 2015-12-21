package com.grim3212.slasher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.grim3212.slasher.PhysicsTest;

public class Testing {

	public static void main(String[] argv) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 320;
		config.title = "Physics Test";
		new LwjglApplication(new PhysicsTest(), config);
	}

}
