package com.grim3212.slasher.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameInfo {

	public static String name = "";
	public static String version = "";
	public static String author = "";
	public static String title = "";

	public static int width = 600;
	public static int height = 480;

	public static boolean vsync = false;
	public static boolean fullscreen = false;
	public static boolean drawFPS = false;
	public static boolean debug = false;
	public static boolean skip = false;

	public static void loadGameInfo() {
		JsonValue root = new JsonReader().parse(Gdx.files.internal("gameInfo.json"));

		name = root.get("info").getString("name");
		version = root.get("info").getString("version");
		author = root.get("info").getString("author");

		width = root.get("settings").getInt("width");
		height = root.get("settings").getInt("height");

		vsync = root.get("settings").getBoolean("vsync");
		drawFPS = root.get("settings").getBoolean("drawFPS");
		debug = root.get("settings").getBoolean("debug");
		skip = root.get("settings").getBoolean("skip");

		title = name + " V" + version + " | By: " + author;
	}

}
