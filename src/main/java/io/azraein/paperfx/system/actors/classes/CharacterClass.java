package io.azraein.paperfx.system.actors.classes;

import java.io.Serializable;

import io.azraein.paperfx.system.actors.stats.Attribute;

public class CharacterClass implements Serializable {

	private static final long serialVersionUID = -3360360407417817033L;

	private String characterClassId;

	private int[] charClassAttributeBonus;

	public CharacterClass(String characterClassId) {
		this.characterClassId = characterClassId;
		charClassAttributeBonus = new int[Attribute.values().length];
	}

	public String getCharacterClassId() {
		return characterClassId;
	}

	public int getCharacterClassAttributeBonus(Attribute attribute) {
		return charClassAttributeBonus[attribute.ordinal()];
	}

	public int[] getCharacterClassAttributeBonuses() {
		return charClassAttributeBonus;
	}

}
