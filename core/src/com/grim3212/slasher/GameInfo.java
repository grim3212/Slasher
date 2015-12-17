package com.grim3212.slasher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameInfo {

	public static String name = "";
	public static String version = "";
	public static String author = "";
	public static String title = "";

	public static boolean vsync = false;
	public static boolean drawFPS = false;

	public static void loadGameInfo() {
		JsonValue root = new JsonReader().parse(Gdx.files.internal("gameInfo.json"));

		name = root.get("info").getString("name");
		version = root.get("info").getString("version");
		author = root.get("info").getString("author");

		vsync = root.get("settings").getBoolean("vsync");
		drawFPS = root.get("settings").getBoolean("drawFPS");

		title = name + " V" + version + " | By: " + author;
	}

}
