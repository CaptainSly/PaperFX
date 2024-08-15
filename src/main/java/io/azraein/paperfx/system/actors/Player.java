package io.azraein.paperfx.system.actors;

import java.util.ArrayList;
import java.util.List;

import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.quest.Quest;

public class Player extends Actor {

	private static final long serialVersionUID = -6782575409641873407L;

	private List<String> playerQuestLog;
	private List<String> playerCompletedQuests;

	public Player(String actorName, ActorClass charClass, ActorRace charRace) {
		super("PLAYER_ACTOR", "player", actorName, charClass, charRace);
		playerQuestLog = new ArrayList<>();
		playerCompletedQuests = new ArrayList<>();

	}

	@Override
	public void updateActor() {
	}

	public void addQuestToLog(Quest quest) {
		playerQuestLog.add(quest.getQuestId());
	}

	public List<String> getPlayerQuestLog() {
		return playerQuestLog;
	}

	public List<String> getPlayerCompeltedQuests() {
		return playerCompletedQuests;
	}

}
