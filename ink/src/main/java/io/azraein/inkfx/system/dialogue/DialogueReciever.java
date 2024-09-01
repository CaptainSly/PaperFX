package io.azraein.inkfx.system.dialogue;

import java.util.List;

public interface DialogueReciever {

    public void printDialogue(String dialogue);

    public void showChoices(List<String> choices);

}
