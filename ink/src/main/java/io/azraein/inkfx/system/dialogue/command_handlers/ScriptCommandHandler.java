package io.azraein.inkfx.system.dialogue.command_handlers;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser.QuoteCommand;

public class ScriptCommandHandler implements CommandHandler {

    @Override
    public void handle(String line, QuoteDialogueParser parser) {

        String scriptCmdLine = parser.getArgument(line, QuoteCommand.SCRIPT);
        String[] scriptArgs = scriptCmdLine.split(" ");

        if (scriptArgs.length >= 2) {
            String scriptId = scriptArgs[0];
            String scriptFunc = scriptArgs[1];
            Object scriptArg = parser.retrieveVariable(scriptArgs[2]);

            Paper.SE.runFunction(scriptId, scriptFunc, CoerceJavaToLua.coerce(scriptArg));
        } else {
            Paper.SE.runScript(scriptArgs[0]);
        }

    }

}
