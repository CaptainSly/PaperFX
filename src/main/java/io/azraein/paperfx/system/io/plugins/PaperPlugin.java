package io.azraein.paperfx.system.io.plugins;

import java.io.Serializable;

import io.azraein.paperfx.system.io.Database;

public class PaperPlugin implements Serializable {

	private static final long serialVersionUID = -3358471471692027114L;

	private final PaperPluginMetadata metadata;
	private final Database pluginDatabase;

	public PaperPlugin(PaperPluginMetadata metadata, Database database) {
		this.metadata = metadata;
		this.pluginDatabase = database;
	}

	public PaperPlugin() {
		metadata = new PaperPluginMetadata();
		pluginDatabase = new Database();
	}

	public PaperPluginMetadata getMetadata() {
		return metadata;
	}

	public Database getPluginDatabase() {
		return pluginDatabase;
	}
}
