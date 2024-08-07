package io.azraein.paperfx.system.io.plugins;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.tinylog.Logger;

import io.azraein.paperfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.paperfx.system.exceptions.MissingPluginDependencyException;
import io.azraein.paperfx.system.exceptions.PluginCorruptionException;
import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.SaveSystem;

public class PaperPluginLoader {

	private final Map<String, PaperPlugin> loadedPlugins;

	private String pluginMainScript;

	public PaperPluginLoader() {
		loadedPlugins = new HashMap<>();
	}

	public Database loadPlugins(final List<String> selectedPluginPaths) {
		try {
			// Step 1. Read Metadata of all selected plugins
			final List<PaperPluginMetadata> metadataList = new ArrayList<>();
			for (final String pluginPath : selectedPluginPaths) {
				final PaperPluginMetadata metadata = SaveSystem.readPluginMetadata(pluginPath);
				Logger.debug("Current Dependency Size for Metadata: " + metadata.getPluginDependencies().size());
				metadataList.add(metadata);
			}

			Logger.debug("Step 1. metadataList Size: " + metadataList.size());

			// Step 2. Sort Plugins based on depedencies
			final List<String> sortedAddonPluginPaths = sortPluginsByDependencies(metadataList);

			// Load thePlugins in sorted order
			for (final String pluginPath : sortedAddonPluginPaths) {
				final PaperPlugin plugin = SaveSystem.loadPlugin(pluginPath);
				Logger.debug("PLUGIN: " + plugin.getMetadata().getPluginId() + " is being added");
				addPlugin(plugin);
			}

			Logger.debug("Step 2. Sorted Plugins, loadedPlugins Size: " + loadedPlugins.size());

			// Step 3. Add plugin data to main database
			final Database combinedPluginDatabase = new Database();
			for (final Entry<String, PaperPlugin> plugin : loadedPlugins.entrySet()) {
				final Database pluginDatabase = plugin.getValue().getPluginDatabase();
				combinedPluginDatabase.mergeDatabase(pluginDatabase);
			}

			Logger.debug(
					"Step 3. Combined Plugin Databases. Database Size: " + combinedPluginDatabase.getDatabaseSize());
			return combinedPluginDatabase;
		} catch (IncompatiblePluginVersionException | PluginCorruptionException | IOException e) {
			Logger.error("Error in plugin loading: " + e.getMessage());
		}

		return null;
	}

	public List<String> sortPluginsByDependencies(final List<PaperPluginMetadata> metadataList) {
		// Maps Plugin IDs to their metadata
		final Map<String, PaperPluginMetadata> metadataMap = new HashMap<>();

		// Maps Plugin IDs to their dependencies
		final Map<String, List<String>> dependencyGraph = new HashMap<>();

		// Initialize metadata map and dependency graph
		Logger.debug("Initializing metadata map and dependency graph");
		for (final PaperPluginMetadata metadata : metadataList) {
			metadataMap.put(metadata.getPluginId(), metadata);
			dependencyGraph.put(metadata.getPluginId(), metadata.getPluginDependencies());
		}
		Logger.debug("metadataMap Size: " + metadataMap.size() + ", dependencyGraph Size: " + dependencyGraph.size());

		// List to store the sorted plugin IDs
		final List<String> sortedPlugins = new ArrayList<>();

		// Sets to keep track of visited and currently visiting plugins
		final Set<String> visited = new HashSet<>();
		final Set<String> visiting = new HashSet<>();

		// Perform topological sort on each plugin
		for (final String pluginId : dependencyGraph.keySet()) {
			Logger.debug("Working on plugin: " + pluginId);
			if (!visited.contains(pluginId)) {

				// Add Plugins with no dependencies (MAIN Plugins usually)
				for (final PaperPluginMetadata metadata : metadataList) {
					if (metadata.isPluginMainFile() && !sortedPlugins.contains(metadata.getPluginId())) {
						if (!sortedPlugins.contains(metadata.getPluginId())) {
							Logger.debug("Adding MAIN Plugin: " + metadata.getPluginId());
							Logger.debug("MAIN Plugin Main Script: " + metadata.getPluginMainScript());
							pluginMainScript = metadata.getPluginMainScript();
							sortedPlugins.add(metadata.getPluginId());
							visited.add(pluginId);
							break;
						}
					}
				}
				if (!visited.contains(pluginId)) {
					try {
						topologicalSort(pluginId, dependencyGraph, visited, visiting, sortedPlugins);
					} catch (final MissingPluginDependencyException e) {
						Logger.error("Missing plugin dependency detected: " + e.getMessage());
					}
				}
			}
		}

		// Convert sorted plugin IDs to plugin paths
		final List<String> sortedPluginPaths = new ArrayList<>();
		for (final String pluginId : sortedPlugins) {
			final PaperPluginMetadata metadata = metadataMap.get(pluginId);
			if (metadata != null)
				sortedPluginPaths.add(metadata.getPluginPath());
		}

		return sortedPluginPaths;
	}

	private void topologicalSort(final String pluginId, final Map<String, List<String>> dependencyGraph,
			final Set<String> visited, final Set<String> visiting, final List<String> sortedPlugins)
			throws MissingPluginDependencyException {
		// Detect circular dependencies
		if (visiting.contains(pluginId)) {
			throw new IllegalStateException("Circular dependency detected involving plugin: " + pluginId);
		}

		// If not already visited
		if (!visited.contains(pluginId)) {
			visiting.add(pluginId);

			// Recursively sort dependencies
			final List<String> dependencies = dependencyGraph.get(pluginId);
			if (dependencies != null) {
				for (final String dependency : dependencies) {
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

	public void addPlugin(final PaperPlugin plugin) {
		loadedPlugins.put(plugin.getMetadata().getPluginId(), plugin);
	}

	public void clearLoadedPlugins() {
		loadedPlugins.clear();
	}

	public Map<String, PaperPlugin> getLoadedPlugins() {
		return loadedPlugins;
	}

	public String getPluginMainScript() {
		return pluginMainScript;
	}

}
