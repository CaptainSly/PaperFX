package io.azraein.paperfx;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.io.SaveSystem;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import io.azraein.paperfx.system.io.scripting.ScriptEngine;
import javafx.application.Application;
import javafx.stage.Stage;

public class PaperFX extends Application {

	public static final String PAPER_VERSION = "0.0.1";

	private PaperPluginLoader ppl;

	@Override
	public void init() throws Exception {
		super.init();

		SaveSystem.checkFileSystem();
		ppl = new PaperPluginLoader();
		Paper.PPL = ppl;

		// Initialize Scripting Engine last.
		Paper.SE = new ScriptEngine();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

}
