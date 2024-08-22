package io.azraein.paperfx.ui.controls.dialog.player;

import java.util.List;

import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;

public class PluginSelectionResult {
    private List<PaperPluginMetadata> selectedPlugins;
    private List<String> selectedPluginPaths;
    private PaperPluginMetadata activePlugin;

    public PluginSelectionResult(List<PaperPluginMetadata> selectedPlugins, List<String> selectedPluginPaths, PaperPluginMetadata activePlugin) {
        this.selectedPlugins = selectedPlugins;
        this.selectedPluginPaths = selectedPluginPaths;
        this.activePlugin = activePlugin;
    }

    public List<PaperPluginMetadata> getSelectedPlugins() {
        return selectedPlugins;
    }

    public List<String> getSelectedPluginPaths() {
        return selectedPluginPaths;
    }

    public PaperPluginMetadata getActivePlugin() {
        return activePlugin;
    }
}
