package io.azraein.inkfx.controls.tab.actorTabs;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import io.azraein.paperfx.system.io.Database;
import javafx.collections.MapChangeListener.Change;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class ActorClassTab extends PaperEditorTab {

	private TextField actorClassIdFld;
	private TextField actorClassNameFld;
	private TextArea actorClassDescriptionArea;
	private ListView<Skill> actorSpecializedSkills;
	private Spinner<Integer>[] actorClassBaseSkillsSpinners;
	private ComboBox<Attribute> actorClassFavoredAttributeOneCB;
	private ComboBox<Attribute> actorClassFavoredAttributeTwoCB;

	@SuppressWarnings("unchecked")
	public ActorClassTab(InkFX inkFX) {
		super(inkFX);
		setText("Class Editor");

		Label actorClassIdLbl = new Label("Class ID");
		Label actorClassNameLbl = new Label("Class Name");
		Label actorClassDescriptionLbl = new Label("Class Description");

		actorClassBaseSkillsSpinners = new Spinner[Skill.values().length];
		Label[] actorClassBaseSkillLbls = new Label[Skill.values().length];
		for (Skill skill : Skill.values()) {
			actorClassBaseSkillLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()) + " Bonus");
			actorClassBaseSkillsSpinners[skill.ordinal()] = new Spinner<>(5, 10, 5);
			actorClassBaseSkillsSpinners[skill.ordinal()].setMaxWidth(80);
		}

		Label actorClassFavoredAttributes = new Label("Class Favorite Attributes");
		actorClassFavoredAttributeOneCB = new ComboBox<>();
		actorClassFavoredAttributeTwoCB = new ComboBox<>();

		actorClassFavoredAttributeOneCB.getItems().addAll(Attribute.values());
		actorClassFavoredAttributeOneCB.getSelectionModel().select(0);
		actorClassFavoredAttributeOneCB.setCellFactory(listView -> new ListCell<Attribute>() {

			@Override
			public void updateItem(Attribute item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					this.setText(Utils.toNormalCase(item.name()));
				} else
					this.setText(null);
			}

		});
		actorClassFavoredAttributeOneCB.setConverter(new StringConverter<Attribute>() {

			@Override
			public String toString(Attribute object) {
				return object != null ? Utils.toNormalCase(object.name()) : null;
			}

			@Override
			public Attribute fromString(String string) {
				return null;
			}
		});

		actorClassFavoredAttributeTwoCB.getItems().addAll(Attribute.values());
		actorClassFavoredAttributeTwoCB.getSelectionModel().select(1);
		actorClassFavoredAttributeTwoCB.setCellFactory(listView -> new ListCell<Attribute>() {

			@Override
			public void updateItem(Attribute item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					this.setText(Utils.toNormalCase(item.name()));
				} else
					this.setText(null);
			}

		});
		actorClassFavoredAttributeTwoCB.setConverter(new StringConverter<Attribute>() {

			@Override
			public String toString(Attribute object) {
				return object != null ? Utils.toNormalCase(object.name()) : null;
			}

			@Override
			public Attribute fromString(String string) {
				return null;
			}
		});

		Label actorClassSkills = new Label("Class Specialized Skills");
		actorSpecializedSkills = new ListView<>();
		actorSpecializedSkills.setCellFactory(listView -> new ListCell<Skill>() {
			@Override
			public void updateItem(Skill item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					this.setText(Utils.toNormalCase(item.name()));
					ContextMenu cm = new ContextMenu();
					MenuItem removeSkillItem = new MenuItem("Remove Skill");
					cm.getItems().add(removeSkillItem);

					removeSkillItem.setOnAction(event -> {
						actorSpecializedSkills.getItems().remove(item);
					});

					this.setContextMenu(cm);
				} else {
					this.setText("");
					this.setContextMenu(null);
				}

			}
		});

		ComboBox<Skill> skillChoice = new ComboBox<>();
		skillChoice.getItems().addAll(Skill.values());
		skillChoice.getSelectionModel().selectFirst();
		skillChoice.setCellFactory(listView -> {
			return new ListCell<Skill>() {
				@Override
				public void updateItem(Skill item, boolean empty) {
					super.updateItem(item, empty);

					if (item != null) {
						this.setText(Utils.toNormalCase(item.name()));
					} else {
						this.setText("");
					}

				}
			};
		});
		skillChoice.setConverter(new StringConverter<Skill>() {

			@Override
			public String toString(Skill object) {
				return object != null ? Utils.toNormalCase(object.name()) : null;
			}

			@Override
			public Skill fromString(String string) {
				return null;
			}
		});

		Button addSkillButton = new Button("Add Skill");
		addSkillButton.setOnAction(event -> {
			if (skillChoice.getValue() != null) { // Make sure the skill isn't null
				if (!actorSpecializedSkills.getItems().contains(skillChoice.getValue())) { // Make sure the list doesn't
																							// contain
					// the skill already
					if (actorSpecializedSkills.getItems().size() < 6) { // Make sure the list doesn't already have 6
																		// skills.
						actorSpecializedSkills.getItems().add(skillChoice.getValue());
					}
				}
			}
		});

		actorClassIdFld = new TextField();
		actorClassNameFld = new TextField();

		actorClassDescriptionArea = new TextArea();
		actorClassDescriptionArea.setWrapText(true);

		ListView<ActorClass> actorClassList = new ListView<>();
		actorClassList.getItems().addAll(inkFX.getActorClassList().values());
		inkFX.getActorClassList().addListener((Change<? extends String, ? extends ActorClass> change) -> {
			if (change.wasAdded()) {
				if (inkFX.currentPluginProperty().get().getPluginDatabase().getActorClassList()
						.containsKey(change.getKey()))
					actorClassList.getItems().remove(change.getValueRemoved());
				actorClassList.getItems().add(change.getValueAdded());
			} else if (change.wasRemoved()) {
				actorClassList.getItems().remove(change.getValueRemoved());
			}
		});
		actorClassList.setCellFactory(listView -> new ListCell<ActorClass>() {

			@Override
			public void updateItem(ActorClass item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					ContextMenu cm = new ContextMenu();
					MenuItem removeClass = new MenuItem("Remove class");
					removeClass.setOnAction(event -> {
						ActorClass ac = actorClassList.getSelectionModel().getSelectedItem();

						if (inkFX.currentPluginProperty().get() != null) {
							Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
							if (curPluginDb.getActorClassList().containsKey(ac.getActorClassId())) {
								curPluginDb.getActorClassList().remove(ac.getActorClassId());
								inkFX.getActorClassList().remove(ac.getActorClassId());
							}
						}

					});

					cm.getItems().add(removeClass);
					setContextMenu(cm);
					setText(item.getActorClassId());
				} else {
					setContextMenu(null);
					setText("");
				}
			}

		});
		actorClassList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue != null) {
				clearData();
				loadData(newValue);
			}

		});

		Button clearFieldBtn = new Button("Clear Form");
		clearFieldBtn.setOnAction(event -> {
			clearData();
		});

		inkFX.currentPluginProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue == null) {
				actorClassList.getItems().clear();
			}
		});

		Button saveActorClassBtn = new Button("Save Actor Class");
		saveActorClassBtn.setOnAction(event -> {
			String actorClassId = actorClassIdFld.getText();
			String actorClassName = actorClassNameFld.getText();

			ActorClass actorClass = null;
			if (inkFX.getActorClassList().containsKey(actorClassId)) {
				actorClass = inkFX.getActorClassList().get(actorClassId);
			}

			if (actorClass == null)
				actorClass = new ActorClass(actorClassId, actorClassName);

			actorClass.setActorClassName(actorClassName);
			actorClass.setActorClassDescription(actorClassDescriptionArea.getText());
			actorClass.setActorClassFavoredAttribute(actorClassFavoredAttributeOneCB.getValue(),
					actorClassFavoredAttributeTwoCB.getValue());
			actorClass.setActorClassSkills(actorSpecializedSkills.getItems());

			for (Skill skill : Skill.values())
				actorClass.getActorClassBaseSkills()[skill.ordinal()] = actorClassBaseSkillsSpinners[skill.ordinal()]
						.getValue();

			inkFX.currentPluginProperty().get().getPluginDatabase().addActorClass(actorClass);
			inkFX.getActorClassList().put(actorClass.getActorClassId(), actorClass);
		});

		Label actorClassBaseSkillsLbl = new Label("Class Skill Bonus");

		GridPane actorClassInfoPane = new GridPane();
		GridPane.setColumnSpan(actorClassDescriptionArea, 4);
		GridPane.setColumnSpan(actorSpecializedSkills, 2);
		GridPane.setRowSpan(actorSpecializedSkills, 12);
		GridPane.setRowSpan(actorClassDescriptionArea, 6);
		actorClassInfoPane.setPadding(new Insets(15));
		actorClassInfoPane.setHgap(10);
		actorClassInfoPane.setVgap(10);
		actorClassInfoPane.add(actorClassIdLbl, 0, 0);
		actorClassInfoPane.add(actorClassIdFld, 1, 0);
		actorClassInfoPane.add(actorClassNameLbl, 2, 0);
		actorClassInfoPane.add(actorClassNameFld, 3, 0);
		actorClassInfoPane.add(actorClassDescriptionLbl, 0, 1);
		actorClassInfoPane.add(actorClassDescriptionArea, 1, 1);

		actorClassInfoPane.add(actorClassSkills, 0, 7);
		actorClassInfoPane.add(actorSpecializedSkills, 0, 8);
		actorClassInfoPane.add(skillChoice, 0, 20);
		actorClassInfoPane.add(addSkillButton, 1, 20);
		actorClassInfoPane.add(saveActorClassBtn, 2, 20);
		actorClassInfoPane.add(clearFieldBtn, 3, 20);

		actorClassInfoPane.add(actorClassFavoredAttributes, 5, 0);
		actorClassInfoPane.add(actorClassFavoredAttributeOneCB, 5, 1);
		actorClassInfoPane.add(actorClassFavoredAttributeTwoCB, 5, 2);

		int numCols = 3;
		int numRows = (int) Math.ceil((double) Skill.values().length / numCols);
		int index = 0;

		actorClassInfoPane.add(actorClassBaseSkillsLbl, 2, 7);
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (index < Skill.values().length) {
					int curCol = 1 + col;
					int curRow = 8 + row;
					actorClassInfoPane.add(actorClassBaseSkillLbls[index], curCol * 2, curRow);
					actorClassInfoPane.add(actorClassBaseSkillsSpinners[index], curCol * 2 + 1, curRow);
					index++;
				}
			}
		}

		content.setLeft(actorClassList);
		content.setCenter(actorClassInfoPane);
	}

	private void clearData() {
		actorClassIdFld.setText("");
		actorClassNameFld.setText("");
		actorClassDescriptionArea.setText("");

		for (Skill skill : Skill.values()) {
			actorClassBaseSkillsSpinners[skill.ordinal()].getValueFactory().setValue(0);
		}

		actorClassFavoredAttributeOneCB.getSelectionModel().select(0);
		actorClassFavoredAttributeTwoCB.getSelectionModel().select(1);

		actorSpecializedSkills.getItems().clear();
	}

	private void loadData(ActorClass newValue) {
		actorClassIdFld.setText(newValue.getActorClassId());
		actorClassNameFld.setText(newValue.getActorClassName());
		actorClassDescriptionArea.setText(newValue.getActorClassDescription());

		for (Skill skill : Skill.values()) {
			actorClassBaseSkillsSpinners[skill.ordinal()].getValueFactory()
					.setValue(newValue.getActorClassBaseSkills()[skill.ordinal()]);
		}

		actorClassFavoredAttributeOneCB.setValue((Attribute) newValue.getActorClassFavoredAttributes().toArray()[0]);
		actorClassFavoredAttributeTwoCB.setValue((Attribute) newValue.getActorClassFavoredAttributes().toArray()[1]);

		for (Object obj : newValue.getActorClassSkills().toArray()) {
			if (obj instanceof Skill skill)
				actorSpecializedSkills.getItems().add(skill);
		}
	}

}
