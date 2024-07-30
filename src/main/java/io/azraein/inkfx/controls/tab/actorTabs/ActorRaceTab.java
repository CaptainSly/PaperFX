package io.azraein.inkfx.controls.tab.actorTabs;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import io.azraein.paperfx.system.io.Database;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ActorRaceTab extends PaperEditorTab {

	public ActorRaceTab(InkFX inkFX) {
		super(inkFX);
		setText("Race Editor");

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
			actorRaceBaseAttrSpinners[attr.ordinal()] = new Spinner<>(0, 100, 0);
			actorRaceBaseAttrLbls[attr.ordinal()] = new Label(Utils.toNormalCase(attr.name()));
		}

		@SuppressWarnings("unchecked")
		Spinner<Integer>[] actorRaceSkillBonusesSpinner = new Spinner[Skill.values().length];
		Label[] actorRaceSkillBonusesLbls = new Label[Skill.values().length];
		for (Skill skill : Skill.values()) {
			actorRaceSkillBonusesSpinner[skill.ordinal()] = new Spinner<>(0, 10, 0);
			actorRaceSkillBonusesLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()));
		}

		ListView<ActorRace> actorRaceList = new ListView<>();
		actorRaceList.setCellFactory(listView -> {
			return new ListCell<ActorRace>() {

				@Override
				public void updateItem(ActorRace item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {

						ContextMenu cm = new ContextMenu();
						MenuItem removeRace = new MenuItem("Remove Race");
						removeRace.setOnAction(event -> {
							ActorRace ar = actorRaceList.getSelectionModel().getSelectedItem();
							
							if (inkFX.currentPluginProperty().get() != null) {
								Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
								if (curPluginDb.getRaceList().containsKey(ar.getActorRaceId())) {
									curPluginDb.getRaceList().remove(ar.getActorRaceId());
									inkFX.getRaceList().remove(ar.getActorRaceId());
								}
							}

						});

						cm.getItems().add(removeRace);
						this.setContextMenu(cm);
						this.setText(item.getActorRaceId());
					} else {
						this.setContextMenu(null);
						this.setText("");
					}
				}

			};
		});
		actorRaceList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				actorRaceIdFld.setText(newValue.getActorRaceId());
				actorRaceNameFld.setText(newValue.getActorRaceName());
				actorRaceDescriptionFld.setText(newValue.getActorRaceDescription());
				actorIsBeastRaceCB.setSelected(newValue.isBeastRace());

				for (Skill skill : Skill.values())
					actorRaceSkillBonusesSpinner[skill.ordinal()].getValueFactory()
							.setValue(newValue.getActorRaceBaseSkill(skill));

				for (Attribute attr : Attribute.values())
					actorRaceBaseAttrSpinners[attr.ordinal()].getValueFactory()
							.setValue(newValue.getActorRaceBaseAttribute(attr));
			}
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

		Button clearFieldBtn = new Button("Clear Form");
		clearFieldBtn.setOnAction(event -> {
			actorRaceIdFld.setText("");
			actorRaceNameFld.setText("");
			actorRaceDescriptionFld.setText("");
			actorIsBeastRaceCB.setSelected(false);

			for (Skill skill : Skill.values())
				actorRaceSkillBonusesSpinner[skill.ordinal()].getValueFactory().setValue(0);

			for (Attribute attr : Attribute.values())
				actorRaceBaseAttrSpinners[attr.ordinal()].getValueFactory().setValue(0);

		});

		inkFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue == null) {
				actorRaceList.getItems().clear();
			}
		});

		Button saveRaceBtn = new Button("Save Race");
		saveRaceBtn.setOnAction(event -> {
			String actorRaceId = actorRaceIdFld.getText();

			ActorRace actorRace = null;
			if (inkFX.getRaceList().containsKey(actorRaceId)) {
				actorRace = inkFX.getRaceList().get(actorRaceId);
			}

			if (actorRace == null)
				actorRace = new ActorRace(actorRaceId, actorRaceNameFld.getText());

			actorRace.setActorRaceName(actorRaceNameFld.getText());
			actorRace.setActorRaceDescription(actorRaceDescriptionFld.getText());
			actorRace.setBeastRace(actorIsBeastRaceCB.isSelected());

			for (Skill skill : Skill.values())
				actorRace.getActorRaceSkillBonuses()[skill.ordinal()] = actorRaceSkillBonusesSpinner[skill.ordinal()]
						.getValue();

			for (Attribute attr : Attribute.values())
				actorRace.getActorRaceBaseAttributes()[attr.ordinal()] = actorRaceBaseAttrSpinners[attr.ordinal()]
						.getValue();

			inkFX.currentPluginProperty().get().getPluginDatabase().addCharacterRace(actorRace);
			inkFX.getRaceList().put(actorRace.getActorRaceId(), actorRace);
		});

		GridPane raceInfoPane = new GridPane();
		GridPane.setColumnSpan(actorRaceDescriptionFld, 4);
		GridPane.setRowSpan(actorRaceDescriptionFld, 6);
		raceInfoPane.setPadding(new Insets(15));
		raceInfoPane.setHgap(10);
		raceInfoPane.setVgap(10);
		raceInfoPane.add(actorRaceIdLbl, 0, 0);
		raceInfoPane.add(actorRaceIdFld, 1, 0);
		raceInfoPane.add(actorRaceNameLbl, 2, 0);
		raceInfoPane.add(actorRaceNameFld, 3, 0);
		raceInfoPane.add(actorRaceDescriptionLbl, 0, 1);
		raceInfoPane.add(actorRaceDescriptionFld, 1, 1);
		raceInfoPane.add(actorIsBeastRaceLbl, 0, 7);
		raceInfoPane.add(actorIsBeastRaceCB, 1, 7);
		raceInfoPane.add(saveRaceBtn, 0, 19);
		raceInfoPane.add(clearFieldBtn, 1, 19);

		raceInfoPane.add(new Label("Race Inital Attribute Values"), 5, 0);

		for (int i = 0; i < Attribute.values().length; i++) {
			raceInfoPane.add(actorRaceBaseAttrLbls[i], 5, i + 1);
			raceInfoPane.add(actorRaceBaseAttrSpinners[i], 6, i + 1);
		}

		int numCols = 3;
		int numRows = (int) Math.ceil((double) Skill.values().length / numCols);
		int index = 0;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (index < Skill.values().length) {
					int curCol = col;
					int curRow = 9 + row;
					raceInfoPane.add(actorRaceSkillBonusesLbls[index], curCol * 2, curRow);
					raceInfoPane.add(actorRaceSkillBonusesSpinner[index], curCol * 2 + 1, curRow);
					index++;
				}
			}
		}

		content.setLeft(actorRaceList);
		content.setCenter(raceInfoPane);
	}

}