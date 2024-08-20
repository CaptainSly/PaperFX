package io.azraein.inkfx.controls.tab.itemTabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.PaperEditorTab;
import io.azraein.paperfx.system.Utils;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.inventory.items.ItemType;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.StringConverter;

public class ItemTab extends PaperEditorTab {

    private TextField itemIdFld, itemNameFld, itemImageFld, itemScriptFld;
    private TextArea itemDescriptionArea;

    private ComboBox<ItemType> itemTypeCB;

    private Spinner<Double> itemWeightSpinner;

    public ItemTab(InkFX inkFX) {
        super(inkFX);
        setText("Item Editor");

        // #region Create Controls
        Label itemIdLbl = new Label("Item Id");
        Label itemNameLbl = new Label("Item Name");
        Label itemDescriptionLbl = new Label("Item Description");
        Label itemTypeLbl = new Label("Item Type");
        Label itemImageLbl = new Label("Item Graphic");
        Label itemScriptLbl = new Label("Item Script");
        Label itemWeightLbl = new Label("Item Weight");

        itemIdFld = new TextField();
        itemNameFld = new TextField();

        itemImageFld = new TextField();
        itemImageFld.setEditable(false);

        itemScriptFld = new TextField();
        itemScriptFld.setEditable(false);

        itemDescriptionArea = new TextArea();
        itemDescriptionArea.setWrapText(true);

        itemTypeCB = new ComboBox<>();
        itemTypeCB.setCellFactory(listView -> new ListCell<ItemType>() {

            @Override
            public void updateItem(ItemType item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    this.setText(Utils.toNormalCase(item.name()));
                } else {
                    this.setText("");
                }
            }

        });
        itemTypeCB.setConverter(new StringConverter<ItemType>() {

            @Override
            public String toString(ItemType object) {
                if (object != null)
                    return Utils.toNormalCase(object.name());
                else
                    return "";
            }

            @Override
            public ItemType fromString(String string) {

                for (ItemType type : ItemType.values()) {

                    if (type.name().equalsIgnoreCase(string))
                        return type;

                }

                return null;
            }

        });
        itemTypeCB.getSelectionModel().select(0);
        itemTypeCB.getItems().addAll(ItemType.values());

        itemWeightSpinner = new Spinner<>(0.0, Double.MAX_VALUE, 0.0, 0.01);
        itemWeightSpinner.setEditable(true);

        ImageView itemImageView = new ImageView();

