package io.azraein.paperfx.system;

public class Utils {

	public static String toNormalCase(String str) {
		if (str.contains("_"))
			str.replace("_", " ");

		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
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
