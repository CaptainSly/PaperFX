package io.azraein.paperfx.system.actors;

import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;

public class Npc extends Actor {

	private static final long serialVersionUID = 2777091766514875234L;

	public Npc(String actorId, String actorName, ActorClass charClass, ActorRace charRace) {
		super(actorId, "npc", actorName, charClass, charRace);
	}

	public Npc(String actorId, ActorState actorState) {
		super(actorId, "npc", actorState);
	}

	@Override
	public void updateActor() {
	}

}
