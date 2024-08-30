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

	public static final String SYSTEM_DEFAULT_MUSIC_VOLUME = "dDefaultMusicVolume";
	public static final String SYSTEM_DEFAULT_AMBIENCE_VOLUME = "dDefaultAmbienceVolume";
	public static final String SYSTEM_DEFAULT_SFX_VOLUME = "dDefaultSFXVolume";

	// Plugin Loader
	public static final String PPL_SELECTED_PLUGINS = "slLoadedPlugins";

	public PaperIni() {
		try {
			File iniFile = new File(INI_NAME);
			if (iniFile.exists()) {
				ini = new Ini(iniFile);
			} else {
				ini = new Ini();
				ini.setComment(
				"PaperFX INI - Contains Options for both the Game Engine and the Game Editor\n" +
				"#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n"	+
				"#|				~KEY~			   |\n" +
				"#| i - Integer					   |\n" +
				"#| b - Boolean					   |\n" + 
				"#| s - String 					   |\n" + 
				"#| f - Float 					   |\n" + 
				"#| d - Double					   |\n" +
				"#| sl - String List 			   |\n" +
				"#-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-\n"	
				);
				ini.setFile(iniFile);

				put(SYSTEM_SECTION, SYSTEM_AUTO_SAVE_DURATION, Options.autoSaveDuration);
				put(SYSTEM_SECTION, SYSTEM_MAX_AUTO_SAVES, Options.maxAutoSaves);
				put(SYSTEM_SECTION, SYSTEM_DO_AUTO_SAVE, Options.doAutoSave);

				put(SYSTEM_SECTION, SYSTEM_DEFAULT_MUSIC_VOLUME, Options.defaultMusicVolume);
				put(SYSTEM_SECTION, SYSTEM_DEFAULT_AMBIENCE_VOLUME, Options.defaultAmbienceVolume);
				put(SYSTEM_SECTION, SYSTEM_DEFAULT_SFX_VOLUME, Options.defaultSFXVolume);

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

	public String getString(String section, String key) {
		return ini.get(section, key, String.class);
	}

	public boolean getBoolean(String section, String key) {
		return ini.get(section, key, Boolean.class);
	}

	public int getInt(String section, String key) {
		return ini.get(section, key, Integer.class);
	}

	public Double getDouble(String section, String key) {
		return ini.get(section, key, Double.class);
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
