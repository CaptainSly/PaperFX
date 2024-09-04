package io.azraein.inkfx.system.dialogue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.exceptions.MissingFunctionException;

public class QuoteDialogueParser {

    private String quoteScript;
    private int currentLineNumber;
    private int previousLineNumber;

    private String[] scriptLines;

    private Map<String, QuoteBlock> functionMap;
    private Map<String, QuoteChoice> choiceMap;

    private DialogueReciever dialogueReciever;

    public static enum QuoteCommand {
        FUNC, CHOICE, IF, ELSE, ELSEIF, THEN, END, AND, OR, NOT, NPC, GIVE_ITEM, START_QUEST, ADV_QUEST, SCRIPT,
        PLAY_SOUND, GOTO, SHOW;

        public int getCmdNameLength() {
            return this.name().length();
        }

    }

    public QuoteDialogueParser(String quoteScript, DialogueReciever dialogueReciever) {
        this.quoteScript = quoteScript;
        this.dialogueReciever = dialogueReciever;

        functionMap = new HashMap<>();
        choiceMap = new HashMap<>();

        scriptLines = this.quoteScript.split("\n");

        try {
            firstPass();
            secondPass();
        } catch (MissingFunctionException e) {
            e.printStackTrace();
        }

        currentLineNumber = 0;
    }

    public void executeScript() {
        boolean inFunctionBlock = false;

        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (currentLine.startsWith("#")) {
                currentLineNumber++;
                continue;

            } else if (isQuoteCommand(currentLine, QuoteCommand.NPC)) {
                String dialouge = getArgument(currentLine, QuoteCommand.NPC);
                dialouge = dialouge.replace("\"", "");
                dialouge = replaceVariables(dialouge);
                dialogueReciever.printDialogue(dialouge);

            } else if (isQuoteCommand(currentLine, QuoteCommand.GOTO)) {
                // Set the previousLineNumber to the currentLineNumber+1 to avoid recursively
                // going back
                previousLineNumber = currentLineNumber++;

                String gotoBlockName = getArgument(currentLine, QuoteCommand.GOTO);

                if (functionMap.containsKey(gotoBlockName)) {
                    currentLineNumber = functionMap.get(gotoBlockName).startLineNumber();
                    inFunctionBlock = true;
                }

                continue;

            } else if (isQuoteCommand(currentLine, QuoteCommand.SHOW)) {
                String choiceBlockName = getArgument(currentLine, QuoteCommand.SHOW);

                if (choiceMap.containsKey(choiceBlockName)) {
                    QuoteChoice qChoice = choiceMap.get(choiceBlockName);
                    int selectedChoiceIdx = dialogueReciever.showChoices(qChoice.getChoiceOptions());

                    String selectedFunc = (String) qChoice.getChoiceOptions().values().toArray()[selectedChoiceIdx];
                    if (functionMap.containsKey(selectedFunc))
                        currentLineNumber = functionMap.get(selectedFunc).startLineNumber();

                    continue;
                }

            } else if (isQuoteCommand(currentLine, QuoteCommand.SCRIPT)) {
                String scriptCmdLine = getArgument(currentLine, QuoteCommand.SCRIPT);
                String[] scriptArgs = scriptCmdLine.split(" ");

                if (scriptArgs.length >= 2) {
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
                    continue;
                }

            } else if (isQuoteCommand(currentLine, QuoteCommand.END)) {

                if (inFunctionBlock) {
                    currentLineNumber = previousLineNumber;
                    previousLineNumber = 0;
                    inFunctionBlock = false;
                }

                previousLineNumber = 0;
            }

            currentLineNumber++;
        }

    }

    private void firstPass() throws MissingFunctionException {

        boolean inFunctionBlock = false;

        // Function Stuff
        String functionBlockName = "";
        int functionStartLine = 0;
        int functionEndLine = 0;

        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (isQuoteCommand(currentLine, QuoteCommand.NPC)) {
                String npcDialogue = replaceVariables(
                        currentLine.substring(QuoteCommand.NPC.getCmdNameLength()).trim());
                scriptLines[currentLineNumber] = QuoteCommand.NPC.name() + " " + npcDialogue;
            } else if (isQuoteCommand(currentLine, QuoteCommand.FUNC)) {
                inFunctionBlock = true;
                functionStartLine = currentLineNumber++;
                functionBlockName = getArgument(currentLine, QuoteCommand.FUNC);

            } else if (isQuoteCommand(currentLine, QuoteCommand.CHOICE)) {
                String choiceName = getArgument(currentLine, QuoteCommand.CHOICE);
                int choiceStartLine = currentLineNumber;
                int choiceEndLine = currentLineNumber++;

                LinkedHashMap<String, String> choiceOptions = new LinkedHashMap<>();
                while (choiceEndLine < scriptLines.length) {

                    String curChoiceLine = scriptLines[choiceEndLine].trim();

                    if (isQuoteCommand(curChoiceLine, QuoteCommand.END))
                        break;
                    else if (isQuoteCommand(curChoiceLine, QuoteCommand.CHOICE)) {
                        choiceEndLine++;
                        continue;
                    } else {
                        Pattern pattern = Pattern.compile("\"([^\"]*)\",\\s*(\\w+)");
                        Matcher matcher = pattern.matcher(curChoiceLine);

                        if (matcher.find()) {
                            String choiceLbl = matcher.group(1);
                            String choiceFunc = matcher.group(2);

                            choiceOptions.put(choiceLbl, choiceFunc);
                        }

                    }

                    choiceEndLine++;
                }

                QuoteBlock qCBlock = new QuoteBlock(choiceStartLine, choiceEndLine);
                QuoteChoice qChoice = new QuoteChoice(choiceName, qCBlock);
                qChoice.setChoiceOptions(choiceOptions);

                choiceMap.put(choiceName, qChoice);

            } else if (isQuoteCommand(currentLine, QuoteCommand.END)) {

                if (inFunctionBlock) {
                    functionEndLine = currentLineNumber;
                    QuoteBlock qFunc = new QuoteBlock(functionStartLine, functionEndLine);

                    functionMap.put(functionBlockName, qFunc);
                    functionBlockName = "";
                    inFunctionBlock = false;
                }

            }

            currentLineNumber++;
        }

        currentLineNumber = 0;
    }

    private void secondPass() throws MissingFunctionException {

        // Validation Pass

        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (isQuoteCommand(currentLine, QuoteCommand.GOTO)) {
                String gotoBlockName = getArgument(currentLine, QuoteCommand.GOTO);
                if (!functionMap.containsKey(gotoBlockName)) {
                    throw new MissingFunctionException("GOTO on line: " + (currentLineNumber + 1)
                            + " references missing function: " + gotoBlockName);
                }
            } else if (isQuoteCommand(currentLine, QuoteCommand.SHOW)) {
                String showBlockName = getArgument(currentLine, QuoteCommand.SHOW);
                if (!choiceMap.containsKey(showBlockName)) {
                    throw new MissingFunctionException("SHOW on line: " + (currentLineNumber + 1)
                            + " references missing choice block: " + showBlockName);
                }
            }

            currentLineNumber++;
        }

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

        return variable;
    }

    private String getArgument(String text, QuoteCommand command) {
        return text.substring(command.getCmdNameLength()).trim();
    }

    private boolean isQuoteCommand(String text, QuoteCommand command) {
        return text.startsWith(command.name());
    }

}
