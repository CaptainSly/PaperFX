package io.azraein.paperfx.system.io.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tinylog.Logger;

import io.azraein.paperfx.system.exceptions.MissingPluginDependencyException;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;

public class PaperPluginLoader {

	private Map<String, PaperPlugin> loadedPlugins;

	public PaperPluginLoader() {
		loadedPlugins = new HashMap<>();
	}

	public Database loadPlugins(List<String> selectedPluginPaths) {
		try {
			// Step 1. Read Metadata of all selected plugins
			List<PaperPluginMetadata> metadataList = new ArrayList<>();
			for (String pluginPath : selectedPluginPaths) {
				PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(pluginPath);
				metadataList.add(metadata);
			}

			Logger.debug("Step 1. metadataList Size: " + metadataList.size());

			// Step 2. Separate MAIN Plugins from ADDON Plugins
			List<PaperPluginMetadata> mainPlugins = new ArrayList<>();
			List<PaperPluginMetadata> addonPlugins = new ArrayList<>();

			for (PaperPluginMetadata metadata : metadataList) {
				if (metadata.isPluginMainFile())
					mainPlugins.add(metadata);
				else
					addonPlugins.add(metadata);
			}

			Logger.debug(
					"Step 2. mainPlugins Size: " + mainPlugins.size() + ", addonPlugins Size: " + addonPlugins.size());
			;

			// Step 3. Sort MAIN Plugins based on dependencies.
			List<String> sortedMainPluginPaths = sortPluginsByDependencies(mainPlugins);

			// Load the MAIN Plugins first
			for (String pluginPath : sortedMainPluginPaths) {
				PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				Logger.debug("MAIN PLUGIN: " + plugin.getMetadata().getPluginId() + " is being added");
				addPlugin(plugin);
			}

			Logger.debug("Step 3. Sorted MAIN Plugins, loadedPlugins Size: " + loadedPlugins.size());

			// Step 4. Sort ADDON Plugins based on depedencies
			List<String> sortedAddonPluginPaths = sortPluginsByDependencies(addonPlugins);

			// Load the ADDON Plugins in sorted order
			for (String pluginPath : sortedAddonPluginPaths) {
				PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				Logger.debug("ADDON PLUGIN: " + plugin.getMetadata().getPluginId() + " is being added");
				addPlugin(plugin);
			}

			Logger.debug("Step 4. Sorted ADDON Plugins, loadedPlugins Size: " + loadedPlugins.size());

			// Step 5. Add plugin data to main database
			Database combinedPluginDatabase = new Database();
			for (Entry<String, PaperPlugin> plugin : loadedPlugins.entrySet()) {
				Database pluginDatabase = plugin.getValue().getPluginDatabase();
				combinedPluginDatabase.mergeDatabase(pluginDatabase);
			}

			Logger.debug(
					"Step 5. Combined Plugin Databases. Database Size: " + combinedPluginDatabase.getDatabaseSize());
			return combinedPluginDatabase;
		} catch (Exception e) {
			Logger.error("Error in plugin loading: " + e.getMessage());
		}

		return null;
	}

	public List<String> sortPluginsByDependencies(List<PaperPluginMetadata> metadataList) {
		// Maps Plugin IDs to their metadata
		Map<String, PaperPluginMetadata> metadataMap = new HashMap<>();

		// Maps Plugin IDs to their dependencies
		Map<String, List<String>> dependencyGraph = new HashMap<>();

		// Initialize metadata map and dependency graph
		Logger.debug("Initializing metadata map and dependency graph");
		for (PaperPluginMetadata metadata : metadataList) {
			metadataMap.put(metadata.getPluginId(), metadata);
			// Concerned about this, we can have MAIN plugins dependending on other MAIN
			// files.
			if (!metadata.isPluginMainFile())
				dependencyGraph.put(metadata.getPluginId(), metadata.getPluginDependencies());
		}
		Logger.debug("metadataMap Size: " + metadataMap.size() + ", dependencyGraph Size: " + dependencyGraph.size());

		// List to store the sorted plugin IDs
		List<String> sortedPlugins = new ArrayList<>();

		// Sets to keep track of visited and currently visiting plugins
		Set<String> visited = new HashSet<>();
		Set<String> visiting = new HashSet<>();

		// Perform topological sort on each plugin
		for (String pluginId : dependencyGraph.keySet()) {
			Logger.debug("Working on plugin: " + pluginId);
			if (!visited.contains(pluginId)) {
				try {
					topologicalSort(pluginId, dependencyGraph, visited, visiting, sortedPlugins);
				} catch (MissingPluginDependencyException e) {
					Logger.error("Missing plugin dependency detected: " + e.getMessage());
				}
			}
		}

		// Add Plugins with no dependencies (MAIN Plugins usually)
		for (PaperPluginMetadata metadata : metadataList) {
			if (metadata.isPluginMainFile() && !sortedPlugins.contains(metadata.getPluginId())) {
				Logger.debug("Adding Plugin: " + metadata.getPluginId());
				sortedPlugins.add(metadata.getPluginId());
			}
		}

		// Convert sorted plugin IDs to plugin paths
		List<String> sortedPluginPaths = new ArrayList<>();
		for (String pluginId : sortedPlugins) {
			PaperPluginMetadata metadata = metadataMap.get(pluginId);
			if (metadata != null)
				sortedPluginPaths.add(metadata.getPluginPath());
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

	public void addPlugin(PaperPlugin plugin) {
		loadedPlugins.put(plugin.getMetadata().getPluginId(), plugin);
	}

	public void clearLoadedPlugins() {
		loadedPlugins.clear();
	}

	public Map<String, PaperPlugin> getLoadedPlugins() {
		return loadedPlugins;
	}

}
