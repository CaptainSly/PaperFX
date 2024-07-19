package io.azraein.inkfx;

import java.util.List;
import java.util.Optional;

import io.azraein.inkfx.controls.tab.PluginMetadataTab;
import io.azraein.inkfx.dialog.PluginSelectionDialog;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class InkFX extends Application {

	private BorderPane rootPane;
	private TabPane tabPane;

	private PaperPluginLoader ppl;

	private ObjectProperty<PaperPlugin> currentPluginProperty = new SimpleObjectProperty<>();
	private ObjectProperty<Database> databaseProperty = new SimpleObjectProperty<>();

	@Override
	public void init() throws Exception {
		super.init();
		ppl = new PaperPluginLoader();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		rootPane = new BorderPane();

		PluginMetadataTab pmt = new PluginMetadataTab(this);

		tabPane = new TabPane();
		tabPane.getTabs().add(pmt);

		// MenuBar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem newPluginItem = new MenuItem("New Plugin");
		newPluginItem.setOnAction(event -> {
			databaseProperty.set(new Database());
		});

		MenuItem openPluginItem = new MenuItem("Open Plugin");
		openPluginItem.setOnAction(event -> {
			PluginSelectionDialog psd = new PluginSelectionDialog();
			Optional<Pair<List<String>, PaperPlugin>> selectedPlugins = psd.showAndWait();
			if (selectedPlugins.isPresent()) {
				pmt.setDependencies(selectedPlugins.get().getKey());
				databaseProperty.set(ppl.loadPlugins(selectedPlugins.get().getKey()));
				currentPluginProperty.set(selectedPlugins.get().getValue());
			}
		});

		MenuItem savePluginItem = new MenuItem("Save Plugin");
		savePluginItem.setOnAction(event -> {

		});

		fileMenu.getItems().addAll(newPluginItem, openPluginItem, savePluginItem);
		menuBar.getMenus().addAll(fileMenu);

		rootPane.setTop(menuBar);
		rootPane.setCenter(tabPane);

		primaryStage.setScene(new Scene(rootPane, 1280, 720));
		primaryStage.setTitle("Ink");
		primaryStage.show();
	}

	public Database getCurrentPluginDatabase() {
		return databaseProperty.get();
	}

}
