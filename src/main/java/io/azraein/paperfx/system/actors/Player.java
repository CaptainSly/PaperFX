package io.azraein.paperfx.system.actors;

import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;

public class Player extends Actor {

	private static final long serialVersionUID = -6782575409641873407L;

	public Player(String actorName, ActorClass charClass, ActorRace charRace) {
		super("PLAYER_ACTOR", actorName, charClass, charRace);
	}

	@Override
	public void updateActor() {
	}

}
