package io.azraein.inkfx.system.actors.stats;

import static io.azraein.inkfx.system.actors.stats.Attribute.*;

public enum Skill {

	// Combat
	ONE_HANDED(STRENGTH), TWO_HANDED(STRENGTH), UNARMED(STRENGTH),
	MARKSMAN(DEXTERITY), BLOCK(CONSTITUTION), HEAVY_ARMOR(CONSTITUTION),
	LIGHT_ARMOR(DEXTERITY), UNARMORED(DEXTERITY),

	// Magic
	ABJURATION(WISDOM), CONJURATION(CHARISMA), 
	DIVINATION(WISDOM), ENCHATMENT(CHARISMA), EVOCATION(INTELLIGENCE), 
	ILLUSION(INTELLIGENCE), NECROMANCY(WISDOM), ALCHEMY(INTELLIGENCE),

	// Stealth Skills
	STEALTH(DEXTERITY), LOCKPICKING(DEXTERITY),

	// Crafting Skills
	SMITHING(INTELLIGENCE), ARMORER(WISDOM), CRAFTING(CONSTITUTION),

	// Social Skills
	SPEECH(CHARISMA), BARTER(CHARISMA),

	;
	
	private Attribute attribute;
	
	Skill(Attribute attribute) {
		this.attribute = attribute;
	}

	public Attribute getSkillAttribute() {
		return attribute;
	}

}
