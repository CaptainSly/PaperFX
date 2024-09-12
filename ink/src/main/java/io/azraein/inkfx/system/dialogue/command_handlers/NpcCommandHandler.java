package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class NpcCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {
        String dialouge = parser.getArgument(line, QuoteCommand.NPC);
        dialouge = dialouge.replace("\"", "");
        dialouge = parser.replaceVariables(dialouge);
        parser.getDialogueReceiver().printDialogue(dialouge);
    }

}
