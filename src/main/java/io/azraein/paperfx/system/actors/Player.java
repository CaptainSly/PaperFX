package io.azraein.paperfx.system.actors;

import io.azraein.paperfx.system.actors.classes.CharacterClass;
import io.azraein.paperfx.system.actors.classes.CharacterRace;

public class Player extends Actor {

	private static final long serialVersionUID = -6782575409641873407L;

	public Player(CharacterClass charClass, CharacterRace charRace) {
		super("PLAYER_ACTOR", charClass, charRace);
	}

}
