package io.azraein.inkfx.system.actors;

import java.io.Serializable;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.stats.Attribute;
import io.azraein.inkfx.system.actors.stats.Skill;
import io.azraein.inkfx.system.actors.stats.Stat;
import io.azraein.inkfx.system.inventory.Inventory;
import io.azraein.inkfx.system.inventory.items.equipment.Equipment;
import io.azraein.inkfx.system.inventory.items.equipment.EquipmentSlot;

// TODO: Stat Implementation still needs work
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

	private String[] equipmentSlots;

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

		initializeAttributes();
		initializeSkills();

		equipmentSlots = new String[EquipmentSlot.values().length];

		actorCarryWeight = actorAttributes[Attribute.STRENGTH.ordinal()].getLevel() * 5;
		actorCurrentHp = actorMaxHp = (actorAttributes[Attribute.CONSTITUTION.ordinal()].getLevel() * 2);
		actorCurrentMp = actorMaxMp = (actorAttributes[Attribute.INTELLIGENCE.ordinal()].getLevel() * 2);
	}

	private void initializeAttributes() {
		for (Attribute attribute : Attribute.values()) {
			int baseExp = actorClass.getActorClassFavoredAttributes().contains(attribute) ? 250 : 300;
			double exponent = actorClass.getActorClassFavoredAttributes().contains(attribute) ? 1.623 : 1.763;
			Stat<Attribute> stat = new Stat<>(attribute, attribute.name(), baseExp, exponent);
			stat.addXp(Utils.getTotalXPForLevel(stat, actorRace.getActorRaceBaseAttribute(attribute)));
			actorAttributes[attribute.ordinal()] = stat;
		}
	}

	private void initializeSkills() {
		for (Skill skill : Skill.values()) {
			int baseExp = actorClass.getActorClassSkills().contains(skill) ? 185 : 225;
			double exponent = actorClass.getActorClassSkills().contains(skill) ? 1.572 : 1.648;
			Stat<Skill> stat = new Stat<>(skill, skill.name(), baseExp, exponent);
			int totalLevel = 0;

			if (actorClass.getActorClassSkills().contains(skill))
				totalLevel += ActorClass.CLASS_SKILL_BASE_LEVEL;

			totalLevel += actorRace.getActorRaceBaseSkill(skill);
			totalLevel += actorClass.getActorClassBaseSkills()[skill.ordinal()];

			stat.addXp(Utils.getTotalXPForLevel(stat, totalLevel));

			actorSkills[skill.ordinal()] = stat;
		}
	}

	public int getExpForNextLevel() {
		return (int) (BASE_EXP * Math.pow(actorLevel + 1, EXPONENT));
	}

	public void addXp(int xp) {
		actorExp += xp;
		while (actorExp >= getExpForNextLevel()) {
			actorExp -= getExpForNextLevel();
			levelUp();
		}
	}

	public ActorState(String actorName, ActorClass actorClass, ActorRace actorRace) {
		this(actorName, "", actorClass, actorRace);
	}

	public void levelUp() {
		actorLevel++;

		actorMaxHp += Math.ceil(actorAttributes[Attribute.CONSTITUTION.ordinal()].getLevel() / 10);
		actorMaxMp += Math.ceil(actorAttributes[Attribute.INTELLIGENCE.ordinal()].getLevel() / 10);
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

	public Equipment getEquipment(EquipmentSlot slot) {
		return Paper.DATABASE.getEquipment(equipmentSlots[slot.ordinal()]);
	}

	public String[] getEquipmentSlots() {
		return equipmentSlots;
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
		return Paper.DATABASE.getCharacterRace(actorRaceId);
	}

	public ActorClass getActorCharacterClass() {
		return Paper.DATABASE.getActorClass(actorClassId);
	}

	public Inventory getActorInventory() {
		return actorInventory;
	}

	public void setActorEquipment(Equipment equipment, EquipmentSlot slot) {
		equipmentSlots[slot.ordinal()] = equipment.getEquipmentId();
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
