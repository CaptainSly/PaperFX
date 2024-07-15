package io.azraein.paperfx.system;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.Player;

public class World implements Serializable {

	private static final long serialVersionUID = -2986100898471011781L;

	private Player player;
	private String currentLocationId;

	public World(Player player, String currentLocationId) {
		this.player = player;
		this.currentLocationId = currentLocationId;
	}
	
	public Player getPlayer() {
		return player;
	}

	public String getCurrentLocationId() {
		return currentLocationId;
	}

}
