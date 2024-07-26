package io.azraein.paperfx.system.actors.classes;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;

public class ActorRace implements Serializable {

	private static final long serialVersionUID = -1719264066833175197L;

	private String actorRaceId;
	private String actorRaceName;
	private String actorRaceDescription;

	private boolean isBeastRace;

	private int[] actorRaceBaseAttributes;
	private int[] actorRaceBaseSkills;

	public ActorRace(String characterRaceId, String characterRaceName) {
		this.actorRaceId = characterRaceId;
		this.actorRaceName = characterRaceName;
		actorRaceBaseAttributes = new int[Attribute.values().length];
		actorRaceBaseSkills = new int[Skill.values().length];
	}

	public String getActorRaceId() {
		return actorRaceId;
	}

	public String getActorRaceName() {
		return actorRaceName;
	}

	public String getActorRaceDescription() {
		return actorRaceDescription;
	}

	public boolean isBeastRace() {
		return isBeastRace;
	}

	public int[] getActorRaceBaseAttributes() {
		return actorRaceBaseAttributes;
	}

	public int[] getActorRaceSkillBonuses() {
		return actorRaceBaseSkills;
	}
	
	public int getActorRaceBaseSkill(Skill skill) {
		return actorRaceBaseSkills[skill.ordinal()];
	}

	public int getActorRaceBaseAttribute(Attribute attribute) {
		return actorRaceBaseAttributes[attribute.ordinal()];
	}

	public void setActorRaceId(String actorRaceId) {
		this.actorRaceId = actorRaceId;
	}

	public void setActorRaceName(String actorRaceName) {
		this.actorRaceName = actorRaceName;
	}

	public void setActorRaceDescription(String actorRaceDescription) {
		this.actorRaceDescription = actorRaceDescription;
	}

	public void setBeastRace(boolean isBeastRace) {
		this.isBeastRace = isBeastRace;
	}

	public void setActorRaceBaseAttributes(int[] actorRaceBaseAttributes) {
		this.actorRaceBaseAttributes = actorRaceBaseAttributes;
	}

	public void setActorBaseSkills(int[] actorRaceBaseSkills) {
		this.actorRaceBaseSkills = actorRaceBaseSkills;
	}

}
