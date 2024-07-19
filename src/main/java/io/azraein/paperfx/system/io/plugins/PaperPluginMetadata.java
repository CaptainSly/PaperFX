package io.azraein.paperfx.system.io.plugins;

import java.util.List;

public class PaperPluginMetadata {

	private String pluginId;
	private String pluginName;
	private String pluginAuthor;
	private String pluginDescription;
	private String pluginPath;

	private boolean isMainFile;

	private List<String> pluginDependencies;

	public PaperPluginMetadata(String pluginId, String pluginName, String pluginAuthor, String pluginDescription,
			List<String> pluginDependencies, String pluginPath, boolean isMainFile) {
		this.pluginId = pluginId;
		this.pluginName = pluginName;
		this.pluginAuthor = pluginAuthor;
		this.pluginDescription = pluginDescription;
		this.pluginDependencies = pluginDependencies;
		this.pluginPath = pluginPath;
		this.isMainFile = isMainFile;
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

	public String getPluginPath() {
		return pluginPath;
	}

	public boolean isPluginMainFile() {
		return isMainFile;
	}

	public List<String> getPluginDependencies() {
		return pluginDependencies;
	}

}
