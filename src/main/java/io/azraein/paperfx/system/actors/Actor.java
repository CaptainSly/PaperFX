package io.azraein.paperfx.system.actors;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;

public abstract class Actor implements Serializable {

	private static final long serialVersionUID = -3805788470792235597L;

	protected String actorId;
	protected String actorType;
	private ActorState actorState;

	public Actor(String actorId, String actorType, String actorName, ActorClass actorClass, ActorRace actorRace) {
		this.actorId = actorId;
		this.actorType = actorType;
		actorState = new ActorState(actorName, actorClass, actorRace);
	}

	public Actor(String actorId, String actorType, ActorState actorState) {
		this.actorId = actorId;
		this.actorType = actorType;
		this.actorState = actorState;
	}

	public abstract void updateActor();

	public String getActorId() {
		return actorId;
	}

	public String getActorType() {
		return actorType;
	}

	public void setActorState(ActorState actorState) {
		this.actorState = actorState;
	}

	public ActorState getActorState() {
		return actorState;
	}

}
