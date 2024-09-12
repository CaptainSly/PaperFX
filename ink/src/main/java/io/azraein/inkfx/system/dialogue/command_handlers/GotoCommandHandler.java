package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class GotoCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {
        // Set the previousLineNumber to the currentLineNumber+1 to avoid recursively
        // going back
        parser.setCurrentLineNumber(parser.getCurrentLineNumber() + 1);
        parser.setPreviousLineNumber(parser.getCurrentLineNumber());

        String gotoBlockName = parser.getArgument(line, QuoteCommand.GOTO);

        if (parser.getFunctionMap().containsKey(gotoBlockName)) {
            parser.setCurrentLineNumber(parser.getFunctionMap().get(gotoBlockName).startLineNumber());
            parser.setInFunctionBlock(true);
        }
    }

}
