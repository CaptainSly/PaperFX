package io.azraein.paperfx.system.actors.classes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;

public class ActorClass implements Serializable {

	private static final long serialVersionUID = -3360360407417817033L;

	private String actorClassId;
	private String actorClassName;
	private String actorClassDescription;

	private int[] actorClassBaseSkills;
	private Set<Attribute> actorClassFavoredAttributes;
	private Set<Skill> actorClassSkills;

	public ActorClass(String actorClassId, String actorClassName) {
		this.actorClassId = actorClassId;
		this.actorClassName = actorClassName;
		actorClassBaseSkills = new int[Skill.values().length];
		actorClassFavoredAttributes = new HashSet<>(2);
		actorClassSkills = new HashSet<>(6);
	}

	public String getActorClassId() {
		return actorClassId;
	}

	public String getActorClassName() {
		return actorClassName;
	}

	public String getActorClassDescription() {
		return actorClassDescription;
	}
	
	public int[] getActorClassBaseSkills() {
		return actorClassBaseSkills;
	}
	
	public Set<Attribute> getActorClassFavoredAttributes() {
		return actorClassFavoredAttributes;
	}

	public Set<Skill> getActorClassSkills() {
		return actorClassSkills;
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

	public void setActorClassSkills(List<Skill> actorSkills) {
		actorClassSkills.addAll(actorSkills);
	}
	
	public void setActorClassFavoredAttribute(Attribute attr1, Attribute attr2) {
		actorClassFavoredAttributes.add(attr1);
		actorClassFavoredAttributes.add(attr2);
	}

}