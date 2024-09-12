package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;
import io.azraein.inkfx.system.inventory.items.Item;

public class GiveItemCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {
        String[] itemArgs = parser.getArgument(line, QuoteCommand.GIVE_ITEM).split(" ");

        String itemId = itemArgs[0];
        int itemAmt = Integer.parseInt(itemArgs[1]);
        Item item = Paper.DATABASE.getItem(itemId);
        Paper.PAPER_PLAYER_PROPERTY.get().getActorState().getActorInventory().addItem(item, itemAmt);
    }

}
