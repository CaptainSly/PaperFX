package io.azraein.paperfx.system.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ini4j.Ini;
import org.tinylog.Logger;

public class PaperIni {

	private Ini ini;

	public static final String INI_NAME = "paperfx.ini";
	
	// Sections
	public static final String SYSTEM_SECTION = "System";
	
	// Keys
	// System
	public static final String SYSTEM_SELECTED_PLUGINS = "slLoadedPlugins";
	

	public PaperIni() {
		try {
			File iniFile = new File(INI_NAME);
			if (iniFile.exists()) {
				ini = new Ini(iniFile);
			} else {
				ini = new Ini();
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

	public void updateSelectedPluginsList(List<String> selectedPlugins) {
		String slLoadedPlugins = "";
		for (String pluginId : selectedPlugins) {
			slLoadedPlugins += pluginId + ";";
		}
	}
	
}
