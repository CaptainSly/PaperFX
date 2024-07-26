package io.azraein.paperfx.system.actors;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;

public abstract class Actor implements Serializable {

	private static final long serialVersionUID = -3805788470792235597L;

	protected String actorId;
	private ActorState actorState;

	public Actor(String actorId, String actorName, ActorClass actorClass, ActorRace actorRace) {
		this.actorId = actorId;
		actorState = new ActorState(actorName, actorClass, actorRace);
	}

	public abstract void updateActor();

	public String getActorId() {
		return actorId;
	}

	public void setActorState(ActorState actorState) {
		this.actorState = actorState;
	}

	public ActorState getActorState() {
		return actorState;
	}

}
