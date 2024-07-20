package io.azraein.paperfx.system.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.tinylog.Logger;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;

import io.azraein.paperfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.paperfx.system.exceptions.PluginCorruptionException;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;

public class SaveSystem {

	public static Gson SAVE_GSON = new Gson();

	public static final String PAPER_PLUGIN_FILE_IDENTIFIER = "PEPF";
	public static final String PAPER_PLUGIN_FILE_VERSION = "1.0";
	public static final String PAPER_PLUGIN_MAIN_FILE_EXTENSION = ".pepm";
	public static final String PAPER_PLUGIN_ADDON_FILE_EXTENSION = ".pepf";

	public static final String PAPER_DATA_FOLDER = "paper/data/";

	public static void savePlugin(PaperPlugin plugin, String path) {
		try (DataOutputStream pluginOutputStream = new DataOutputStream(new FileOutputStream(path))) {
			// Write Plugin Identifier
			pluginOutputStream.writeUTF(PAPER_PLUGIN_FILE_IDENTIFIER);

			// Write Plugin File Version
			pluginOutputStream.writeUTF(PAPER_PLUGIN_FILE_VERSION);

			// Get Database stuff ready for writing
			String databaseJson = SAVE_GSON.toJson(plugin.getPluginDatabase());
			Logger.debug(databaseJson);
			byte[] uncompressedDatabaseBytes = databaseJson.getBytes();
			int uncompressedDatabaseSize = uncompressedDatabaseBytes.length;
			pluginOutputStream.writeInt(uncompressedDatabaseSize);

			// Write Plugin Metadata
			pluginOutputStream.writeUTF(plugin.getPluginId());
			pluginOutputStream.writeUTF(plugin.getPluginName());
			pluginOutputStream.writeUTF(plugin.getPluginAuthor());
			pluginOutputStream.writeUTF(plugin.getPluginVersion());
			pluginOutputStream.writeUTF(plugin.getPluginDescription());
			pluginOutputStream.writeBoolean(plugin.isMainFile());

			// Write Plugin Dependency Ids
			pluginOutputStream.writeInt(plugin.getPluginDependencies().size());
			for (String pluginId : plugin.getPluginDependencies())
				pluginOutputStream.writeUTF(pluginId);

			// Finally Compress and Write the database bytes
			byte[] compressedDatabaseBytes = Zstd.compress(uncompressedDatabaseBytes);
			pluginOutputStream.write(compressedDatabaseBytes);
			pluginOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PaperPlugin loadPlugin(String pluginPath)
			throws IncompatiblePluginVersionException, PluginCorruptionException {
		PaperPlugin ppf = null;
		Logger.debug("Reading Plugin from path: " + pluginPath);
		try (DataInputStream pluginInputStream = new DataInputStream(new FileInputStream(pluginPath))) {

			// Read Plugin File Identifier
			String pluginFileId = pluginInputStream.readUTF();
			if (!pluginFileId.equals(PAPER_PLUGIN_FILE_IDENTIFIER))
				throw new PluginCorruptionException(
						"The Paper Plugin File Identifier doesn't match, the file might be corrupted");

			String pluginFileVersion = pluginInputStream.readUTF();
			if (!pluginFileVersion.equals(PAPER_PLUGIN_FILE_VERSION))
				throw new IncompatiblePluginVersionException(
						"The plugin file version doesn't match, incompatible with this version of Paper");

			int uncompressedDatabaseSize = pluginInputStream.readInt();

			String pluginId = pluginInputStream.readUTF();
			String pluginName = pluginInputStream.readUTF();
			String pluginAuthor = pluginInputStream.readUTF();
			String pluginVersion = pluginInputStream.readUTF();
			String pluginDescription = pluginInputStream.readUTF();
			Boolean pluginIsMainFile = pluginInputStream.readBoolean();

			int dependencyCount = pluginInputStream.readInt();

			List<String> pluginDependencies = new ArrayList<>();
			for (int i = 0; i < dependencyCount; i++)
				pluginDependencies.add(pluginInputStream.readUTF());

			byte[] compressedDatabase = pluginInputStream.readAllBytes();
			Database database = SAVE_GSON.fromJson(
					new String(Zstd.decompress(compressedDatabase, uncompressedDatabaseSize)), Database.class);

			ppf = new PaperPlugin(pluginId, pluginName);
			ppf.setPluginAuthor(pluginAuthor);
			ppf.setPluginDependencies(pluginDependencies);
			ppf.setPluginDescription(pluginDescription);
			ppf.setPluginVersion(pluginVersion);
			ppf.setIsMainFile(pluginIsMainFile);
			ppf.setPluginDatabase(database);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ppf;
	}

	public static PaperPluginMetadata readPluginMetadata(String pluginPath) throws IOException {
		try (DataInputStream metadataInputStream = new DataInputStream(new FileInputStream(pluginPath))) {
			// Ignore the File METADATA
			metadataInputStream.readUTF();
			metadataInputStream.readUTF();
			metadataInputStream.readInt();

			// We're looking for the Plugin METADATA
			// Read Plugin Id
			String pluginId = metadataInputStream.readUTF();
			// Read Plugin Name
			String pluginName = metadataInputStream.readUTF();
			// Read Plugin Author
			String pluginAuthor = metadataInputStream.readUTF();
			// Read Plugin Version
			String pluginVersion = metadataInputStream.readUTF();
			// Read Plugin Description
			String pluginDescription = metadataInputStream.readUTF();
			// Read Plugin isMainFile
			boolean isPluginMainFile = metadataInputStream.readBoolean();
			// Read Plugin Dependencies Count
			int dependencyCount = metadataInputStream.readInt();
			// Read Plugin Dependencies
			List<String> pluginDependencies = new ArrayList<>();
			for (int i = 0; i < dependencyCount; i++) {
				pluginDependencies.add(metadataInputStream.readUTF());
			}

			return new PaperPluginMetadata(pluginId, pluginName, pluginAuthor, pluginDescription, pluginVersion,
					pluginDependencies, pluginPath, isPluginMainFile);
		}
	}

}
