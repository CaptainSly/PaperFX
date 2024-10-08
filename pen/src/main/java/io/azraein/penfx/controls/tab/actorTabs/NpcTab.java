package io.azraein.penfx.controls.tab.actorTabs;

import org.tinylog.Logger;

import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.ActorState;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.stats.Attribute;
import io.azraein.inkfx.system.actors.stats.Skill;
import io.azraein.inkfx.system.io.Database;
import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.converters.ActorClassStringConverter;
import io.azraein.penfx.controls.converters.ActorRaceStringConverter;
import io.azraein.penfx.controls.tab.PaperEditorTab;
import javafx.collections.MapChangeListener.Change;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class NpcTab extends PaperEditorTab {

    private TextField npcActorIdFld;
    private TextField npcActorNameFld;
    private TextArea npcActorDescriptionArea;

    private ComboBox<ActorRace> npcActorRaceCB;
    private ComboBox<ActorClass> npcActorClassCB;

    private Spinner<Integer>[] npcActorAttributeSpinners;
    private Spinner<Integer>[] npcActorSkillSpinners;

    // TODO Implement Inventory, Equipment, etc.

    @SuppressWarnings("unchecked")
    public NpcTab(PenFX inkFX) {
        super(inkFX);
        setText("Npc Editor");

        ListView<Npc> dbNpcLV = new ListView<>();
        dbNpcLV.setCellFactory(listView -> new ListCell<Npc>() {

            @Override
            protected void updateItem(Npc item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getActorId());

                    ContextMenu cm = new ContextMenu();
                    MenuItem removeNpcItem = new MenuItem("Remove Npc");
                    removeNpcItem.setOnAction(event -> {
                        Npc npc = dbNpcLV.getSelectionModel().getSelectedItem();

                        if (penFX.currentPluginProperty().get() != null) {
                            Database curPluginDb = penFX.currentPluginProperty().get().getPluginDatabase();
                            if (curPluginDb.getNpcRegistry().containsKey(npc.getActorId())) {
                                curPluginDb.getNpcRegistry().remove(npc.getActorId());
                                penFX.getObservableLocationRegistry().remove(npc.getActorId());
                            }
                        }
                    });
                    cm.getItems().add(removeNpcItem);
                    setContextMenu(cm);
                } else {
                    setText("");
                    setContextMenu(null);
                }

            }

        });
        dbNpcLV.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue != null) {
                ActorState actorState = newValue.getActorState();
                npcActorIdFld.setText(newValue.getActorId());
                npcActorNameFld.setText(actorState.getActorName());
                npcActorDescriptionArea.setText(actorState.getActorDescription());

                ActorRace race = penFX.getObservableActorRaceRegistry().get(actorState.getActorRaceId());
                ActorClass clazz = penFX.getObservableActorClassRegistry().get(actorState.getActorClassId());

                npcActorRaceCB.getSelectionModel().select(race);
                npcActorClassCB.getSelectionModel().select(clazz);

                for (Attribute attr : Attribute.values())
                    npcActorAttributeSpinners[attr.ordinal()].getValueFactory()
                            .setValue(actorState.getActorAttribute(attr).getLevel());

                for (Skill skill : Skill.values())
                    npcActorSkillSpinners[skill.ordinal()].getValueFactory()
                            .setValue(actorState.getActorSkill(skill).getLevel());
            }
        });
        inkFX.getObservableNpcRegistry().values().forEach(npc -> dbNpcLV.getItems().add(npc));
        inkFX.getObservableNpcRegistry().addListener((Change<? extends String, ? extends Npc> change) -> {

            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getNpcRegistry()
                        .containsKey(change.getKey())) {
                    dbNpcLV.getItems().remove(change.getValueRemoved());
                    dbNpcLV.getItems().add(change.getValueAdded());
                } else {
                    dbNpcLV.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                dbNpcLV.getItems().remove(change.getValueRemoved());
            }
        });

        npcActorIdFld = new TextField();
        npcActorNameFld = new TextField();

        npcActorDescriptionArea = new TextArea();
        npcActorDescriptionArea.setWrapText(true);

        npcActorRaceCB = new ComboBox<>();
        npcActorRaceCB.setCellFactory(listView -> new ListCell<>() {

            @Override
            public void updateItem(ActorRace item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getActorRaceId());
                } else
                    setText("");

            }

        });
        npcActorRaceCB.setConverter(new ActorRaceStringConverter(inkFX));
        npcActorRaceCB.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue != null) {
                ActorClass clazz = npcActorClassCB.getValue();

                int[] skillLevels = new int[Skill.values().length];
                if (clazz != null) {
                    for (Skill skill : Skill.values()) {
                        skillLevels[skill.ordinal()] = clazz.getActorClassBaseSkills()[skill.ordinal()];
                    }
                }

                for (Skill skill : Skill.values()) {
                    skillLevels[skill.ordinal()] += newValue.getActorRaceBaseSkill(skill);
                    npcActorSkillSpinners[skill.ordinal()].getValueFactory().setValue(skillLevels[skill.ordinal()]);
                }

                for (Attribute attr : Attribute.values()) {
                    npcActorAttributeSpinners[attr.ordinal()].getValueFactory()
                            .setValue(newValue.getActorRaceBaseAttribute(attr));
                }

            }

        });
        inkFX.getObservableActorRaceRegistry().addListener((Change<? extends String, ? extends ActorRace> change) -> {
            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getActorRaceRegistry()
                        .containsKey(change.getKey())) {
                    npcActorRaceCB.getItems().remove(change.getValueRemoved());
                    npcActorRaceCB.getItems().add(change.getValueAdded());
                } else {
                    npcActorRaceCB.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                npcActorRaceCB.getItems().remove(change.getValueRemoved());
            }
        });

        npcActorClassCB = new ComboBox<>();
        npcActorClassCB.setCellFactory(listView -> new ListCell<ActorClass>() {

            @Override
            public void updateItem(ActorClass item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null)
                    setText(item.getActorClassId());
                else
                    setText("");

            }

        });
        npcActorClassCB.setConverter(new ActorClassStringConverter(inkFX));
        npcActorClassCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                ActorRace race = npcActorRaceCB.getValue();

                int[] skillLevels = new int[Skill.values().length];
                if (race != null) {
                    for (Skill skill : Skill.values()) {
                        skillLevels[skill.ordinal()] = race.getActorRaceBaseSkill(skill);
                    }
                }

                for (Skill skill : Skill.values()) {
                    skillLevels[skill.ordinal()] += newValue.getActorClassBaseSkills()[skill.ordinal()];
                    npcActorSkillSpinners[skill.ordinal()].getValueFactory().setValue(skillLevels[skill.ordinal()]);
                }

            }

        });
        inkFX.getObservableActorClassRegistry().addListener((Change<? extends String, ? extends ActorClass> change) -> {
            if (change.wasAdded()) {
                if (penFX.currentPluginProperty().get().getPluginDatabase().getActorClassRegistry()
                        .containsKey(change.getKey())) {
                    npcActorClassCB.getItems().remove(change.getValueRemoved());
                    npcActorClassCB.getItems().add(change.getValueAdded());
                } else {
                    npcActorClassCB.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                npcActorClassCB.getItems().remove(change.getValueRemoved());
            }
        });

        npcActorAttributeSpinners = new Spinner[Attribute.values().length];
        npcActorSkillSpinners = new Spinner[Skill.values().length];

        for (Attribute attr : Attribute.values()) {
            npcActorAttributeSpinners[attr.ordinal()] = new Spinner<>(0, 100, 1);
            npcActorAttributeSpinners[attr.ordinal()].setEditable(true);
        }

        for (Skill skill : Skill.values()) {
            npcActorSkillSpinners[skill.ordinal()] = new Spinner<>(0, 100, 1);
            npcActorSkillSpinners[skill.ordinal()].setEditable(true);
        }

        Label npcActorIdLbl = new Label("Npc Id");
        Label npcActorNameLbl = new Label("Npc Name");
        Label npcActorDescriptionLbl = new Label("Npc Description");
        Label npcActorRaceLbl = new Label("Npc Actor Race");
        Label npcActorClassLbl = new Label("Npc Actor Class");

        GridPane infoGridPane = new GridPane();
        GridPane.setColumnSpan(npcActorDescriptionArea, 3);
        infoGridPane.setPadding(new Insets(15));
        infoGridPane.setHgap(10);
        infoGridPane.setVgap(10);
        infoGridPane.add(npcActorIdLbl, 0, 0);
        infoGridPane.add(npcActorIdFld, 1, 0);
        infoGridPane.add(npcActorNameLbl, 2, 0);
        infoGridPane.add(npcActorNameFld, 3, 0);
        infoGridPane.add(npcActorDescriptionLbl, 0, 1);
        infoGridPane.add(npcActorDescriptionArea, 1, 1);
        infoGridPane.add(npcActorRaceLbl, 0, 2);
        infoGridPane.add(npcActorRaceCB, 1, 2);
        infoGridPane.add(npcActorClassLbl, 2, 2);
        infoGridPane.add(npcActorClassCB, 3, 2);

        GridPane statGridPane = new GridPane();
        statGridPane.setPadding(new Insets(15));
        statGridPane.setHgap(10);
        statGridPane.setVgap(10);

        // Place Attributes
        for (int i = 0; i < Attribute.values().length; i++) {
            Attribute attr = Attribute.values()[i];
            statGridPane.add(new Label(Utils.toNormalCase(attr.name())), 0, i);
            statGridPane.add(npcActorAttributeSpinners[i], 1, i);
        }

        // Place Skills
        int numCols = 3;
        int numRows = (int) Math.ceil((double) Skill.values().length / numCols);
        int index = 0;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (index < Skill.values().length) {
                    int curCol = 3 + col;
                    Skill skill = Skill.values()[index];
                    statGridPane.add(new Label(Utils.toNormalCase(skill.name())), curCol * 2, row);
                    statGridPane.add(npcActorSkillSpinners[index], curCol * 2 + 1, row);
                    index++;
                }
            }
        }

        Button saveNpcBtn = new Button("Save Npc");
        saveNpcBtn.setOnAction(event -> {
            ActorRace npcRace = npcActorRaceCB.getValue();
            ActorClass npcClass = npcActorClassCB.getValue();

            String npcId = npcActorIdFld.getText();
            String npcName = npcActorNameFld.getText();

            Npc npc = new Npc(npcId, npcName, npcClass, npcRace);

            npc.getActorState().setActorDescription(npcActorDescriptionArea.getText());

            Logger.debug("About to set Npc Skills");

            ActorState npcActorState = npc.getActorState();

            // Set the NPC's skills
            for (Skill skill : Skill.values())
                npcActorSkillSpinners[skill.ordinal()].getValueFactory()
                        .setValue(npcActorState.getActorSkill(skill).getLevel());
            // Set the NPC's attributes
            for (Attribute attr : Attribute.values())
                npcActorAttributeSpinners[attr.ordinal()].getValueFactory()
                        .setValue(npcActorState.getActorAttribute(attr).getLevel());

            inkFX.currentPluginProperty().get().getPluginDatabase().addNpc(npc);
            inkFX.getObservableNpcRegistry().put(npc.getActorId(), npc);
        });

        Button clearNpcFormBtn = new Button("Clear Npc Form");
        clearNpcFormBtn.setOnAction(event -> {
            npcActorIdFld.setText("");
            npcActorNameFld.setText("");
            npcActorDescriptionArea.setText("");
            npcActorRaceCB.getSelectionModel().select(null);
            npcActorClassCB.getSelectionModel().select(null);

            for (Skill skill : Skill.values())
                npcActorSkillSpinners[skill.ordinal()].getValueFactory().setValue(1);

            for (Attribute attr : Attribute.values())
                npcActorAttributeSpinners[attr.ordinal()].getValueFactory().setValue(1);

        });

        GridPane footerControlContainer = new GridPane();
        footerControlContainer.setPadding(new Insets(5));
        footerControlContainer.setHgap(10);
        footerControlContainer.setVgap(10);
        footerControlContainer.add(saveNpcBtn, 0, 0);
        footerControlContainer.add(clearNpcFormBtn, 1, 0);

        VBox controlContainer = new VBox();
        controlContainer.setPadding(new Insets(5, 10, 5, 10));

        ScrollPane statScrollPane = new ScrollPane(statGridPane);
        controlContainer.getChildren().addAll(infoGridPane, new Separator(Orientation.HORIZONTAL), statScrollPane,
                footerControlContainer);

        content.setLeft(dbNpcLV);
        content.setCenter(controlContainer);
    }

}
