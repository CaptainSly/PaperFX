package io.azraein.paperfx.system.actors;

import java.io.Serializable;

import org.tinylog.Logger;

import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.CharacterClass;
import io.azraein.paperfx.system.actors.classes.CharacterRace;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import io.azraein.paperfx.system.actors.stats.Stat;
import io.azraein.paperfx.system.inventory.Inventory;

public abstract class Actor implements Serializable {

	private static final long serialVersionUID = -3805788470792235597L;

	public static final transient int BASE_EXP = 1000;
	public static final transient double EXPONENT = 1.750;

	protected String actorId;

	// Actor Information
	private String actorName;
	private String actorDescription;

	private CharacterRace actorRace;
	private CharacterClass actorClass;

	// Actor Stats
	private int actorMaxHp, actorCurrentHp;
	private int actorMaxMp, actorCurrentMp;

	private int actorExp;
	private int actorLevel;

	private int[] actorAttributes;
	private Stat[] actorSkills;

	// Actor Inventory
	private Inventory actorInventory;
	private double actorEncumbranceLimit;

	public Actor(String actorId, CharacterClass actorClass, CharacterRace actorRace) {
		this.actorId = actorId;
		this.actorClass = actorClass;
		this.actorRace = actorRace;

		actorExp = 0;
		actorLevel = 1;

		actorInventory = new Inventory();

		actorAttributes = new int[Attribute.values().length];
		actorSkills = new Stat[Skill.values().length];

		for (Attribute attribute : Attribute.values()) {
			actorAttributes[attribute.ordinal()] = actorRace.getCharacterRaceBaseAttribute(attribute);
		}

		for (Skill skill : Skill.values()) {
			actorSkills[skill.ordinal()] = new Stat(skill, Stat.BASE_EXP, Stat.EXPONENT);

			if (actorRace.getCharacterRaceSkillBonuses().containsKey(skill)) {
				actorSkills[skill.ordinal()].addXp(Utils.getTotalXPForLevel(Stat.BASE_EXP, Stat.EXPONENT,
						actorRace.getCharacterRaceSkillBonus(skill)));
			}

		}

		actorEncumbranceLimit = getActorAttribute(Attribute.STRENGTH) * 5;

	}

	private int getXpForNextLevel() {
		return (int) (BASE_EXP * Math.pow(actorLevel, EXPONENT));
	}

	public void addXp(int xp) {
		actorExp += xp;
		while (actorExp >= getXpForNextLevel()) {
			actorExp -= getXpForNextLevel();
			levelUp();
		}
	}

	public void levelUp() {
		actorLevel++;

		// Adjust Stats on level up. I dunno how yet.

		Logger.debug(actorName + " is now level " + actorLevel);
	}

	public void useSkill(Skill skill, int expGained) {
		actorSkills[skill.ordinal()].addXp(expGained);
	}

	public String getActorId() {
		return actorId;
	}

	public String getActorName() {
		return actorName;
	}

	public String getActorDescription() {
		return actorDescription;
	}

	public boolean isActorEncumbered() {
		return actorEncumbranceLimit < actorInventory.getInventoryWeight();
	}

	public int getActorExp() {
		return actorExp;
	}

	public int getActorLevel() {
		return actorLevel;
	}

	public int getActorMaxHp() {
		return actorMaxHp;
	}

	public int getActorCurrentHp() {
		return actorCurrentHp;
	}

	public int getActorMaxMp() {
		return actorMaxMp;
	}

	public int getActorCurrentMp() {
		return actorCurrentMp;
	}

	public int[] getActorAttributes() {
		return actorAttributes;
	}

	public Stat[] getActorSkills() {
		return actorSkills;
	}

	public int getActorAttribute(Attribute attribute) {
		return actorAttributes[attribute.ordinal()];
	}

	public Stat getActorSkill(Skill skill) {
		return actorSkills[skill.ordinal()];
	}

	public CharacterRace getActorCharacterRace() {
		return actorRace;
	}

	public CharacterClass getActorCharacterClass() {
		return actorClass;
	}

	public Inventory getActorInventory() {
		return actorInventory;
	}

}
