package io.azraein.inkfx.system.dialogue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuoteChoice {

    private String choiceName;
    private QuoteBlock choiceBlock;

    private LinkedHashMap<String, String> choiceOptions;

    public QuoteChoice(String choiceName, QuoteBlock choiceBlock) {
        this.choiceName = choiceName;
        this.choiceBlock = choiceBlock;

        choiceOptions = new LinkedHashMap<>();

    }

    public String getChoiceName() {
        return choiceName;
    }

    public QuoteBlock getChoiceBlock() {
        return choiceBlock;
    }

    public Map<String, String> getChoiceOptions() {
        return choiceOptions;
    }

    public void setChoiceOptions(LinkedHashMap<String, String> choiceOptions) {
        this.choiceOptions = choiceOptions;
    }

}
