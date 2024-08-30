package io.azraein.inkfx.system;

import java.io.Serializable;

public class Action implements Serializable {

    private static final long serialVersionUID = -3624655800064136884L;

    private String actionId;
    private String actionName;
    private String actionDescription;

    private String actionScript;

    private boolean isRepeated = false;

    private int actionDelay = 0;

    private transient float lastActionUpdate = 0;

    public Action(String actionId, String actionName, String actionDescription, String actionScript) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.actionDescription = actionDescription;
        this.actionScript = actionScript;
    }

    public void onAction() {
        Paper.SE.runFunction(actionScript, "onAction");
    }

    public void onRepeatedAction(float delta) {

        lastActionUpdate++;
        if (lastActionUpdate >= actionDelay) {
            Paper.SE.runFunction(actionScript, "onRepeatedAction");
            lastActionUpdate = 0;
        }

    }

    public String getActionId() {
        return actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public String getActionScript() {
        return actionScript;
    }

}
