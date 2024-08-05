package io.azraein.paperfx.system.io.plugins;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PaperPluginMetadata implements Serializable {

	private static final long serialVersionUID = -6663876549074108161L;

	private String pluginId;
	private String pluginName;
	private String pluginAuthor;
	private String pluginDescription;
	private String pluginVersion;
	private String pluginPath;

	private boolean isPluginMainFile;
	private String pluginMainScript;

	private List<String> pluginDependencies;

	public PaperPluginMetadata(String pluginId, String pluginName, String pluginAuthor, String pluginDescription,
			String pluginVersion, List<String> pluginDependencies, 
			String pluginPath, boolean isMainFile, String pluginMainScript) {
		this.pluginId = pluginId;
		this.pluginName = pluginName;
		this.pluginAuthor = pluginAuthor;
		this.pluginDescription = pluginDescription;
		this.pluginVersion = pluginVersion;
		this.pluginDependencies = pluginDependencies;
		this.pluginPath = pluginPath;
		this.isPluginMainFile = isMainFile;
	}

	public PaperPluginMetadata() {
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
		return isPluginMainFile;
	}

	public List<String> getPluginDependencies() {
		return pluginDependencies;
	}
	public String getPluginVersion() {
		return pluginVersion;
	}

	public String getPluginMainScript() {
		return pluginMainScript;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public void setPluginMainScript(String pluginMainScript) {
		this.pluginMainScript = pluginMainScript;
	}

	public void setPluginAuthor(String pluginAuthor) {
		this.pluginAuthor = pluginAuthor;
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription = pluginDescription;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}

	public void setPluginPath(String pluginPath) {
		this.pluginPath = pluginPath;
	}

	public void setPluginMainFile(boolean isPluginMainFile) {
		this.isPluginMainFile = isPluginMainFile;
	}

	public void setPluginDependencies(List<String> pluginDependencies) {
		this.pluginDependencies = pluginDependencies;
	}

}
