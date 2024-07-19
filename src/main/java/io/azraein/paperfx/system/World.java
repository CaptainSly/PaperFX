package io.azraein.paperfx.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.actors.Player;
import io.azraein.paperfx.system.actors.creatures.CreatureState;
import io.azraein.paperfx.system.locations.Direction;
import io.azraein.paperfx.system.locations.LocationState;

public class World implements Serializable {

	private static final long serialVersionUID = -2986100898471011781L;

	private transient PaperFX paperFX;

	private Player player;
	private String currentLocationId;

	// A list of the current plugins activated for this world.
	private List<String> worldPlugins;

	private Map<String, LocationState> locationStates;
	private Map<String, ActorState> actorStates;
	private Map<String, CreatureState> creatureStates;

	public World(PaperFX paperFX, Player player, String currentLocationId) {
		this.paperFX = paperFX;
		this.player = player;
		this.currentLocationId = currentLocationId;
		locationStates = new HashMap<>();
		actorStates = new HashMap<>();
		creatureStates = new HashMap<>();

		worldPlugins = new ArrayList<>();
	}

	public void updateWorld() {
		
	}

	public void moveDirection(Direction direction) { 

	}

	public Player getPlayer() {
		return player;
	}

	public String getCurrentLocationId() {
		return currentLocationId;
	}

	public List<String> getWorldPlugins() {
		return worldPlugins;
	}

	public Map<String, LocationState> getLocationStates() {
		return locationStates;
	}

	public Map<String, ActorState> getActorStates() {
		return actorStates;
	}

	public Map<String, CreatureState> getCreatureStates() {
		return creatureStates;
	}

}