        Button chooseItemImageButton = new Button("Select Item Graphic");
        chooseItemImageButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(SaveSystem.PAPER_GFX_FOLDER));
            fc.getExtensionFilters().addAll(new ExtensionFilter("PNG", "*.png"), new ExtensionFilter("JPG", "*.jpg"),
                    new ExtensionFilter("JPEG", "*.jpeg"));

            File image = fc.showOpenDialog(inkFX.getPrimaryStage().getOwner());
            if (image.exists()) {
                itemImageFld.setText(image.getName());
                try {
                    itemImageView.setImage(new Image(new FileInputStream(image)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        Button chooseItemScriptButton = new Button("Select Item Script");
        chooseItemScriptButton.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(SaveSystem.PAPER_SCRIPT_FOLDER));
            fc.getExtensionFilters().add(new ExtensionFilter("Lua", "*.lua"));

            File script = fc.showOpenDialog(inkFX.getPrimaryStage().getOwner());
            if (script.exists()) {
                itemScriptFld.setText(script.getName());
            }
        });

        Button removeItemImageButton = new Button("Remove Item Graphic");
        removeItemImageButton.setOnAction(event -> {
            itemImageFld.setText("");
            itemImageView.setImage(null);
        });

        Button removeItemScriptButton = new Button("Remove Item Script");
        removeItemScriptButton.setOnAction(event -> {
            itemScriptFld.setText("");
        });

        Button saveItemButton = new Button("Save Item");
        saveItemButton.setOnAction(event -> {
            String itemId = itemIdFld.getText();
            String itemName = itemNameFld.getText();
            String itemDescription = itemDescriptionArea.getText();
            ItemType it = itemTypeCB.getValue();

            Item item = new Item(itemId, it, itemName, itemDescription);
            item.setItemWeight(itemWeightSpinner.getValue());
            item.setItemImage(itemImageFld.getText());
            item.setItemScript(itemScriptFld.getText());

            inkFX.currentPluginProperty().get().getPluginDatabase().addItem(item);
            inkFX.getObservableItemRegistry().put(item.getItemId(), item);
        });

        Button clearItemFormButton = new Button("Clear Item Form");
        clearItemFormButton.setOnAction(event -> {
            itemIdFld.setText("");
            itemNameFld.setText("");
            itemDescriptionArea.setText("");
            itemScriptFld.setText("");
            itemTypeCB.getSelectionModel().select(0);
            itemWeightSpinner.getValueFactory().setValue(0.0);
        });

        ListView<Item> dbItemLV = new ListView<>();
        dbItemLV.setCellFactory(listView -> new ListCell<Item>() {

            @Override
            public void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                if (item != null) {
                    setText(item.getItemId());
                    ContextMenu cm = new ContextMenu();
                    MenuItem removeItem = new MenuItem("Remove Race");
                    removeItem.setOnAction(event -> {
                        Item _item = dbItemLV.getSelectionModel().getSelectedItem();

                        if (inkFX.currentPluginProperty().get() != null) {
                            Database curPluginDb = inkFX.currentPluginProperty().get().getPluginDatabase();
                            if (curPluginDb.getItemRegistry().containsKey(_item.getItemId())) {
                                curPluginDb.getItemRegistry().remove(_item.getItemId());
                                inkFX.getObservableItemRegistry().remove(_item.getItemId());
                            }
                        }
                    });

                    cm.getItems().add(removeItem);
                    setContextMenu(cm);
                } else {
                    setText("");
                    setContextMenu(null);
                }
            }

        });
        dbItemLV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                itemIdFld.setText(newValue.getItemId());
                itemNameFld.setText(newValue.getItemName());
                itemDescriptionArea.setText(newValue.getItemDescription());
                itemImageFld.setText(newValue.getItemImage());
                itemScriptFld.setText(newValue.getItemScript());
                itemTypeCB.setValue(newValue.getItemType());
                itemWeightSpinner.getValueFactory().setValue(newValue.getItemWeight());
            }

        });
        dbItemLV.getItems().addAll(inkFX.getObservableItemRegistry().values());

        // #endregion

        // #region Listener setup

        inkFX.getObservableItemRegistry().addListener((MapChangeListener<String, Item>) change -> {

            if (change.wasAdded()) {
                if (inkFX.currentPluginProperty().get().getPluginDatabase().getItemRegistry()
                        .containsKey(change.getKey())) {
                    dbItemLV.getItems().remove(change.getValueRemoved());
                    dbItemLV.getItems().add(change.getValueAdded());
                } else {
                    dbItemLV.getItems().add(change.getValueAdded());
                }
            } else if (change.wasRemoved()) {
                dbItemLV.getItems().remove(change.getValueRemoved());
            }

        });

        // #endregion

        // #region Add Controls

        GridPane gp = new GridPane(10, 10);
        gp.setPadding(new Insets(15));
        gp.add(itemIdLbl, 0, 0);
        gp.add(itemIdFld, 1, 0);
        gp.add(itemNameLbl, 2, 0);
        gp.add(itemNameFld, 3, 0);

        gp.add(itemDescriptionLbl, 0, 1);
        gp.add(itemDescriptionArea, 1, 1, 4, 2);

        gp.add(itemTypeLbl, 0, 3);
        gp.add(itemTypeCB, 1, 3);
        gp.add(itemWeightLbl, 2, 3);
        gp.add(itemWeightSpinner, 3, 3);

        gp.add(itemImageLbl, 0, 4);
        gp.add(itemImageFld, 1, 4);
        gp.add(chooseItemImageButton, 2, 4);
        gp.add(removeItemImageButton, 3, 4);
        gp.add(itemImageView, 4, 4, 2, 2);

        gp.add(itemScriptLbl, 0, 5);
        gp.add(itemScriptFld, 1, 5);
        gp.add(chooseItemScriptButton, 2, 5);
        gp.add(removeItemScriptButton, 3, 5);

        gp.add(saveItemButton, 0, 6);
        gp.add(clearItemFormButton, 1, 6);

        content.setLeft(dbItemLV);
        content.setCenter(gp);

        // #endregion

    }

}
