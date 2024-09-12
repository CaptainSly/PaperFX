package io.azraein;

import java.util.Map;
import java.util.Scanner;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.dialogue.DialogueReceiver;
import io.azraein.inkfx.system.dialogue.QuoteDialogueParser;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.SaveSystem;

public class ConsoleDialogueReciever implements DialogueReceiver {

    public static Scanner scanner;

    public static void main(String[] args) {
        Logger.debug("Quack");
        scanner = new Scanner(System.in);
        
        Paper.DATABASE = new Database();
        Paper.DATABASE.addGlobal("playerName", "Phil Collins");
        Paper.DATABASE.addGlobal("hasAmulet", false);
        Logger.debug("Quack");
        
        ConsoleDialogueReciever cdr = new ConsoleDialogueReciever();
        String dialogue = Utils.getFileAsString(SaveSystem.PAPER_FOLDER + "testDialogue.peqf");
        Logger.debug("Quack");
        
        QuoteDialogueParser qdp = new QuoteDialogueParser(dialogue, cdr);
        
        Logger.debug("Quack");
        while (qdp.getCurrentLineNumber() < qdp.getScriptLines().length) {
            qdp.step();
        }
        Logger.debug("Quack");

        scanner.close();
    }

    @Override
    public void printDialogue(String dialogue) {
        System.out.println(dialogue);
    }

    @Override
    public int showChoices(Map<String, String> choices) {
        int index = 1;

        System.out.println("Please select an option:");

        for (String choice : choices.keySet()) {
            System.out.println(index + ": " + choice);
            index++;
        }

        int selectedChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Ensure the selected choice is valid
        while (selectedChoice < 1 || selectedChoice > choices.size()) {
            System.out.println("Invalid choice. Please select a valid option:");
            selectedChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }

        // Return the index (1-based) of the selected choice
        return selectedChoice - 1;
    }

}
