package io.azraein.paperfx;

import java.util.List;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.dialogue.DialogueReciever;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.scripting.ScriptEngine;

public class TestMain implements DialogueReciever {

    public static void main(String[] args) {
        Paper.SE = new ScriptEngine();
        Paper.DATABASE = new Database();
        Paper.DATABASE.addGlobal("playerName", "Phil Collins");
        Logger.debug("Testing");
        String dialouge = """
                    FUNC test 
                        NPC "Hello there fuck face!"
                    END

                    NPC "Hello there $playerName! my name is Bob!"
                    NPC "I just got here, and boy! I already hate it!"

                    GOTO test

                    NPC "Yeah that's right, I just insulted you!"

                """;

        TestMain t = new TestMain();

        QuoteDialogueParser qdp = new QuoteDialogueParser(dialouge, t);
        qdp.executeScript();
    }

    @Override
    public void printDialogue(String dialogue) {
        System.out.println(dialogue);
    }

    @Override
    public void showChoices(List<String> choices) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showChoices'");
    }

}
