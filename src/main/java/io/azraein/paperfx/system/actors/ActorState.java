package io.azraein.paperfx.system.actors;

import java.io.Serializable;

import org.tinylog.Logger;

import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import io.azraein.paperfx.system.actors.stats.Stat;
import io.azraein.paperfx.system.inventory.Inventory;

// TODO: Stat Implementation still needs work
// TODO: More Stat information needs to be added
public class ActorState implements Serializable {

	private static final long serialVersionUID = -2984008478151078486L;

	public static final transient int BASE_EXP = 1000;
	public static final transient double EXPONENT = 1.750;

	// Actor Information
	private String actorName;
	private String actorDescription;

	private transient ActorRace actorRace;
	private transient ActorClass actorClass;

	public String actorRaceId, actorClassId;

	// Actor Stats
	private int actorMaxHp, actorCurrentHp;
	private int actorMaxMp, actorCurrentMp;

	private int actorExp;
	private int actorLevel;

	private Stat<Attribute>[] actorAttributes;
	private Stat<Skill>[] actorSkills;

	// Actor Inventory
	private Inventory actorInventory;
	private double actorCarryWeight;

	@SuppressWarnings("unchecked")
	public ActorState(String actorName, String actorDescription, ActorClass actorClass, ActorRace actorRace) {
		this.actorName = actorName;
		this.actorDescription = actorDescription;
		this.actorClass = actorClass;
		this.actorRace = actorRace;

		this.actorClassId = actorClass.getActorClassId();
		this.actorRaceId = actorRace.getActorRaceId();

		actorExp = 0;
		actorLevel = 1;

		actorInventory = new Inventory();

		actorAttributes = new Stat[Attribute.values().length];
		actorSkills = new Stat[Skill.values().length];

		for (Attribute attribute : Attribute.values()) {
			int baseExp = 300;
			double exponent = 1.763;

			if (actorClass.getActorClassFavoredAttributes().contains(attribute)) {
				baseExp = 250;
				exponent = 1.623;
			}

			actorAttributes[attribute.ordinal()] = new Stat<>(attribute, attribute.name(), baseExp, exponent);
			int totalXp = Utils.getTotalXPForLevel(actorAttributes[attribute.ordinal()],
					actorRace.getActorRaceBaseAttribute(attribute));
			actorAttributes[attribute.ordinal()].addXp(totalXp);

		}

		for (Skill skill : Skill.values()) {
			int baseExp = 225;
			double exponent = 1.648;

			if (actorClass.getActorClassSkills().contains(skill)) {
				baseExp = 185;
				exponent = 1.572;
			}

			actorSkills[skill.ordinal()] = new Stat<>(skill, skill.name(), baseExp, exponent);
			int totalXp = Utils.getTotalXPForLevel(actorSkills[skill.ordinal()],
					actorRace.getActorRaceBaseSkill(skill));
			totalXp += Utils.getTotalXPForLevel(actorSkills[skill.ordinal()],
					actorClass.getActorClassBaseSkills()[skill.ordinal()]);
			actorSkills[skill.ordinal()].addXp(totalXp);
		}

		actorCarryWeight = actorAttributes[Attribute.STRENGTH.ordinal()].getLevel() * 5;
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

	public ActorState(String actorName, ActorClass actorClass, ActorRace actorRace) {
		this(actorName, "", actorClass, actorRace);
	}

	public void levelUp() {
		actorLevel++;

		// Adjust Stats on level up. I dunno how yet.

		Logger.debug(actorName + " is now level " + actorLevel);
	}

	public void useSkill(Skill skill, int expGained) {
		actorSkills[skill.ordinal()].addXp(expGained);
	}

	public String getActorName() {
		return actorName;
	}

	public String getActorDescription() {
		return actorDescription;
	}

	public String getActorClassId() {
		return actorClassId;
	}

	public String getActorRaceId() {
		return actorRaceId;
	}

	public boolean isActorEncumbered() {
		return actorCarryWeight < actorInventory.getInventoryWeight();
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

	public Stat<Attribute>[] getActorAttributes() {
		return actorAttributes;
	}

	public Stat<Skill>[] getActorSkills() {
		return actorSkills;
	}

	public double getActorCarryWeight() {
		return actorCarryWeight;
	}

	public Stat<Attribute> getActorAttribute(Attribute attribute) {
		return actorAttributes[attribute.ordinal()];
	}

	public Stat<Skill> getActorSkill(Skill skill) {
		return actorSkills[skill.ordinal()];
	}

	public ActorRace getActorCharacterRace() {
		return actorRace;
	}

	public ActorClass getActorCharacterClass() {
		return actorClass;
	}

	public Inventory getActorInventory() {
		return actorInventory;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public void setActorDescription(String actorDescription) {
		this.actorDescription = actorDescription;
	}

	public void setActorRace(ActorRace actorRace) {
		this.actorRace = actorRace;
	}

	public void setActorClass(ActorClass actorClass) {
		this.actorClass = actorClass;
	}

	public void setActorMaxHp(int actorMaxHp) {
		this.actorMaxHp = actorMaxHp;
	}

	public void setActorCurrentHp(int actorCurrentHp) {
		this.actorCurrentHp = actorCurrentHp;
	}

	public void setActorMaxMp(int actorMaxMp) {
		this.actorMaxMp = actorMaxMp;
	}

	public void setActorCurrentMp(int actorCurrentMp) {
		this.actorCurrentMp = actorCurrentMp;
	}

	public void setActorExp(int actorExp) {
		this.actorExp = actorExp;
	}

	public void setActorLevel(int actorLevel) {
		this.actorLevel = actorLevel;
	}

	public void setActorAttributes(Stat<Attribute>[] actorAttributes) {
		this.actorAttributes = actorAttributes;
	}

	public void setActorSkills(Stat<Skill>[] actorSkills) {
		this.actorSkills = actorSkills;
	}

	public void setActorInventory(Inventory actorInventory) {
		this.actorInventory = actorInventory;
	}

	public void setActorCarryWeight(double actorEncumbranceLimit) {
		this.actorCarryWeight = actorEncumbranceLimit;
	}

}
