package io.azraein.inkfx.system.dialogue;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.exceptions.quote.MissingAssetException;
import io.azraein.inkfx.system.exceptions.quote.MissingFunctionException;
import io.azraein.inkfx.system.inventory.items.Item;
import io.azraein.inkfx.system.io.SaveSystem;

// TODO: Implement Conditionals and this should be about done, unless any other commands get added

public class QuoteDialogueParser {

    private String quoteScript;
    private int currentLineNumber;
    private int previousLineNumber;

    private boolean inFunctionBlock = false;
    private boolean inConditionBlock = false;

    private String[] scriptLines;

    private Stack<Boolean> conditionStack;

    private Map<String, QuoteBlock> functionMap;
    private Map<String, QuoteChoice> choiceMap;

    private DialogueReceiver dialogueReceiver;

    public static enum QuoteCommand {
        FUNC, CHOICE, IF, ELSE, ELSEIF, THEN, END, NPC, GIVE_ITEM, START_QUEST, ADV_QUEST, SCRIPT, PLAY_SOUND, GOTO,
        SHOW;

        public int getCmdNameLength() {
            return this.name().length();
        }

    }

    public QuoteDialogueParser(String quoteScript, DialogueReceiver dialogueReciever) {
        this.quoteScript = quoteScript;
        this.dialogueReceiver = dialogueReciever;

        functionMap = new LinkedHashMap<>();
        choiceMap = new LinkedHashMap<>();
        conditionStack = new Stack<>();

        scriptLines = this.quoteScript.split("\n");

        try {
            firstPass();
            secondPass();
        } catch (MissingFunctionException | MissingAssetException e) {
            e.printStackTrace();
        }

        currentLineNumber = 0;
    }

    public QuoteDialogueParser(DialogueReceiver dialogueReciever) {
        this.dialogueReceiver = dialogueReciever;

        functionMap = new LinkedHashMap<>();
        choiceMap = new LinkedHashMap<>();
        conditionStack = new Stack<>();
    }

