package io.azraein.paperfx.system.io.plugins;

import java.util.*;
import java.util.Map.Entry;

import org.tinylog.Logger;

import io.azraein.paperfx.system.exceptions.MissingPluginDependencyException;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;

public class PaperPluginLoader {

	private Map<String, PaperPlugin> loadedPlugins;

	public PaperPluginLoader() {
		loadedPlugins = new HashMap<>();
	}

	public Database loadPlugins(List<String> selectedPlugins) {
		try {

			// Step 1. Read metadata of all selected plugins
			List<PaperPluginMetadata> metadataList = new ArrayList<>();
			for (String pluginPath : selectedPlugins) {
				PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(pluginPath);
				metadataList.add(metadata);
			}

			// Step 2. Separate MAIN Plugins from addon plugins
			List<PaperPluginMetadata> mainPlugins = new ArrayList<>();
			List<PaperPluginMetadata> addonPlugins = new ArrayList<>();

			for (PaperPluginMetadata metadata : metadataList) {
				if (metadata.isPluginMainFile())
					mainPlugins.add(metadata);
				else
					addonPlugins.add(metadata);
			}

			// Load the MAIN Plugins first, absolutely no other way. THESE NEED TO BE THERE.
			for (PaperPluginMetadata metadata : mainPlugins) {
				PaperPlugin plugin = SaveSystem.loadPlugin(metadata.getPluginPath());
				addPlugin(plugin);
			}

			// Step 3. Sort plugins based on dependencies
			List<String> sortedPluginPaths = sortPluginsByDependencies(addonPlugins);

			// Step 4. Load Plugins in sorted order
			for (String pluginPath : sortedPluginPaths) {
				PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				addPlugin(plugin);
			}

			// Step 5. Add plugin data to main database
			Database combinedPluginDatabase = new Database();
			for (Entry<String, PaperPlugin> plugin : loadedPlugins.entrySet()) {
				Database pluginDatabase = plugin.getValue().getPluginDatabase();
				combinedPluginDatabase.mergeDatabase(pluginDatabase);
			}

			return combinedPluginDatabase;
		} catch (Exception e) {
			Logger.error("Error during Plugin loading", e);
		}

		return null;
	}

	public void addPlugin(PaperPlugin paperPlugin) {
		loadedPlugins.put(paperPlugin.getPluginId(), paperPlugin);
	}

	private List<String> sortPluginsByDependencies(List<PaperPluginMetadata> metadataList) {
		Map<String, PaperPluginMetadata> metadataMap = new HashMap<>();
		Map<String, List<String>> dependencyGraph = new HashMap<>();
		Set<String> mainPluginIds = new HashSet<>();

		for (PaperPluginMetadata metadata : metadataList) {
			metadataMap.put(metadata.getPluginId(), metadata);
			if (metadata.isPluginMainFile())
				mainPluginIds.add(metadata.getPluginId());
			else
				dependencyGraph.put(metadata.getPluginId(), metadata.getPluginDependencies());
		}

		List<String> sortedPlugins = new ArrayList<>();
		Set<String> visited = new HashSet<>();
		Set<String> visiting = new HashSet<>();

		// Add dependency-less plugins first
		for (String pluginId : dependencyGraph.keySet()) {
			if (dependencyGraph.get(pluginId).isEmpty()) {
				sortedPlugins.add(pluginId);
				visited.add(pluginId);
			}
		}

		for (String pluginId : dependencyGraph.keySet()) {
			if (!visited.contains(pluginId))
				try {
					topologicalSort(pluginId, dependencyGraph, visited, visiting, sortedPlugins);
				} catch (MissingPluginDependencyException e) {
					Logger.error("There seems to be a missing dependency while loading plugins", e);
				}
		}

		List<String> sortedPluginPaths = new ArrayList<>();
		for (String pluginId : sortedPlugins) {
			for (PaperPluginMetadata metadata : metadataList) {
				if (metadata.getPluginId().equals(pluginId)) {
					if (metadata != null)
						sortedPluginPaths.add(metadata.getPluginPath());
				}
			}
		}

		return sortedPluginPaths;
	}

	private void topologicalSort(String pluginId, Map<String, List<String>> dependencyGraph, Set<String> visited,
			Set<String> visiting, List<String> sortedPlugins) throws MissingPluginDependencyException {

		if (visiting.contains(pluginId))
			throw new IllegalStateException("Circular Dependency detected involving Plugin: " + pluginId);

		if (!visited.contains(pluginId)) {
			visiting.add(pluginId);
			for (String dependency : dependencyGraph.get(pluginId)) {

				if (!dependencyGraph.containsKey(dependency))
					throw new MissingPluginDependencyException(pluginId + " is missing a dependency: " + dependency);

				topologicalSort(dependency, dependencyGraph, visited, visiting, sortedPlugins);
			}

			visiting.remove(pluginId);
			visited.add(pluginId);
			sortedPlugins.add(pluginId);
		}

	}

	public Map<String, PaperPlugin> getLoadedPlugins() {
		return loadedPlugins;
	}

}
