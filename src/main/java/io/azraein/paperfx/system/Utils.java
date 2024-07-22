package io.azraein.paperfx.system;

public class Utils {

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
			switch (lastDigit) {
			case 1:
				return number + "st";
			case 2:
				return number + "nd";
			case 3:
				return number + "rd";
			default:
				return number + "th";
			}
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

	// Math Stuff

	public static int getTotalXPForLevel(int baseXp, double exponent, int targetLevel) {
		int totalXP = 0;
		for (int level = 1; level <= targetLevel; level++) {
			totalXP += (int) (baseXp * Math.pow(level, exponent));
		}
		return totalXP;
	}

}