    public void step() {
        Logger.debug("CL: " + currentLineNumber);
        if (currentLineNumber >= scriptLines.length)
            return;

        boolean isExecuting = true;

        String currentLine = scriptLines[currentLineNumber].trim();

        if (currentLine.startsWith("#")) {
            currentLineNumber++;
            return;
        } else if (isQuoteCommand(currentLine, QuoteCommand.NPC)) {
            String dialouge = getArgument(currentLine, QuoteCommand.NPC);
            dialouge = dialouge.replace("\"", "");
            dialouge = replaceVariables(dialouge);
            dialogueReceiver.printDialogue(dialouge);
        } else if (isQuoteCommand(currentLine, QuoteCommand.GOTO)) {
            // Set the previousLineNumber to the currentLineNumber+1 to avoid recursively
            // going back
            previousLineNumber = currentLineNumber + 2;

            String gotoBlockName = getArgument(currentLine, QuoteCommand.GOTO);

            if (functionMap.containsKey(gotoBlockName)) {
                currentLineNumber = functionMap.get(gotoBlockName).startLineNumber();
                inFunctionBlock = true;
            }

        } else if (isQuoteCommand(currentLine, QuoteCommand.SHOW)) {
            String choiceBlockName = getArgument(currentLine, QuoteCommand.SHOW);

            if (choiceMap.containsKey(choiceBlockName)) {
                QuoteChoice qChoice = choiceMap.get(choiceBlockName);
                int selectedChoiceIdx = dialogueReceiver.showChoices(qChoice.getChoiceOptions());

                String selectedFunc = (String) qChoice.getChoiceOptions().values().toArray()[selectedChoiceIdx];
                if (functionMap.containsKey(selectedFunc))
                    currentLineNumber = functionMap.get(selectedFunc).startLineNumber();

                return;
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
        } else if (isQuoteCommand(currentLine, QuoteCommand.GIVE_ITEM)) {
            String[] itemArgs = getArgument(currentLine, QuoteCommand.GIVE_ITEM).split(" ");

            String itemId = itemArgs[0];
            int itemAmt = Integer.parseInt(itemArgs[1]);
            Item item = Paper.DATABASE.getItem(itemId);
            Paper.PAPER_PLAYER_PROPERTY.get().getActorState().getActorInventory().addItem(item, itemAmt);
        } else if (isQuoteCommand(currentLine, QuoteCommand.PLAY_SOUND)) {
            String audioId = getArgument(currentLine, QuoteCommand.PLAY_SOUND);
            Paper.AUDIO.playSoundEffect(audioId);
        } else if (isQuoteCommand(currentLine, QuoteCommand.START_QUEST)) {
            // TODO: Implement once quests have been fully implemented
            String questId = getArgument(currentLine, QuoteCommand.START_QUEST);
            Paper.PAPER_PLAYER_PROPERTY.get().addQuestToLog(Paper.DATABASE.getQuest(questId));
        } else if (isQuoteCommand(currentLine, QuoteCommand.ADV_QUEST)) {
            String[] questArgs = getArgument(currentLine, QuoteCommand.ADV_QUEST).split(" ");

            int questStage = Integer.parseInt(questArgs[1]);
            Paper.DATABASE.getQuest(questArgs[0]).setQuestStage(questStage);
        } else if (isQuoteCommand(currentLine, QuoteCommand.FUNC)) {
            if (!inFunctionBlock) {
                // Skip over this and set the current line to the end of the function
                String funcName = getArgument(currentLine, QuoteCommand.FUNC);
                currentLineNumber = functionMap.get(funcName).closingLineNumber();
                return;
            }

        } else if (isQuoteCommand(currentLine, QuoteCommand.IF)) {
            String condition = getCondition(currentLine, QuoteCommand.IF);

        } else if (isQuoteCommand(currentLine, QuoteCommand.ELSEIF)) {
            String condition = getCondition(currentLine, QuoteCommand.ELSEIF);

        } else if (isQuoteCommand(currentLine, QuoteCommand.ELSE)) {

        } else if (isQuoteCommand(currentLine, QuoteCommand.END)) {

            if (inFunctionBlock && !inConditionBlock) {
                currentLineNumber = previousLineNumber;
                previousLineNumber = 0;
                inFunctionBlock = false;
                return;
            }

            if (inConditionBlock) {
                inConditionBlock = false;
            }

            previousLineNumber = 0;
        }

        currentLineNumber++;

    }

    private void firstPass() throws MissingFunctionException {

        // Function Stuff
        boolean inFunctionBlock = false;
        String functionBlockName = "";
        int functionStartLine = 0;
        int functionEndLine = 0;

        boolean inChoiceBlock = false;
        String choiceBlockName = "";
        int choiceStartLine = 0;
        int choiceEndLine = 0;

        LinkedHashMap<String, String> choiceOptions = new LinkedHashMap<>();

        while (currentLineNumber < scriptLines.length) {
            String currentLine = scriptLines[currentLineNumber].trim();

            if (inChoiceBlock) {
                Logger.debug("CL: " + currentLine);
                // Handle Choice Stuff If we're in a choice block
                Pattern pattern = Pattern.compile("\"([^\"]*)\",\\s*(\\w+)");
                Matcher matcher = pattern.matcher(currentLine);

                if (matcher.find()) {
                    String choiceLbl = matcher.group(1);
                    String choiceFunc = matcher.group(2);

                    choiceOptions.put(choiceLbl, choiceFunc);
                }
            }

            if (isQuoteCommand(currentLine, QuoteCommand.NPC)) {
                String npcDialogue = replaceVariables(
                        currentLine.substring(QuoteCommand.NPC.getCmdNameLength()).trim());
                scriptLines[currentLineNumber] = QuoteCommand.NPC.name() + " " + npcDialogue;
            } else if (isQuoteCommand(currentLine, QuoteCommand.FUNC)) {
                inFunctionBlock = true;
                functionStartLine = currentLineNumber++;
                functionBlockName = getArgument(currentLine, QuoteCommand.FUNC);
                continue;

            } else if (isQuoteCommand(currentLine, QuoteCommand.CHOICE)) {
                choiceOptions.clear();
                inChoiceBlock = true;
                choiceStartLine = currentLineNumber++;
                choiceBlockName = getArgument(currentLine, QuoteCommand.CHOICE);
                continue;

            } else if (isQuoteCommand(currentLine, QuoteCommand.END)) {

                if (inFunctionBlock && !inChoiceBlock) {
                    functionEndLine = currentLineNumber;
                    QuoteBlock qFunc = new QuoteBlock(functionStartLine, functionEndLine);

                    functionMap.put(functionBlockName, qFunc);
                    functionBlockName = "";
                    inFunctionBlock = false;
                }

                if (inChoiceBlock) {
                    choiceEndLine = currentLineNumber;
                    QuoteBlock qChoiceBlock = new QuoteBlock(choiceStartLine, choiceEndLine);
                    QuoteChoice qChoice = new QuoteChoice(choiceBlockName, qChoiceBlock);
                    qChoice.setChoiceOptions(choiceOptions);
                    choiceMap.put(choiceBlockName, qChoice);

                    choiceBlockName = "";
                    inChoiceBlock = false;
                }

            }

            currentLineNumber++;
        }

        currentLineNumber = 0;
    }

    private void secondPass() throws MissingFunctionException, MissingAssetException {

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
            } else if (isQuoteCommand(currentLine, QuoteCommand.START_QUEST)) {
                String questId = getArgument(currentLine, QuoteCommand.START_QUEST);

                if (!Paper.DATABASE.getQuestRegistry().containsKey(questId))
                    throw new MissingAssetException("Quest: " + questId + " does not exist within the Quest Registry");

            } else if (isQuoteCommand(currentLine, QuoteCommand.ADV_QUEST)) {
                String questId = getArgument(currentLine, QuoteCommand.ADV_QUEST).split(" ")[0];

                if (!Paper.DATABASE.getQuestRegistry().containsKey(questId))
                    throw new MissingAssetException("Quest: " + questId + " does not exist within the Quest Registry");

            } else if (isQuoteCommand(currentLine, QuoteCommand.PLAY_SOUND)) {
                String audioId = getArgument(currentLine, QuoteCommand.PLAY_SOUND);

                if (!new File(SaveSystem.PAPER_AUDIO_FOLDER + audioId).exists()) {
                    throw new MissingAssetException(
                            "PLAY_SOUND command references non-existant audio file: " + audioId);
                }
            } else if (isQuoteCommand(currentLine, QuoteCommand.GIVE_ITEM)) {
                String itemId = getArgument(currentLine, QuoteCommand.GIVE_ITEM);

                if (!Paper.DATABASE.getItemRegistry().containsKey(itemId)) {
                    throw new MissingAssetException("GIVE_ITEM command references non-existant Item: " + itemId
                            + ", check the ID and see if it's spelt correctly");
                }
            }

            currentLineNumber++;
        }

    }

    public void setScript(String script) {
        this.quoteScript = script;

        functionMap.clear();
        choiceMap.clear();
        conditionStack.clear();
        scriptLines = quoteScript.split("\n");

        try {
            firstPass();
            secondPass();
        } catch (MissingFunctionException | MissingAssetException e) {
            e.printStackTrace();
        }

        currentLineNumber = 0;
    }

    public String replaceVariables(String text) {
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

    public Object retrieveVariable(String variable) {
        if (variable.matches("\\$[a-zA-Z_][a-zA-Z0-9_]*")) {
            String variableKey = variable.substring(1);
            return Paper.DATABASE.getGlobal(variableKey);
        }

        return variable;
    }

    public String getArgument(String text, QuoteCommand command) {
        return text.substring(command.getCmdNameLength()).trim();
    }

    public String getCondition(String text, QuoteCommand conditionalCmd) {
        return text.substring(conditionalCmd.getCmdNameLength(), text.indexOf(QuoteCommand.THEN.name())).trim();
    }

    public boolean isQuoteCommand(String text, QuoteCommand command) {
        return text.startsWith(command.name());
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public String[] getScriptLines() {
        return scriptLines;
    }

    public int getPreviousLineNumber() {
        return previousLineNumber;
    }

    public Stack<Boolean> getConditionStack() {
        return conditionStack;
    }

    public Map<String, QuoteBlock> getFunctionMap() {
        return functionMap;
    }

    public boolean isInFunctionBlock() {
        return inFunctionBlock;
    }

    public boolean isInConditionBlock() {
        return inConditionBlock;
    }

    public Map<String, QuoteChoice> getChoiceMap() {
        return choiceMap;
    }

    public DialogueReceiver getDialogueReceiver() {
        return dialogueReceiver;
    }

    public void setCurrentLineNumber(int currentLineNumber) {
        this.currentLineNumber = currentLineNumber;
    }

    public void setPreviousLineNumber(int previousLineNumber) {
        this.previousLineNumber = previousLineNumber;
    }

    public void setInFunctionBlock(boolean inFunctionBlock) {
        this.inFunctionBlock = inFunctionBlock;
    }

    public void setInConditionBlock(boolean inConditionBlock) {
        this.inConditionBlock = inConditionBlock;
    }

}
