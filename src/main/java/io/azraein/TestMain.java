package io.azraein;

import org.tinylog.Logger;

import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Stat;

public class TestMain {

    public static void main(String[] args) {
        testLevelUpStuff();
    }

    private static void testLevelUpStuff() {
        int baseExp = 185;
        double exponent = 1.572;
        int targetLevel = 40;

        Stat<Attribute> attr = new Stat<>(Attribute.STRENGTH, Attribute.STRENGTH.name(), baseExp, exponent);

        int totalXP = 0;
        for (int level = 1; level <= targetLevel; level++) {
            totalXP += (int) (baseExp * Math.pow(level, exponent));
        }

        attr.addXp(totalXP);

        System.out.println("Total XP to reach level " + targetLevel + ": " + totalXP);

        Logger.debug("Level for XP Amount: " + totalXP + " is: " + getLevelFromXP(totalXP, baseExp, exponent));
        Logger.debug("Stat CurrentEXP: " + attr.getCurrentExp());
    }

    private static int getLevelFromXP(int givenXP, int baseExp, double exponent) {
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
