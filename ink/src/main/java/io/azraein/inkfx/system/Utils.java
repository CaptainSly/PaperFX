package io.azraein.inkfx.system;

import java.io.InputStream;

import io.azraein.inkfx.system.actors.ActorState;
import io.azraein.inkfx.system.actors.stats.Stat;

public class Utils {

	public static InputStream getFileFromResources(String fileName) {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		return classloader.getResourceAsStream(fileName);
	}

	public static String toNormalCase(String input) {
		if (input == null || input.isEmpty()) {
			return input; // Return the same if input is null or empty
		}

		// Replace underscores with spaces
		String replaced = input.replace("_", " ");

		// Split the string by spaces, capitalize each word, and join them back together
		String[] words = replaced.split(" ");
		StringBuilder result = new StringBuilder();

		for (String word : words) {
			if (!word.isEmpty()) {
				// Capitalize the first letter and append the rest of the word in lowercase
				result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase())
						.append(" ");
			}
		}

		// Trim the final string to remove any trailing space
		return result.toString().trim();
	}

	public static String getSuffix(int number) {
		if (number >= 11 && number <= 13) {
			return "th";
		} else {
			int lastDigit = number % 10;
			return switch (lastDigit) {
			case 1 -> number + "st";
			case 2 -> number + "nd";
			case 3 -> number + "rd";
			default -> number + "th";
			};
		}
	}

	public static float formatToDecimalPlace(int place, float number) {
		float scale = (float) Math.pow(10, place);
		return Math.round(number * scale) / scale;
	}

	public static float getCalendarUpdateInterval(int minutesPerDay) {
		float updateInterval = (float) ((minutesPerDay * 60) / 24) / 60;
		return Utils.formatToDecimalPlace(2, updateInterval);
	}

	public static float getAutoSaveInterval(int minutes) {
		float updateInterval = minutes * 60;
		return updateInterval;
	}

	/**
	 * Using the given stat, gets the totalXPForLevel based on it's baseExp and
	 * Exponent
	 * 
	 * @param stat
	 * @param targetLevel
	 * @return The Total amount exp for the level given based off a Stats level
	 *         growth
	 */
	public static int getTotalXPForLevel(Stat<?> stat, int targetLevel) {
		int totalXP = 0;
		for (int level = 1; level <= targetLevel; level++) {
			totalXP += (int) (stat.getBaseExp() * Math.pow(level, stat.getExponent()));
		}
		return totalXP;
	}

	/**
	 * Gets the totalXPForLevel based off the ActorState class's baseExp and
	 * Exponent
	 * 
	 * @param targetLevel
	 * @return The total amount exp for the level given based off an Actor level
	 *         growth.
	 */
	public static int getTotalXPForLevel(int targetLevel) {
		int totalXp = 0;
		for (int level = 1; level <= targetLevel; level++) {
			totalXp += (int) (ActorState.BASE_EXP * Math.pow(level, ActorState.EXPONENT));
		}

		return totalXp;
	}

	public static int getLevelFromXp(int givenXP, Stat<?> stat) {
		int level = 1;
		int totalXP = 0;

		while (true) {
			int xpForCurrentLevel = (int) (stat.getBaseExp() * Math.pow(level, stat.getExponent()));
			totalXP += xpForCurrentLevel;

			if (totalXP >= givenXP) {
				return level;
			}

			level++;
		}
	}

	public static int getLevelFromXP(int givenXP, int baseExp, double exponent) {
		int level = 1;
		int totalXP = 0;

		while (true) {
			int xpForCurrentLevel = (int) (baseExp * Math.pow(level, exponent));
			totalXP += xpForCurrentLevel;

			if (totalXP >= givenXP) {
				return level;
			}

			level++;
		}
	}

}
