package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.dialogue.QuoteChoice;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class ShowCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {

        String choiceBlockName = parser.getArgument(line, QuoteCommand.SHOW);

        if (parser.getChoiceMap().containsKey(choiceBlockName)) {
            QuoteChoice qChoice = parser.getChoiceMap().get(choiceBlockName);
            int selectedChoiceIdx = parser.getDialogueReceiver().showChoices(qChoice.getChoiceOptions());

            String selectedFunc = (String) qChoice.getChoiceOptions().values().toArray()[selectedChoiceIdx];
            if (parser.getFunctionMap().containsKey(selectedFunc))
                parser.setCurrentLineNumber(parser.getFunctionMap().get(selectedFunc).startLineNumber());

            return;
        }

    }

}
