package com.grim3212.slasher.entity;

import com.grim3212.slasher.entity.player.Player;

public class EntityManager {

	private static Player player;

	public static Player getPlayer() {
		return player;
	}

	public static void setPlayer(Player player) {
		EntityManager.player = player;
	}

}
