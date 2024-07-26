package io.azraein.paperfx.system.io;

import java.io.*;

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

			String metadataJson = SAVE_GSON.toJson(plugin.getMetadata());
			String databaseJson = SAVE_GSON.toJson(plugin.getPluginDatabase());

			byte[] uncompressedMetadataBytes = metadataJson.getBytes();
			byte[] uncompressedDatabaseBytes = databaseJson.getBytes();

			int uncompressedMetadataSize = uncompressedMetadataBytes.length;
			int uncompressedDatabaseSize = uncompressedDatabaseBytes.length;

			pluginOutputStream.writeInt(uncompressedMetadataSize);
			pluginOutputStream.writeInt(uncompressedDatabaseSize);

			// Finally Compress and Write the database bytes
			byte[] compressedMetadataBytes = Zstd.compress(uncompressedMetadataBytes);
			byte[] compressedDatabaseBytes = Zstd.compress(uncompressedDatabaseBytes);

			int compressedMetadataBytesSize = compressedMetadataBytes.length;
			int compressedDatabaseBytesSize = compressedDatabaseBytes.length;

			pluginOutputStream.writeInt(compressedMetadataBytesSize);
			pluginOutputStream.writeInt(compressedDatabaseBytesSize);
			pluginOutputStream.write(compressedMetadataBytes);
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
		Logger.debug("Plugin Path: " + pluginPath);
		try (DataInputStream pluginInputStream = new DataInputStream(new FileInputStream(pluginPath))) {

			String pluginFileIdentifier = pluginInputStream.readUTF();
			if (!pluginFileIdentifier.equals(PAPER_PLUGIN_FILE_IDENTIFIER))
				throw new PluginCorruptionException(
						"The File Identifier does not match, the file could be corrupted or malformed");

			String pluginFileVersion = pluginInputStream.readUTF();
			if (!pluginFileVersion.equals(PAPER_PLUGIN_FILE_VERSION))
				throw new IncompatiblePluginVersionException(
						"The loaded plugins version is incompatible with this version of Paper");

			int uncompressedMetadataBytesSize = pluginInputStream.readInt();
			int uncompressedDatabaseBytesSize = pluginInputStream.readInt();

			int compressedMetadataBytesSize = pluginInputStream.readInt();
			int compressedDatabaseBytesSize = pluginInputStream.readInt();

			byte[] compressedMetadataBytes = pluginInputStream.readNBytes(compressedMetadataBytesSize);
			byte[] compressedDatabaseBytes = pluginInputStream.readNBytes(compressedDatabaseBytesSize);

			byte[] uncompressedMetadataBytes = Zstd.decompress(compressedMetadataBytes, uncompressedMetadataBytesSize);
			byte[] uncompressedDatabaseBytes = Zstd.decompress(compressedDatabaseBytes, uncompressedDatabaseBytesSize);

			PaperPluginMetadata metadata = SAVE_GSON.fromJson(new String(uncompressedMetadataBytes),
					PaperPluginMetadata.class);
			Database database = SAVE_GSON.fromJson(new String(uncompressedDatabaseBytes), Database.class);

			ppf = new PaperPlugin(metadata, database);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ppf;
	}

	@SuppressWarnings("unused")
	public static PaperPluginMetadata readPluginMetadata(String pluginPath) throws IOException {
		try (DataInputStream metadataInputStream = new DataInputStream(new FileInputStream(pluginPath))) {
			metadataInputStream.readUTF();
			metadataInputStream.readUTF();
			int uncompressedMetadataBytesSize = metadataInputStream.readInt();
			int uncompressedDatabaseBytesSize = metadataInputStream.readInt();
			int compressedMetadataBytesSize = metadataInputStream.readInt();
			int compressedDatabaseBytesSize = metadataInputStream.readInt();
			byte[] compressedMetadataBytes = metadataInputStream.readNBytes(compressedMetadataBytesSize);
			byte[] compressedDatabaseBytes = metadataInputStream.readNBytes(compressedDatabaseBytesSize);
			byte[] uncompressedMetadataBytes = Zstd.decompress(compressedMetadataBytes, uncompressedMetadataBytesSize);
			PaperPluginMetadata metadata = SAVE_GSON.fromJson(new String(uncompressedMetadataBytes),
					PaperPluginMetadata.class);

			return metadata;
		}
	}

}
