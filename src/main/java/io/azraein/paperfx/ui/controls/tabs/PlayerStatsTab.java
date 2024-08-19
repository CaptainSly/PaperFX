package io.azraein.paperfx.ui.controls.tabs;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import io.azraein.paperfx.system.actors.stats.Stat;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class PlayerStatsTab extends JournalTab {

    private TextField[] attributeFlds;
    private TextField[] skillFlds;

    private Label currentHealthLbl, currentManaLbl, currentLevelLbl, currentExpLbl;

    public PlayerStatsTab() {
        super("Abilities and Stats");
        ActorState playerState = Paper.PAPER_PLAYER_PROPERTY.get().getActorState();

        Label[] attributeLbls = new Label[Attribute.values().length];
        Label[] skillLbls = new Label[Skill.values().length];

        Label actorClassLbl = new Label("Class: " + playerState.getActorCharacterClass().getActorClassName());
        actorClassLbl.setTooltip(new Tooltip(playerState.getActorCharacterClass().getActorClassDescription()));

        Label actorRaceLbl = new Label("Race: " + playerState.getActorCharacterRace().getActorRaceName());
        actorRaceLbl.setTooltip(new Tooltip(playerState.getActorCharacterRace().getActorRaceDescription()));

        currentLevelLbl = new Label();
        currentExpLbl = new Label();
        currentHealthLbl = new Label();
        currentManaLbl = new Label();

        attributeFlds = new TextField[Attribute.values().length];
        skillFlds = new TextField[Skill.values().length];

        for (Attribute attr : Attribute.values()) {
            attributeLbls[attr.ordinal()] = new Label(Utils.toNormalCase(attr.name()));
            attributeFlds[attr.ordinal()] = new TextField();
            attributeFlds[attr.ordinal()].setEditable(false);
            attributeFlds[attr.ordinal()].setMaxWidth(85);
        }

        for (Skill skill : Skill.values()) {
            skillLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()));
            skillFlds[skill.ordinal()] = new TextField();
            skillFlds[skill.ordinal()].setEditable(false);
            skillFlds[skill.ordinal()].setMaxWidth(85);
        }

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));

        // Player Name, Race, and Class
        gp.add(new Label("Name: " + playerState.getActorName()), 0, 0);
        gp.add(actorRaceLbl, 0, 1);
        gp.add(actorClassLbl, 0, 2);

        // Health and Mana
        gp.add(currentHealthLbl, 1, 0);
        gp.add(currentManaLbl, 1, 1);

        // Level and Experience
        gp.add(currentLevelLbl, 1, 2);
        gp.add(currentExpLbl, 2, 2);

        // Player Attributes and Skills
        for (int i = 0; i < Attribute.values().length; i++) {
            gp.add(attributeLbls[i], 0, i + 3);
            gp.add(attributeFlds[i], 1, i + 3);
        }

        int numCols = 3;
        int numRows = (int) Math.ceil((double) Skill.values().length / numCols);
        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (index < Skill.values().length) {
                    int curCol = 3 + col;
                    int curRow = 3 + row;
                    gp.add(skillLbls[index], curCol * 2, curRow);
                    gp.add(skillFlds[index], curCol * 2 + 1, curRow);
                    index++;
                }
            }
        }

        setContent(gp);
    }

    @Override
    public void updateTab() {
        ActorState playerState = Paper.PAPER_PLAYER_PROPERTY.get().getActorState();

        currentHealthLbl.setText("HP: " + playerState.getActorCurrentHp() + "/" + playerState.getActorMaxHp());
        currentManaLbl.setText("MP: " + playerState.getActorCurrentMp() + "/" + playerState.getActorMaxMp());

        int expForNextLevel = Utils.getTotalXPForLevel(playerState.getActorLevel() + 1);

        currentLevelLbl.setText("LEVEL: " + playerState.getActorLevel());
        currentExpLbl.setText("EXP: " + playerState.getActorExp() + "/" + expForNextLevel);

        for (Skill skill : Skill.values()) {
            Stat<Skill> stat = playerState.getActorSkill(skill);
            skillFlds[skill.ordinal()].setText("Lvl: " + stat.getLevel());
        }

        for (Attribute attr : Attribute.values()) {
            Stat<Attribute> stat = playerState.getActorAttribute(attr);
            attributeFlds[attr.ordinal()].setText("Lvl: " + stat.getLevel());
        }
    }

}
