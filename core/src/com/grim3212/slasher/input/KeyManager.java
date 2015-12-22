package com.grim3212.slasher.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.grim3212.slasher.entity.EntityManager;

public class KeyManager {

	public static int JUMP = Keys.SPACE;
	public static int JUMP_ALT = Keys.W;
	public static int LEFT = Keys.A;
	public static int LEFT_ALT = Keys.LEFT;
	public static int RIGHT = Keys.D;
	public static int RIGHT_ALT = Keys.RIGHT;
	public static int PAUSE = Keys.ESCAPE;
	public static int PAUSE_ALT = Keys.BACKSPACE;

	public static void handleInput() {
		if (Gdx.input.isKeyPressed(PAUSE) || Gdx.input.isKeyPressed(PAUSE_ALT)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(LEFT_ALT)) {
			EntityManager.getPlayer().movePlayer(false);
		}

		if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(RIGHT_ALT)) {
			EntityManager.getPlayer().movePlayer(true);
		}

		if (Gdx.input.isKeyJustPressed(JUMP) || Gdx.input.isKeyJustPressed(JUMP_ALT)) {
			EntityManager.getPlayer().jump();
		}
	}

	public static boolean isMovementKeyPressed() {
		if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(LEFT_ALT)) {
			return true;
		}

		if (Gdx.input.isKeyPressed(RIGHT) || Gdx.input.isKeyPressed(RIGHT_ALT)) {
			return true;
		}

		return false;
	}

}
