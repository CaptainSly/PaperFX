package io.azraein.inkfx.system.dialogue;

import java.util.Map;

public interface DialogueReceiver {

    public void printDialogue(String dialogue);

    public int showChoices(Map<String, String> choices);

}
