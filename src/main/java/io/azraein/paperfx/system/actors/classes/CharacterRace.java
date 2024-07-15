package io.azraein.paperfx.system.actors.classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;

public class CharacterRace implements Serializable {

	private static final long serialVersionUID = -1719264066833175197L;

	private String characterRaceId;

	private int[] characterRaceBaseAttributes;
	private Map<Skill, Integer> characterRaceSkillBonuses;

	public CharacterRace(String characterRaceId) {
		this.characterRaceId = characterRaceId;
		characterRaceBaseAttributes = new int[Attribute.values().length];
		characterRaceSkillBonuses = new HashMap<>();
	}

	public String getCharacterRaceId() {
		return characterRaceId;
	}

	public int getCharacterRaceSkillBonus(Skill skill) {
		return characterRaceSkillBonuses.get(skill);
	}

	public Map<Skill, Integer> getCharacterRaceSkillBonuses() {
		return characterRaceSkillBonuses;
	}

	public int getCharacterRaceBaseAttribute(Attribute attribute) {
		return characterRaceBaseAttributes[attribute.ordinal()];
	}

	public int[] getCharacterRaceBaseAttributes() {
		return characterRaceBaseAttributes;
	}

}
