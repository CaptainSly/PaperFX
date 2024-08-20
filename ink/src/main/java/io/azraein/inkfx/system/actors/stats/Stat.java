package io.azraein.inkfx.system.actors.stats;

import java.io.Serializable;

public class Stat<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T type;
	private String name;

	private int baseExp;
	private double exponent;

	private int currentExp;
	private int level;

	public Stat(T type, String name, int baseExp, double exponent) {
		this.type = type;
		this.name = name;
		this.baseExp = baseExp;
		this.exponent = exponent;
		this.currentExp = 0;
		this.level = 1;
	}

	public T getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getBaseExp() {
		return baseExp;
	}

	public double getExponent() {
		return exponent;
	}

	public int getCurrentExp() {
		return currentExp;
	}

	public int getLevel() {
		return level;
	}

	public int getExpForNextLevel() {
		return (int) (baseExp * Math.pow(level + 1, exponent));
	}

	public void addXp(int xp) {
		currentExp += xp;
		while (currentExp >= getExpForNextLevel()) {
			currentExp -= getExpForNextLevel();
			levelUp();
		}
	}

	public void levelUp() {
		level++;
	}

	public int getTotalXpForLevel(int targetLevel) {
		int totalXP = 0;
		for (int level = 1; level <= targetLevel; level++) {
			totalXP += (int) (baseExp * Math.pow(level, exponent));
		}
		return totalXP;
	}

	public void addXpForLevel(int targetLevel) {
		int totalXp = getTotalXpForLevel(targetLevel);
		addXp(totalXp - currentExp); // Adjust current XP to match the target level XP
	}
}
