package io.azraein.inkfx.system.dialogue;

import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;

public class QuoteDialogueParser {

    private String quoteScript;
    private int currentLineNumber;

    private String[] scriptLines;

    private Map<String, QuoteBlock> functionMap;

    private DialogueReciever dialogueReciever;

    public static enum QuoteCommand {
        FUNC, CHOICE, IF, ELSE, ELSEIF, THEN, END, AND, OR, NOT, NPC, GIVE_ITEM, START_QUEST, ADV_QUEST, SCRIPT,
        PLAY_SOUND, GOTO,;

        public int getCmdNameLength() {
            return this.name().length();
        }

    }

    public QuoteDialogueParser(String quoteScript, DialogueReciever dialogueReciever) {
        this.quoteScript = quoteScript;
        this.dialogueReciever = dialogueReciever;

        functionMap = new HashMap<>();

        scriptLines = this.quoteScript.split("\n");

        firstPass();

        currentLineNumber = 0;
    }

    public void executeScript() {
        boolean inFunctionBlock = false;

        int previousLineNumber = 0;

        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (isQuoteCommand(currentLine, QuoteCommand.NPC)) {
                String dialouge = getArgument(currentLine, QuoteCommand.NPC);
                dialouge = dialouge.replace("\"", "");
                dialogueReciever.printDialogue(dialouge);
            } else if (isQuoteCommand(currentLine, QuoteCommand.GOTO)) {
                // Set the previousLineNumber to the currentLineNumber+1 to avoid recursively
                // going back
                previousLineNumber = currentLineNumber++;
                String funcName = getArgument(currentLine, QuoteCommand.GOTO);
                currentLineNumber = functionMap.get(funcName).startLineNumber();
                inFunctionBlock = true;
                continue;
            } else if (isQuoteCommand(currentLine, QuoteCommand.SCRIPT)) {
                String scriptCmdLine = getArgument(currentLine, QuoteCommand.SCRIPT);
                String[] scriptArgs = scriptCmdLine.split(" ");

                if (scriptArgs.length > 2) {
                    // Supports scriptId and ONE argument for now. The argument can be a global
                    // variable
                    String scriptId = scriptArgs[0];
                    String scriptFunc = scriptArgs[1];
                    Object scriptArg = retrieveVariable(scriptArgs[2]);

                    Paper.SE.runFunction(scriptId, scriptFunc, CoerceJavaToLua.coerce(scriptArg));
                } else {
                    Paper.SE.runScript(scriptArgs[0]);
                }

            } else if (isQuoteCommand(currentLine, QuoteCommand.FUNC)) {
                if (!inFunctionBlock) {
                    // Skip over this and set the current line to the end of the function
                    String funcName = getArgument(currentLine, QuoteCommand.FUNC);
                    currentLineNumber = functionMap.get(funcName).closingLineNumber();
                }

            } else if (isQuoteCommand(currentLine, QuoteCommand.END)) {
                if (!inFunctionBlock) {
                    continue;
                }

                currentLineNumber = previousLineNumber;
                previousLineNumber = 0;
                inFunctionBlock = false;
            }

            currentLineNumber++;
        }

    }

    private void firstPass() {

        boolean inFunctionBlock = false;

        // Function Stuff
        String functionName = "";
        int startLine = 0;
        int endLine = 0;

        // Replace Variables inside NPC calls and map functions to line numbers
        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (currentLine.startsWith(QuoteCommand.NPC.name())) {
                String npcDialogue = replaceVariables(
                        currentLine.substring(QuoteCommand.NPC.getCmdNameLength()).trim());
                scriptLines[currentLineNumber] = QuoteCommand.NPC.name() + " " + npcDialogue;
            } else if (currentLine.startsWith(QuoteCommand.FUNC.name())) {
                inFunctionBlock = true;
                startLine = currentLineNumber++;
                functionName = currentLine.substring(QuoteCommand.FUNC.getCmdNameLength()).trim();

            } else if (currentLine.startsWith(QuoteCommand.END.name())) {

                if (inFunctionBlock) {
                    endLine = currentLineNumber;
                    QuoteBlock qFunc = new QuoteBlock(startLine, endLine);

                    functionMap.put(functionName, qFunc);
                    functionName = "";
                    inFunctionBlock = false;
                }

            }
            currentLineNumber++;
        }

        currentLineNumber = 0;
    }

    private String replaceVariables(String text) {
        String[] tokens = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            // Extract any trailing punctuation
            String punctuation = "";
            while (!token.isEmpty() && !Character.isLetterOrDigit(token.charAt(token.length() - 1))) {
                punctuation = token.charAt(token.length() - 1) + punctuation;
                token = token.substring(0, token.length() - 1);
            }

            // Check if the remaining token is a variable
            if (token.matches("\\$[a-zA-Z_][a-zA-Z0-9_]*")) {
                String variableKey = token.substring(1);
                String replacement = Paper.DATABASE.getGlobal(variableKey).toString();
                result.append(replacement).append(punctuation).append(" ");
            } else {
                result.append(token).append(punctuation).append(" ");
            }
        }

        return result.toString().trim();
    }

    private Object retrieveVariable(String variable) {
        if (variable.matches("\\$[a-zA-Z_][a-zA-Z0-9_]*")) {
            String variableKey = variable.substring(1);
            return Paper.DATABASE.getGlobal(variableKey);
        }

        return null;
    }

    private String getArgument(String text, QuoteCommand command) {
        return text.substring(command.getCmdNameLength()).trim();
    }

    private boolean isQuoteCommand(String text, QuoteCommand command) {
        return text.startsWith(command.name());
    }

}
