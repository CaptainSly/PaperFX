package io.azraein.paperfx.system.actors.creatures;

import java.io.Serializable;

public abstract class Creature implements Serializable {

	private static final long serialVersionUID = -6435028869545021494L;

	private String creatureId;

	private CreatureState creatureState;

	public Creature(String creatureId) {
		this.creatureId = creatureId;
		creatureState = new CreatureState();
	}

	public String getCreatureId() {
		return creatureId;
	}

	public void setCreatureState(CreatureState creatureState) {
		this.creatureState = creatureState;
	}

	public CreatureState getCreatureState() {
		return creatureState;
	}
}
