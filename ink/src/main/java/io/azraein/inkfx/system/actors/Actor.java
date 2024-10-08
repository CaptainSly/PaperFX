package io.azraein.inkfx.system.actors;

import java.io.Serializable;

import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;

public abstract class Actor implements Serializable {

	private static final long serialVersionUID = -3805788470792235597L;

	protected String actorId;
	protected String actorType;
	private ActorState actorState;

	public Actor(String actorId, String actorType, String actorName, ActorClass actorClass, ActorRace actorRace) {
		this.actorId = actorId;
		this.actorType = actorType;
		actorState = new ActorState(actorName, actorClass, actorRace);
		initActor();
	}

	public Actor(String actorId, String actorType, ActorState actorState) {
		this.actorId = actorId;
		this.actorType = actorType;
		this.actorState = actorState;
		initActor();
	}

	public abstract void initActor();

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
