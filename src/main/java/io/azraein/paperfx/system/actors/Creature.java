package io.azraein.paperfx.system.actors;

import java.io.Serializable;

public class Creature implements Serializable {

	private static final long serialVersionUID = -6435028869545021494L;

	private String creatureId;

	public Creature(String creatureId) {
		this.creatureId = creatureId;
	}

	public String getCreatureId() {
		return creatureId;
	}
}
