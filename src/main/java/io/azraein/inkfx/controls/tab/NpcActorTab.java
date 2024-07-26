package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class NpcActorTab extends PaperEditorTab {

	public NpcActorTab(InkFX inkFX) {
		super(inkFX);
		setText("Npc Actor Editor");
		setClosable(false);

		Label actorIdLbl = new Label("Actor ID");
		Label actorNameLbl = new Label("Actor Name");
		Label actorDescriptionLbl = new Label("Actor Description");
		Label actorCharacterRaceLbl = new Label("Actor Race");
		Label actorCharacterClassLbl = new Label("Actor Class");
		Label actorMaxHpLbl = new Label("Actor HP");
		Label actorMaxMpLbl = new Label("Actor MP");
		Label actorLevelLbl = new Label("Actor Level");
		Label actorExpLbl = new Label("Actor Exp");

		TextField actorIdFld = new TextField();
		TextField actorNameFld = new TextField();

		TextArea actorDescriptionFld = new TextArea();
		actorDescriptionFld.setWrapText(true);

		ComboBox<ActorRace> actorCharacterRaceCB = new ComboBox<>();
		actorCharacterRaceCB.getItems().addAll(inkFX.getRaceList().values());
		actorCharacterRaceCB.setConverter(new StringConverter<ActorRace>() {

			@Override
			public String toString(ActorRace object) {
				if (object != null)
					return object.getActorRaceId();
				else
					return null;
			}

			@Override
			public ActorRace fromString(String string) {
				return null;
			}
		});
		actorCharacterRaceCB.setCellFactory(listView -> {
			return new ListCell<ActorRace>() {

				@Override
				public void updateItem(ActorRace item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.getActorRaceId());
					} else {
						this.setText("");
					}
				}

			};
		});
		inkFX.getRaceList().addListener(new MapChangeListener<String, ActorRace>() {

			@Override
			public void onChanged(Change<? extends String, ? extends ActorRace> change) {

				if (change.wasAdded()) {
					if (inkFX.currentPluginProperty().get().getPluginDatabase().getRaceList()
							.containsKey(change.getKey()))
						actorCharacterRaceCB.getItems().remove(change.getValueRemoved());
					actorCharacterRaceCB.getItems().add(change.getValueAdded());
				} else if (change.wasRemoved()) {
					actorCharacterRaceCB.getItems().remove(change.getValueRemoved());
				}

			}

		});

		ComboBox<ActorClass> actorCharacterClassCB = new ComboBox<>();
		actorCharacterClassCB.getItems().addAll(inkFX.getCharClassList().values());
		actorCharacterClassCB.setConverter(new StringConverter<ActorClass>() {

			@Override
			public String toString(ActorClass object) {
				if (object != null)
					return object.getActorClassId();
				else
					return null;
			}

			@Override
			public ActorClass fromString(String string) {
				return null;
			}
		});
		inkFX.getCharClassList().addListener(new MapChangeListener<String, ActorClass>() {

			@Override
			public void onChanged(Change<? extends String, ? extends ActorClass> change) {

				if (change.wasAdded()) {
					if (inkFX.currentPluginProperty().get().getPluginDatabase().getCharClassList()
							.containsKey(change.getKey()))
						actorCharacterClassCB.getItems().remove(change.getValueRemoved());
					actorCharacterClassCB.getItems().add(change.getValueAdded());
				} else if (change.wasRemoved()) {
					actorCharacterClassCB.getItems().remove(change.getValueRemoved());
				}

			}

		});

		Spinner<Integer> actorMaxHPSpinner = new Spinner<>(1, 99999, 1);
		Spinner<Integer> actorMaxMPSpinner = new Spinner<>(1, 99999, 1);
		Spinner<Integer> actorLevelSpinner = new Spinner<>(1, 999, 1);

		@SuppressWarnings("unchecked")
		Spinner<Integer>[] actorAttributeSpinners = new Spinner[Attribute.values().length];
		Label[] actorAttributeLbls = new Label[Attribute.values().length];
		for (Attribute atr : Attribute.values()) {
			actorAttributeLbls[atr.ordinal()] = new Label(Utils.toNormalCase(atr.name()));
			actorAttributeSpinners[atr.ordinal()] = new Spinner<Integer>(1, 999, 1);
		}

		@SuppressWarnings("unchecked")
		Spinner<Integer>[] actorSkillSpinners = new Spinner[Skill.values().length];
		Label[] actorSkillLbls = new Label[Skill.values().length];
		for (Skill skill : Skill.values()) {
			actorSkillLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()));
			actorSkillSpinners[skill.ordinal()] = new Spinner<Integer>(1, 999, 1);
		}

		ListView<Actor> actorList = new ListView<>();
		actorList.setCellFactory(listView -> {
			return new ListCell<Actor>() {

				@Override
				public void updateItem(Actor item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.getActorId());

					}

				}

			};
		});
		actorList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				ActorState state = newValue.getActorState();

			}
		});
		actorList.getItems().addAll(inkFX.getActorList().values());
		inkFX.getActorList().addListener(new MapChangeListener<String, Actor>() {

			@Override
			public void onChanged(Change<? extends String, ? extends Actor> change) {
				if (change.wasAdded()) {
					if (inkFX.currentPluginProperty().get().getPluginDatabase().getActorList()
							.containsKey(change.getKey()))
						actorList.getItems().remove(change.getValueRemoved());
					actorList.getItems().add(change.getValueAdded());
				} else if (change.wasRemoved()) {
					actorList.getItems().remove(change.getValueRemoved());
				}
			}

		});

		
		inkFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue == null)
				actorList.getItems().clear();				
		});


		// Put All the Controls in the Grid
		GridPane actorBasicInfoPane = new GridPane();
		GridPane.setColumnSpan(actorDescriptionFld, 3);
		actorBasicInfoPane.setPadding(new Insets(15));
		actorBasicInfoPane.setHgap(10);
		actorBasicInfoPane.setVgap(10);
		actorBasicInfoPane.add(actorIdLbl, 0, 0);
		actorBasicInfoPane.add(actorIdFld, 1, 0);
		actorBasicInfoPane.add(actorNameLbl, 2, 0);
		actorBasicInfoPane.add(actorNameFld, 3, 0);
		actorBasicInfoPane.add(actorDescriptionLbl, 0, 1);
		actorBasicInfoPane.add(actorDescriptionFld, 1, 1);
		actorBasicInfoPane.add(actorCharacterClassLbl, 0, 2);
		actorBasicInfoPane.add(actorCharacterClassCB, 1, 2);
		actorBasicInfoPane.add(actorMaxHpLbl, 2, 2);
		actorBasicInfoPane.add(actorMaxHPSpinner, 3, 2);
		actorBasicInfoPane.add(actorCharacterRaceLbl, 0, 3);
		actorBasicInfoPane.add(actorCharacterRaceCB, 1, 3);
		actorBasicInfoPane.add(actorMaxMpLbl, 2, 3);
		actorBasicInfoPane.add(actorMaxMPSpinner, 3, 3);

		BorderPane rootPane = new BorderPane();
		rootPane.setLeft(actorList);
		rootPane.setCenter(actorBasicInfoPane);

		setContent(rootPane);
	}

}
