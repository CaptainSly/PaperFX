package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;

public class EndCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {

        if (parser.isInFunctionBlock() && !parser.isInConditionBlock()) {
            parser.setCurrentLineNumber(parser.getPreviousLineNumber());
            parser.setPreviousLineNumber(0);
            parser.setInFunctionBlock(false);
            return;
        }

        if (parser.isInConditionBlock()) {
            parser.setInConditionBlock(false);
        }

        parser.setPreviousLineNumber(0);

    }

}
