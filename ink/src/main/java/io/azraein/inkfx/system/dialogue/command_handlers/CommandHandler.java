package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;

public interface CommandHandler {

    void handle(String line, QuoteDialogueParser parser);

}
