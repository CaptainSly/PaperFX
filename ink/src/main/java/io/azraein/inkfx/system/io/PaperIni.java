package io.azraein.inkfx.system.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.Ini;
import org.tinylog.Logger;

import io.azraein.inkfx.system.Options;

public class PaperIni {

	private Ini ini;

	public static final String INI_NAME = SaveSystem.PAPER_FOLDER + "paperfx.ini";

	// Sections
	public static final String SYSTEM_SECTION = "System";

	public static final String PLUGIN_SECTION = "PluginLoader";

	// Keys

	// System
	public static final String SYSTEM_AUTO_SAVE_DURATION = "iAutoSaveDuration";
	public static final String SYSTEM_MAX_AUTO_SAVES = "iMaxAutoSaves";
	public static final String SYSTEM_DO_AUTO_SAVE = "bDoAutoSave";

	// Plugin Loader
	public static final String PPL_SELECTED_PLUGINS = "slLoadedPlugins";

	public PaperIni() {
		try {
			File iniFile = new File(INI_NAME);
			if (iniFile.exists()) {
				ini = new Ini(iniFile);
			} else {
				ini = new Ini();
				ini.setComment("PaperFX INI - Contains Options for both the Game Engine and the Game Editor");
				ini.setFile(iniFile);

				put(SYSTEM_SECTION, SYSTEM_AUTO_SAVE_DURATION, Options.autoSaveDuration);
				put(SYSTEM_SECTION, SYSTEM_MAX_AUTO_SAVES, Options.maxAutoSaves);
				put(SYSTEM_SECTION, SYSTEM_DO_AUTO_SAVE, Options.doAutoSave);

				ini.store();
			}
		} catch (IOException e) {
			Logger.error("Failed to load or create INI file", e);
		}

		if (ini == null) {
			ini = new Ini();
			ini.setFile(new File(INI_NAME));
		}

	}

	public void put(String section, String key, Object value) throws IOException {
		ini.put(section, key, value);
		ini.store();
	}

	public void updateSelectedPluginsList(List<String> selectedPlugins) throws IOException {
		String slLoadedPlugins = "";
		for (String pluginId : selectedPlugins) {
			slLoadedPlugins += pluginId + ";";
		}

		put(PLUGIN_SECTION, PPL_SELECTED_PLUGINS, slLoadedPlugins);
	}

	public List<String> getSelectedPluginsList() {
		List<String> selectedPluginsList = new ArrayList<>();
		String selectedPlugins = ini.get(PLUGIN_SECTION, PPL_SELECTED_PLUGINS);

		if (selectedPlugins == null || selectedPlugins.isEmpty()) {
			return new ArrayList<String>();
		}

		String[] splitPlugins = selectedPlugins.split(";");

		for (String str : splitPlugins) {
			selectedPluginsList.add(str);
		}

		return selectedPluginsList;
	}

}
