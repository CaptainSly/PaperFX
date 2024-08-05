package io.azraein.paperfx.system.actors.stats;

import java.io.Serializable;

import org.tinylog.Logger;

import io.azraein.paperfx.system.Utils;

public class Stat<T> implements Serializable {

	private static final long serialVersionUID = 8889867311180368598L;

	private String statName;
	private T stat;

	private int currentExp;
	private int level;

	private int baseExp;
	private double exponent;

	public Stat(T skill, String statName, int baseXp, double exponent) {
		this.stat = skill;
		this.statName = Utils.toNormalCase(statName);
		this.baseExp = baseXp;
		this.exponent = exponent;
		this.currentExp = 0;
		this.level = 1;
	}

	private int getXpForNextLevel() {
		return (int) (baseExp * Math.pow(level+1, exponent));
	}

	public void addXp(int xp) {
		currentExp += xp;
		while (currentExp >= getXpForNextLevel()) {
			currentExp -= getXpForNextLevel();
			levelUp();
		}
	}

	public void levelUp() {
		level++;
		Logger.debug("Stat: " + statName + " has leveled up to: " + level);
	}

	public T getStat() {
		return stat;
	}

	public int getBaseExp() {
		return baseExp;
	}

	public double getExponent() {
		return exponent;
	}

	public String getStatName() {
		return statName;
	}

	public int getCurrentXp() {
		return currentExp;
	}

	public int getLevel() {
		return level;
	}
}
