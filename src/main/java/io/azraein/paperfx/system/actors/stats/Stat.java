package io.azraein.paperfx.system.actors.stats;

import java.io.Serializable;

import org.tinylog.Logger;

import io.azraein.paperfx.system.Utils;

public class Stat implements Serializable {

	private static final long serialVersionUID = 8889867311180368598L;

	private String statName;
	private Skill skill;

	private int currentXp;
	private int level;

	public static final transient int BASE_EXP = 150;
	public static final transient double EXPONENT = 1.660;

	public Stat(Skill skill, int baseXp, double exponent) {
		this.skill = skill;
		this.statName = Utils.toNormalCase(skill.name());
		this.currentXp = 0;
		this.level = 1;
	}

	private int getXpForNextLevel() {
		return (int) (BASE_EXP * Math.pow(level, EXPONENT));
	}

	public void addXp(int xp) {
		currentXp += xp;
		while (currentXp >= getXpForNextLevel()) {
			currentXp -= getXpForNextLevel();
			levelUp();
		}
	}

	public void levelUp() {
		level++;
		Logger.debug("Stat: " + statName + " has leveled up to: " + level);
	}

	public Skill getSkill() {
		return skill;
	}

	public String getStatName() {
		return statName;
	}

	public int getCurrentXp() {
		return currentXp;
	}

	public int getLevel() {
		return level;
	}
}
