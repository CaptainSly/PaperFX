package io.azraein.inkfx.system.actors.dialogue;

import java.io.Serializable;

public class Topic implements Serializable {

    private static final long serialVersionUID = 1145689832182087231L;

    private String topicId;
    private String topicName;

    private String topicDialogue;

    private boolean isHidden;

    public Topic(String topicId, String topicName, String topicDialogue) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.topicDialogue = topicDialogue;

        isHidden = false;
    }

    public String getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicDialogue() {
        return topicDialogue;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTopicDialogue(String topicDialogue) {
        this.topicDialogue = topicDialogue;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

}
