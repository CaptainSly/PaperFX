package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class ActorRaceTab extends PaperEditorTab {

	public ActorRaceTab(InkFX inkFX) {
		super(inkFX);
		setText("Race Editor");
		setClosable(false);

		Label actorRaceIdLbl = new Label("Race Id");
		Label actorRaceNameLbl = new Label("Race Name");
		Label actorRaceDescriptionLbl = new Label("Race Description");
		Label actorIsBeastRaceLbl = new Label("Is Beast Race?");

		TextField actorRaceIdFld = new TextField();
		TextField actorRaceNameFld = new TextField();

		CheckBox actorIsBeastRaceCB = new CheckBox();

		TextArea actorRaceDescriptionFld = new TextArea();
		actorRaceDescriptionFld.setWrapText(true);

		@SuppressWarnings("unchecked")
		Spinner<Integer>[] actorRaceBaseAttrSpinners = new Spinner[Attribute.values().length];
		Label[] actorRaceBaseAttrLbls = new Label[Attribute.values().length];
		for (Attribute attr : Attribute.values()) {
			actorRaceBaseAttrSpinners[attr.ordinal()] = new Spinner<>(1, 999, 1);
			actorRaceBaseAttrLbls[attr.ordinal()] = new Label(Utils.toNormalCase(attr.name()));
		}

		@SuppressWarnings("unchecked")
		Spinner<Integer>[] actorRaceSkillBonusesSpinner = new Spinner[Skill.values().length];
		Label[] actorRaceSkillBonusesLbls = new Label[Skill.values().length];
		for (Skill skill : Skill.values()) {
			actorRaceSkillBonusesSpinner[skill.ordinal()] = new Spinner<>(1, 999, 1);
			actorRaceSkillBonusesLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()));
		}

		ListView<ActorRace> actorRaceList = new ListView<>();
		actorRaceList.setCellFactory(listView -> {
			return new ListCell<ActorRace>() {

				@Override
				public void updateItem(ActorRace item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(item.getActorRaceId());
					}
				}

			};
		});

		actorRaceList.getItems().addAll(inkFX.getRaceList().values());
		inkFX.getRaceList().addListener(new MapChangeListener<String, ActorRace>() {

			@Override
			public void onChanged(Change<? extends String, ? extends ActorRace> change) {
				if (change.wasAdded()) {
					if (inkFX.currentPluginProperty().get().getPluginDatabase().getRaceList()
							.containsKey(change.getKey())) {
						actorRaceList.getItems().remove(change.getValueRemoved());
						actorRaceList.getItems().add(change.getValueAdded());
					} else {
						actorRaceList.getItems().add(change.getValueAdded());
					}
				} else if (change.wasRemoved()) {
					actorRaceList.getItems().remove(change.getValueRemoved());
				}
			}

		});

		Button saveRaceBtn = new Button("Save Race");
		saveRaceBtn.setOnAction(event -> {
			ActorRace race = new ActorRace(actorRaceIdFld.getText(), actorRaceNameFld.getText());
			race.setActorRaceDescription(actorRaceDescriptionFld.getText());
			race.setBeastRace(actorIsBeastRaceCB.isSelected());

			for (Attribute attr : Attribute.values()) {
				race.getActorRaceBaseAttributes()[attr.ordinal()] = actorRaceBaseAttrSpinners[attr.ordinal()]
						.getValue();
			}

			for (Skill skill : Skill.values()) {
				race.getActorRaceSkillBonuses()[skill.ordinal()] = actorRaceSkillBonusesSpinner[skill.ordinal()]
						.getValue();
			}

			inkFX.currentPluginProperty().get().getPluginDatabase().addCharacterRace(race);
			inkFX.getRaceList().put(race.getActorRaceId(), race);
		});

		// TODO: Add the rest of the stuff to the tab, not even close to be finished
		// yet.

		GridPane raceInfoPane = new GridPane();
		GridPane.setColumnSpan(actorRaceDescriptionFld, 3);
		raceInfoPane.setPadding(new Insets(15));
		raceInfoPane.setHgap(10);
		raceInfoPane.setVgap(10);
		raceInfoPane.add(actorRaceIdLbl, 0, 0);
		raceInfoPane.add(actorRaceIdFld, 1, 0);
		raceInfoPane.add(actorRaceNameLbl, 2, 0);
		raceInfoPane.add(actorRaceNameFld, 3, 0);
		raceInfoPane.add(actorRaceDescriptionLbl, 0, 1);
		raceInfoPane.add(actorRaceDescriptionFld, 1, 1);
		raceInfoPane.add(actorIsBeastRaceLbl, 0, 2);
		raceInfoPane.add(actorIsBeastRaceCB, 1, 2);
		raceInfoPane.add(saveRaceBtn, 2, 2);

		BorderPane rootPane = new BorderPane();
		rootPane.setLeft(actorRaceList);
		rootPane.setCenter(raceInfoPane);

		setContent(rootPane);

	}

}