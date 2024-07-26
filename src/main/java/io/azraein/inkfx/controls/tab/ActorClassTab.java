package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.stats.Attribute;
import io.azraein.paperfx.system.actors.stats.Skill;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

public class ActorClassTab extends PaperEditorTab {

	private TextField actorClassIdFld;
	private TextField actorClassNameFld;
	private TextArea actorClassDescriptionArea;
	private ListView<Skill> classSkills;
	private Spinner<Integer>[] actorClassSkillBonusSpinners;
	private ComboBox<Attribute> actorClassFavoredAttributeOneCB;
	private ComboBox<Attribute> actorClassFavoredAttributeTwoCB;

	@SuppressWarnings("unchecked")
	public ActorClassTab(InkFX inkFX) {
		super(inkFX);
		setText("Class Editor");
		setClosable(false);

		Label actorClassIdLbl = new Label("Class ID");
		Label actorClassNameLbl = new Label("Class Name");
		Label actorClassDescriptionLbl = new Label("Class Description");

		actorClassSkillBonusSpinners = new Spinner[Skill.values().length];
		Label[] actorClassSkillBonusLbls = new Label[Skill.values().length];
		for (Skill skill : Skill.values()) {
			actorClassSkillBonusLbls[skill.ordinal()] = new Label(Utils.toNormalCase(skill.name()) + " Bonus");
			actorClassSkillBonusSpinners[skill.ordinal()] = new Spinner<>(0, 999, 0);
			actorClassSkillBonusSpinners[skill.ordinal()].setMaxWidth(80);
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

		Label actorClassSkills = new Label("Class Skills");
		classSkills = new ListView<>();
		classSkills.setCellFactory(listView -> new ListCell<Skill>() {
			@Override
			public void updateItem(Skill item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					this.setText(Utils.toNormalCase(item.name()));
					ContextMenu cm = new ContextMenu();
					MenuItem removeSkillItem = new MenuItem("Remove Skill");
					cm.getItems().add(removeSkillItem);

					removeSkillItem.setOnAction(event -> {
						classSkills.getItems().remove(item);
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
				if (!classSkills.getItems().contains(skillChoice.getValue())) { // Make sure the list doesn't contain
																				// the skill already
					if (classSkills.getItems().size() < 6) { // Make sure the list doesn't already have 6 skills.
						classSkills.getItems().add(skillChoice.getValue());
					}
				}
			}
		});

		actorClassIdFld = new TextField();
		actorClassNameFld = new TextField();

		actorClassDescriptionArea = new TextArea();
		actorClassDescriptionArea.setWrapText(true);

		ListView<ActorClass> actorClassList = new ListView<>();
		actorClassList.getItems().addAll(inkFX.getCharClassList().values());
		inkFX.getCharClassList().addListener(new MapChangeListener<String, ActorClass>() {

			@Override
			public void onChanged(Change<? extends String, ? extends ActorClass> change) {

				if (change.wasAdded()) {
					if (inkFX.currentPluginProperty().get().getPluginDatabase().getCharClassList()
							.containsKey(change.getKey()))
						actorClassList.getItems().remove(change.getValueRemoved());
					actorClassList.getItems().add(change.getValueAdded());
				} else if (change.wasRemoved()) {
					actorClassList.getItems().remove(change.getValueRemoved());
				}

			}

		});
		actorClassList.setCellFactory(listView -> new ListCell<ActorClass>() {

			@Override
			public void updateItem(ActorClass item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					setText(item.getActorClassId());
				} else {
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
			if (inkFX.getCharClassList().containsKey(actorClassId)) {
				actorClass = inkFX.getCharClassList().get(actorClassId);
			}

			if (actorClass == null)
				actorClass = new ActorClass(actorClassId, actorClassName);

			actorClass.setActorClassName(actorClassName);
			actorClass.setActorClassDescription(actorClassDescriptionArea.getText());
			actorClass.setActorClassFavoredAttribute(actorClassFavoredAttributeOneCB.getValue(),
					actorClassFavoredAttributeTwoCB.getValue());
			actorClass.setActorClassSkills(classSkills.getItems());

			for (Skill skill : Skill.values())
				actorClass.getActorSkillBonuses()[skill.ordinal()] = actorClassSkillBonusSpinners[skill.ordinal()]
						.getValue();

			inkFX.currentPluginProperty().get().getPluginDatabase().addCharacterClass(actorClass);
			inkFX.getCharClassList().put(actorClass.getActorClassId(), actorClass);
		});

		GridPane actorClassInfoPane = new GridPane();
		GridPane.setColumnSpan(actorClassDescriptionArea, 4);
		GridPane.setColumnSpan(classSkills, 2);
		GridPane.setRowSpan(classSkills, 12);
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
		actorClassInfoPane.add(classSkills, 0, 8);
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

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				if (index < Skill.values().length) {
					int curCol = 1 + col;
					int curRow = 8 + row;
					actorClassInfoPane.add(actorClassSkillBonusLbls[index], curCol * 2, curRow);
					actorClassInfoPane.add(actorClassSkillBonusSpinners[index], curCol * 2 + 1, curRow);
					index++;
				}
			}
		}

		BorderPane rootPane = new BorderPane();
		rootPane.setLeft(actorClassList);
		rootPane.setCenter(actorClassInfoPane);

		setContent(rootPane);
	}
	
	private void clearData() {
		actorClassIdFld.setText("");
		actorClassNameFld.setText("");
		actorClassDescriptionArea.setText("");

		for (Skill skill : Skill.values()) {
			actorClassSkillBonusSpinners[skill.ordinal()].getValueFactory().setValue(0);
		}

		actorClassFavoredAttributeOneCB.getSelectionModel().select(0);
		actorClassFavoredAttributeTwoCB.getSelectionModel().select(1);

		classSkills.getItems().clear();
	}

	private void loadData(ActorClass newValue) {
		actorClassIdFld.setText(newValue.getActorClassId());
		actorClassNameFld.setText(newValue.getActorClassName());
		actorClassDescriptionArea.setText(newValue.getActorClassDescription());

		for (Skill skill : Skill.values()) {
			actorClassSkillBonusSpinners[skill.ordinal()].getValueFactory()
					.setValue(newValue.getActorSkillBonuses()[skill.ordinal()]);
		}

		actorClassFavoredAttributeOneCB
				.setValue((Attribute) newValue.getActorClassFavoredAttributes().toArray()[0]);
		actorClassFavoredAttributeTwoCB
				.setValue((Attribute) newValue.getActorClassFavoredAttributes().toArray()[1]);

		for (Object obj : newValue.getActorClassSkills().toArray()) {
			if (obj instanceof Skill)
				classSkills.getItems().add(((Skill) obj));
		}
	}
	
}
