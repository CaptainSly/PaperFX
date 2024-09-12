package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class FuncCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {
        if (!parser.isInFunctionBlock()) {
            // Skip over this and set the current line to the end of the function
            String funcName = parser.getArgument(line, QuoteCommand.FUNC);
            parser.setCurrentLineNumber(parser.getFunctionMap().get(funcName).closingLineNumber());
            return;
        }

    }

}
