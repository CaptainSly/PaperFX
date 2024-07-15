package io.azraein.paperfx.system.actors;

import io.azraein.paperfx.system.actors.classes.CharacterClass;
import io.azraein.paperfx.system.actors.classes.CharacterRace;

public class Npc extends Actor {

	private static final long serialVersionUID = 2777091766514875234L;

	public Npc(String actorId, CharacterClass charClass, CharacterRace charRace) {
		super(actorId, charClass, charRace);
	}

}
