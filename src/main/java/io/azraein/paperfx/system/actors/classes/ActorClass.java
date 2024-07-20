package io.azraein.paperfx.system.actors.classes;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.stats.Attribute;

public class ActorClass implements Serializable {

	private static final long serialVersionUID = -3360360407417817033L;

	private String actorClassId;
	private String actorClassName;
	private String actorClassDescription;

	private int[] actorClassAttributeBonus;

	public ActorClass(String actorClassId, String actorClassName) {
		this.actorClassId = actorClassId;
		this.actorClassName = actorClassName;
		actorClassAttributeBonus = new int[Attribute.values().length];
	}

	public String getActorClassId() {
		return actorClassId;
	}

	public int getActorClassAttributeBonus(Attribute attribute) {
		return actorClassAttributeBonus[attribute.ordinal()];
	}

	public int[] getActorClassAttributeBonuses() {
		return actorClassAttributeBonus;
	}

	public String getActorClassName() {
		return actorClassName;
	}

	public String getActorClassDescription() {
		return actorClassDescription;
	}

	public void setActorClassId(String actorClassId) {
		this.actorClassId = actorClassId;
	}

	public void setActorClassName(String actorClassName) {
		this.actorClassName = actorClassName;
	}

	public void setActorClassDescription(String actorClassDescription) {
		this.actorClassDescription = actorClassDescription;
	}

	public void setActorClassAttributeBonus(int[] actorClassAttributeBonus) {
		this.actorClassAttributeBonus = actorClassAttributeBonus;
	}

}
