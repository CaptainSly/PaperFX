package io.azraein.inkfx.system.dialogue.command_handlers;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class PlaySoundCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {
        String audioId = parser.getArgument(line, QuoteCommand.PLAY_SOUND);
        Paper.AUDIO.playSoundEffect(audioId);
    }

}
