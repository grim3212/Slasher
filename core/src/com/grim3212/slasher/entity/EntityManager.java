package com.grim3212.slasher.entity;

import java.util.HashMap;

import com.grim3212.slasher.entity.player.Player;
import com.grim3212.slasher.util.Constants;

public class EntityManager {

	private static HashMap<String, Entity> entities = new HashMap<String, Entity>();

	public static void registerEntity(Entity entity) {
		entities.put(entity.entityID, entity);
	}

	public static Entity getEntity(String id) {
		if (!entities.containsKey(id))
			return null;

		return entities.get(id);
	}

	public static Player getPlayer() {
		if (!entities.containsKey(Constants.PLAYER_ID))
			return null;

		return (Player) entities.get(Constants.PLAYER_ID);
	}

	public static HashMap<String, Entity> getEntityList() {
		return entities;
	}
}
