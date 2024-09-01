package io.azraein.inkfx.system.actors;

import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.dialogue.Topic;

public class Npc extends Actor {

	private static final long serialVersionUID = 2777091766514875234L;

	private List<String> npcDialogueTopics;

	public Npc(String actorId, String actorName, ActorClass charClass, ActorRace charRace) {
		super(actorId, "npc", actorName, charClass, charRace);
	}

	public Npc(String actorId, ActorState actorState) {
		super(actorId, "npc", actorState);
	}

	@Override
	public void initActor() {
		npcDialogueTopics = new ArrayList<>();
	}

	@Override
	public void updateActor() {
	}

	public void addDialogueTopic(Topic topic) {
		npcDialogueTopics.add(topic.getTopicId());
	}

	public List<String> getNpcDialogueTopics() {
		return npcDialogueTopics;
	}

}
