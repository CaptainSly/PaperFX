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
			Logger.debug("Step 1. metadataList Size: " + metadataList.size());

			// Step 2. Separate MAIN Plugins from addon plugins
			List<PaperPluginMetadata> mainPlugins = new ArrayList<>();
			List<PaperPluginMetadata> addonPlugins = new ArrayList<>();

			for (PaperPluginMetadata metadata : metadataList) {
				if (metadata.isPluginMainFile())
					mainPlugins.add(metadata);
				else
					addonPlugins.add(metadata);
			}
			Logger.debug("Step 2. Separate MAIN and ADDON Plugins, MAIN = " + mainPlugins.size() + ", ADDONS = "
					+ addonPlugins.size());

			// Step 3. Sort Main Plugins based on dependencies
			List<String> sortedMainPluginPaths = sortPluginsByDependencies(mainPlugins);

			// Load the MAIN Plugins first, absolutely no other way. THESE NEED TO BE THERE.
			for (String pluginPath : sortedMainPluginPaths) {
				PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				addPlugin(plugin);
			}
			Logger.debug(
					"Step 3. Sort and add MAIN Plugins to loadedPlugins list. LoadedPlugins: " + loadedPlugins.size());

			// Step 4. Sort Addon Plugins based on dependencies
			List<String> sortedPluginPaths = sortPluginsByDependencies(addonPlugins);

			// Load Plugins in sorted order
			for (String pluginPath : sortedPluginPaths) {
				PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				addPlugin(plugin);
			}
			Logger.debug(
					"Step 5. Sort and add ADDON Plugins to loadedPlugins list. LoadedPlugins: " + loadedPlugins.size());

			// Step 5. Add plugin data to main database
			Database combinedPluginDatabase = new Database();
			for (Entry<String, PaperPlugin> plugin : loadedPlugins.entrySet()) {
				Database pluginDatabase = plugin.getValue().getPluginDatabase();
				combinedPluginDatabase.mergeDatabase(pluginDatabase);
			}

			return combinedPluginDatabase;
		} catch (Exception e) {
			Logger.error("Error during Plugin loading", e);
			e.printStackTrace();
		}

		return null;
	}

	public void addPlugin(PaperPlugin paperPlugin) {
		loadedPlugins.put(paperPlugin.getPluginId(), paperPlugin);
	}

	private List<String> sortPluginsByDependencies(List<PaperPluginMetadata> metadataList) {
		// Maps plugin IDs to their metadata
		Map<String, PaperPluginMetadata> metadataMap = new HashMap<>();
		// Maps plugin IDs to their dependencies
		Map<String, List<String>> dependencyGraph = new HashMap<>();

		// Initialize metadata map and dependency graph
		for (PaperPluginMetadata metadata : metadataList) {
			metadataMap.put(metadata.getPluginId(), metadata);
			if (!metadata.isPluginMainFile()) {
				dependencyGraph.put(metadata.getPluginId(), metadata.getPluginDependencies());
			}
		}

		// List to store the sorted plugin IDs
		List<String> sortedPlugins = new ArrayList<>();
		// Sets to keep track of visited and currently visiting plugins
		Set<String> visited = new HashSet<>();
		Set<String> visiting = new HashSet<>();

		// Perform topological sort on each plugin
		for (String pluginId : dependencyGraph.keySet()) {
			if (!visited.contains(pluginId)) {
				try {
					topologicalSort(pluginId, dependencyGraph, visited, visiting, sortedPlugins);
				} catch (MissingPluginDependencyException e) {
					Logger.error("Missing plugin dependency detected: " + e.getMessage());
				}
			}
		}

		// Add plugins with no dependencies (main plugins)
		for (PaperPluginMetadata metadata : metadataList) {
			if (metadata.isPluginMainFile() && !sortedPlugins.contains(metadata.getPluginId())) {
				sortedPlugins.add(metadata.getPluginId());
			}
		}

		// Convert sorted plugin IDs to plugin paths
		List<String> sortedPluginPaths = new ArrayList<>();
		for (String pluginId : sortedPlugins) {
			PaperPluginMetadata metadata = metadataMap.get(pluginId);
			if (metadata != null) {
				sortedPluginPaths.add(metadata.getPluginPath());
			}
		}

		return sortedPluginPaths;
	}

	private void topologicalSort(String pluginId, Map<String, List<String>> dependencyGraph, Set<String> visited,
			Set<String> visiting, List<String> sortedPlugins) throws MissingPluginDependencyException {
		// Detect circular dependencies
		if (visiting.contains(pluginId)) {
			throw new IllegalStateException("Circular dependency detected involving plugin: " + pluginId);
		}

		// If not already visited
		if (!visited.contains(pluginId)) {
			visiting.add(pluginId);

			// Recursively sort dependencies
			List<String> dependencies = dependencyGraph.get(pluginId);
			if (dependencies != null) {
				for (String dependency : dependencies) {
					if (!dependencyGraph.containsKey(dependency)) {
						throw new MissingPluginDependencyException(
								"Plugin " + pluginId + " depends on a non-existent plugin: " + dependency);
					}
					topologicalSort(dependency, dependencyGraph, visited, visiting, sortedPlugins);
				}
			}

			// Mark the current plugin as visited and add to sorted list
			visiting.remove(pluginId);
			visited.add(pluginId);
			sortedPlugins.add(pluginId);
		}
	}

	public Map<String, PaperPlugin> getLoadedPlugins() {
		return loadedPlugins;
	}

}
