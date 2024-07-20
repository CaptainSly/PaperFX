package io.azraein.paperfx.system.io.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.azraein.paperfx.system.io.Database;

public class PaperPlugin implements Serializable {

	private static final long serialVersionUID = -3358471471692027114L;

	private String pluginId;
	private String pluginName;
	private String pluginAuthor;
	private String pluginDescription;
	private String pluginVersion;

	private boolean isMainFile;

	private List<String> pluginDependencies;

	private Database pluginDatabase;

	public PaperPlugin(String pluginId, String pluginName) {
		this.pluginId = pluginId;
		this.pluginName = pluginName;
		pluginDependencies = new ArrayList<>();
		pluginDatabase = new Database();
	}

	public PaperPlugin() {
		pluginDependencies = new ArrayList<>();
		pluginDatabase = new Database();
	}

	public void addDependency(PaperPlugin dependency) {
		pluginDependencies.add(dependency.getPluginId());
	}

	public String getPluginId() {
		return pluginId;
	}

	public String getPluginName() {
		return pluginName;
	}

	public String getPluginAuthor() {
		return pluginAuthor;
	}

	public String getPluginDescription() {
		return pluginDescription;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public boolean isMainFile() {
		return isMainFile;
	}

	public List<String> getPluginDependencies() {
		return pluginDependencies;
	}

	public Database getPluginDatabase() {
		return pluginDatabase;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public void setPluginAuthor(String pluginAuthor) {
		this.pluginAuthor = pluginAuthor;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription = pluginDescription;
	}

	public void setPluginDependencies(List<String> pluginDependencies) {
		this.pluginDependencies = pluginDependencies;
	}

	public void setPluginDatabase(Database pluginDatabase) {
		this.pluginDatabase = pluginDatabase;
	}

	public void setIsMainFile(boolean isMainFile) {
		this.isMainFile = isMainFile;
	}

}
