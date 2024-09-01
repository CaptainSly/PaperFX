package io.azraein.inkfx.system.quest;

import java.io.Serializable;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;

// TODO: Finish working on the Quest Implementation

public class Quest implements Serializable {

    private static final long serialVersionUID = 8053839905933722791L;

    private final String questId;
    private final String questName;
    private final String questDescription;

    private String questScript;

    private int questStage;

    private boolean isQuestComplete;

    public Quest(String questId, String questName, String questDescription) {
        this.questId = questId;
        this.questName = questName;
        this.questDescription = questDescription;

        questStage = 0;
        isQuestComplete = false;
    }

    public void onQuestStart() {
        if (questScript != null) {
            if (!questScript.isEmpty()) {
                Paper.SE.runFunction(questScript, "onQuestStart", CoerceJavaToLua.coerce(this));
            }
        }
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

    public boolean isQuestComplete() {
        return isQuestComplete;
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

    public void setQuestComplete(boolean isComplete) {
        this.isQuestComplete = isComplete;
    }

}
