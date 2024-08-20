package io.azraein.inkfx.system.quest;

import java.io.Serializable;

// TODO: Finish working on the Quest Implementation

public class Quest implements Serializable {

    private static final long serialVersionUID = 8053839905933722791L;

    private final String questId;
    private final String questName;
    private final String questDescription;

    private String questScript;

    private int questStage;

    public Quest(String questId, String questName, String questDescription) {
        this.questId = questId;
        this.questName = questName;
        this.questDescription = questDescription;

        questStage = 0;
    }

    public String getQuestId() {
        return questId;
    }

    public String getQuestName() {
        return questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public String getQuestScript() {
        return questScript;
    }

    public int getQuestStage() {
        return questStage;
    }

    public void setQuestScript(String questScript) {
        this.questScript = questScript;
    }

    public void setQuestStage(int questStage) {
        this.questStage = questStage;
    }

}
