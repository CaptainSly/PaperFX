package io.azraein.inkfx.system.dialogue.command_handlers;

import java.util.EnumMap;

import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.END;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.FUNC;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.GIVE_ITEM;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.GOTO;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.NPC;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.PLAY_SOUND;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.SCRIPT;
import static io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand.SHOW;

import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class CommandHandlerRegistry {

    private final EnumMap<QuoteCommand, CommandHandler> handlers = new EnumMap<>(QuoteCommand.class);

    public CommandHandlerRegistry() {
        handlers.put(END, new EndCommandHandler());
        handlers.put(FUNC, new FuncCommandHandler());
        handlers.put(GIVE_ITEM, new GiveItemCommandHandler());
        handlers.put(GOTO, new GotoCommandHandler());
        handlers.put(NPC, new NpcCommandHandler());
        handlers.put(PLAY_SOUND, new PlaySoundCommandHandler());
        handlers.put(SCRIPT, new ScriptCommandHandler());
        handlers.put(SHOW, new ShowCommandHandler());
    }

    public CommandHandler getHandler(QuoteCommand cmd) {
        return handlers.get(cmd);
    }

}
