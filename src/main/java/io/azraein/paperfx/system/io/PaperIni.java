package io.azraein.paperfx.system.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.Ini;
import org.tinylog.Logger;

public class PaperIni {

	private Ini ini;

	public static final String INI_NAME = SaveSystem.PAPER_FOLDER + "paperfx.ini";

	// Sections
	public static final String SYSTEM_SECTION = "System";

	public static final String EDITOR_SECTION = "Editor";

	public static final String GAME_SECTION = "Game";

	// Keys

	// System
	public static final String SYSTEM_SELECTED_PLUGINS = "slLoadedPlugins";

	// Editor

	// Game

	public PaperIni() {
		try {
			File iniFile = new File(INI_NAME);
			if (iniFile.exists()) {
				ini = new Ini(iniFile);
			} else {
				ini = new Ini();
				ini.setComment("PaperFX INI - Contains Options for both the Game Engine and the Game Editor");
				ini.setFile(iniFile);
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

		put(SYSTEM_SECTION, SYSTEM_SELECTED_PLUGINS, slLoadedPlugins);
	}

	public List<String> getSelectedPluginsList() {
		List<String> selectedPluginsList = new ArrayList<>();
		String selectedPlugins = ini.get(SYSTEM_SECTION, SYSTEM_SELECTED_PLUGINS);

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
