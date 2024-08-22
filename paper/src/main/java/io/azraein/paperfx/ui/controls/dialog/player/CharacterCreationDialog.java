package io.azraein.paperfx.ui.controls.dialog.player;

import java.util.Optional;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.Player;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.stats.Attribute;
import io.azraein.inkfx.system.actors.stats.Skill;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CharacterCreationDialog extends Dialog<Player> {

    private TextField playerNameFld;
    private ComboBox<ActorRace> playerRaceBox;
    private ComboBox<ActorClass> playerClassBox;

    private TextArea playerClassDescription;
    private TextArea playerRaceDescription;

    private TextField playerFavoredAttribute1, playerFavoredAttribute2;

    private ListView<Skill> playerClassSkillLV;

    public CharacterCreationDialog() {
        setTitle("Create Character");
        setHeaderText("Design your character");

        playerNameFld = new TextField();

        playerRaceBox = new ComboBox<>();
        playerRaceBox.getItems().addAll(Paper.DATABASE.getActorRaceRegistry().values());
        playerRaceBox.setConverter(new StringConverter<ActorRace>() {

            @Override
            public String toString(ActorRace object) {
                if (object != null) {
                    return object.getActorRaceName();
                }

                return null;
            }

            @Override
            public ActorRace fromString(String string) {
                return Paper.DATABASE.getCharacterRace(string);
            }
        });
        playerRaceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                playerRaceDescription.setText(newValue.getActorRaceDescription());
            }

        });

        playerClassBox = new ComboBox<>();
        playerClassBox.getItems().addAll(Paper.DATABASE.getActorClassRegistry().values());
        playerClassBox.setConverter(new StringConverter<ActorClass>() {

            @Override
            public String toString(ActorClass object) {
                if (object != null) {
                    return object.getActorClassName();
                }

                return null;
            }

            @Override
            public ActorClass fromString(String string) {
                return Paper.DATABASE.getActorClass(string);
            }
        });
        playerClassBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                playerClassDescription.setText(newValue.getActorClassDescription());

                playerFavoredAttribute1.setText(Utils
                        .toNormalCase(((Attribute) newValue.getActorClassFavoredAttributes().toArray()[0]).name()));

                playerFavoredAttribute2.setText(Utils
                        .toNormalCase(((Attribute) newValue.getActorClassFavoredAttributes().toArray()[1]).name()));

                playerClassSkillLV.getItems().clear();
                playerClassSkillLV.getItems().addAll(newValue.getActorClassSkills());
            }
        });

        playerClassDescription = new TextArea();
        playerClassDescription.setEditable(false);
        playerClassDescription.setWrapText(true);

        playerRaceDescription = new TextArea();
        playerRaceDescription.setEditable(false);
        playerRaceDescription.setWrapText(true);

        playerFavoredAttribute1 = new TextField();
        playerFavoredAttribute1.setEditable(false);

        playerFavoredAttribute2 = new TextField();
        playerFavoredAttribute2.setEditable(false);

        playerClassSkillLV = new ListView<>();
        playerClassSkillLV.setCellFactory(lv -> new ListCell<Skill>() {

            @Override
            public void updateItem(Skill item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(Utils.toNormalCase(item.name()));
                } else
                    setText("");

            };

        });

        getDialogPane().getButtonTypes().add(ButtonType.OK);
        setResultConverter(new Callback<ButtonType, Player>() {

            @Override
            public Player call(ButtonType param) {

                if (param.equals(ButtonType.OK)) {
                    ActorClass aClass = playerClassBox.getSelectionModel().getSelectedItem();
                    ActorRace aRace = playerRaceBox.getSelectionModel().getSelectedItem();

                    Logger.debug("Creating player with " + aClass.getActorClassName() + " and "
                            + aRace.getActorRaceName() + " with the name " + playerNameFld.getText());

                    return new Player(playerNameFld.getText(), aClass, aRace);
                }

                return null;
            }
        });

        Label playerNameLbl = new Label("Player Name");
        Label playerRaceLbl = new Label("Player Race");
        Label playerClassLbl = new Label("Player Class");
        Label playerRaceDescLbl = new Label("Player Race Description");
        Label playerClassDescLbl = new Label("Player Class Description");
        Label playerFavoredAttributeLbl = new Label("Class Favored Attributes");
        Label playerClassSkillsLbl = new Label("Class Skills");

        GridPane gp = new GridPane(10, 10);
        gp.add(playerNameLbl, 0, 0);
        gp.add(playerNameFld, 1, 0);
        gp.add(playerRaceLbl, 2, 0);
        gp.add(playerRaceBox, 3, 0, 2, 1);
        gp.add(playerClassLbl, 5, 0);
        gp.add(playerClassBox, 6, 0, 2, 1);
        gp.add(playerRaceDescLbl, 0, 2);
        gp.add(playerRaceDescription, 0, 3, 3, 3);
        gp.add(playerClassDescLbl, 0, 6);
        gp.add(playerClassDescription, 0, 7, 3, 3);
        gp.add(playerFavoredAttributeLbl, 4, 1);
        gp.add(playerFavoredAttribute1, 4, 2);
        gp.add(playerFavoredAttribute2, 4, 3);
        gp.add(playerClassSkillsLbl, 4, 4);
        gp.add(playerClassSkillLV, 4, 5, 2, 5);

        getDialogPane().setContent(gp);
        getDialogPane().setMaxHeight(650);
    }

    public void showDialog() {
        Optional<Player> player = this.showAndWait();

        if (player.isPresent()) {
            Player p = player.get();
            Paper.DATABASE.addGlobal("playerName", p.getActorState().getActorName());
            Paper.PAPER_PLAYER_PROPERTY.set(p);
        } else {
            Logger.debug("FUCK, Can't load player");
            return;
        }

    }

}
