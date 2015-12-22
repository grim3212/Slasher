package com.grim3212.slasher.util;

public class GameUtil {

	public static float unitScale = 1 / 16F;

	public static float convertToMapUnits(float input) {
		return unitScale * input;
	}

}
