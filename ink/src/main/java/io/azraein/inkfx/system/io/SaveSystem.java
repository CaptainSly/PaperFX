package io.azraein.inkfx.system.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.tinylog.Logger;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.azraein.inkfx.system.Options;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.actors.Actor;
import io.azraein.inkfx.system.actors.ActorState;
import io.azraein.inkfx.system.exceptions.system.IncompatiblePluginVersionException;
import io.azraein.inkfx.system.exceptions.system.IncompatibleSaveVersionException;
import io.azraein.inkfx.system.exceptions.system.PluginCorruptionException;
import io.azraein.inkfx.system.exceptions.system.SaveCorruptionException;
import io.azraein.inkfx.system.io.plugins.PaperPlugin;
import io.azraein.inkfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.inkfx.system.io.type_adapters.ActorTypeAdapter;
import io.azraein.inkfx.system.locations.LocationState;
import io.azraein.inkfx.system.locations.buildings.BuildingState;
import io.azraein.inkfx.system.world.World;

public class SaveSystem {

	public static Gson SAVE_GSON;

	// Paper File Format Information
	public static final String PAPER_PLUGIN_FILE_IDENTIFIER = "PEPF";
	public static final String PAPER_PLUGIN_FILE_VERSION = "1.0";
	public static final String PAPER_PLUGIN_MAIN_FILE_EXTENSION = ".pepm";
	public static final String PAPER_PLUGIN_ADDON_FILE_EXTENSION = ".pepf";

	public static final String PAPER_SAVE_FILE_IDENTIFIER = "PESF";
	public static final String PAPER_SAVE_FILE_VERSION = "1.0";
	public static final String PAPER_SAVE_FILE_EXTENSION = ".pesf";

	// Paper Folders
	public static final String PAPER_FOLDER = "paper_data/";
	public static final String PAPER_DATA_FOLDER = PAPER_FOLDER + "data/";
	public static final String PAPER_SCRIPT_FOLDER = PAPER_DATA_FOLDER + "scripts/";
	public static final String PAPER_GFX_FOLDER = PAPER_DATA_FOLDER + "gfx/";
	public static final String PAPER_AUDIO_FOLDER = PAPER_DATA_FOLDER + "audio/";
	public static final String PAPER_SAVE_FOLDER = PAPER_FOLDER + "saves/";

	public static final String[] PAPER_DATA_FOLDERS = {
			// Data Folders
			"data/", "data/audio/", "data/audio/sfx/", "data/audio/music/", "data/gfx/", "data/scripts/",
			// Internal Paper Folders
			"saves/", "logs",
			// To be continued
	};

	public static final String PAPER_AUTOSAVE_NAME = "paper_autosave_";

	private static int autoSaveCounter = 0;

	static {
		SAVE_GSON = new GsonBuilder().registerTypeAdapter(Actor.class, new ActorTypeAdapter(new Gson())).create();
	}

	public static void checkFileSystem() {
		Logger.debug("Checking to see if Paper's Working Directory exists, if not we'll create it for you.");

		File file = new File(PAPER_FOLDER);
		if (!file.exists()) {
			file.mkdirs();

			for (String str : PAPER_DATA_FOLDERS) {
				new File(PAPER_FOLDER + str).mkdir();
			}

		}
	}

	public static void autoSave() {
		if (autoSaveCounter > Options.maxAutoSaves)
			autoSaveCounter = 0;

		String savePath = PAPER_SAVE_FOLDER + PAPER_AUTOSAVE_NAME + autoSaveCounter + PAPER_SAVE_FILE_EXTENSION;
		savePlayerFile(Paper.PAPER_WORLD_PROPERTY.get(), savePath);
		autoSaveCounter++;
	}

	public static void savePlayerFile(World world, String path) {
		try (DataOutputStream worldOutputStream = new DataOutputStream(new FileOutputStream(path))) {

			// Write Save Identifier
			worldOutputStream.writeUTF(PAPER_SAVE_FILE_IDENTIFIER);

			// Write Save File Version
			worldOutputStream.writeUTF(PAPER_SAVE_FILE_VERSION);

			// Write the World to a json string then convert it to bytes.
			String worldJson = SAVE_GSON.toJson(world);
			byte[] uncompressedWorldBytes = worldJson.getBytes();

			// Get the size of the uncompressed bytes.
			int uncompressedWorldSize = uncompressedWorldBytes.length;
			worldOutputStream.writeInt(uncompressedWorldSize);

			// Compress and Write the World to the file.
			byte[] compressedWorldBytes = Zstd.compress(uncompressedWorldBytes);
			worldOutputStream.write(compressedWorldBytes);

			worldOutputStream.flush();
			worldOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static World loadPlayerFile(String path) throws SaveCorruptionException, IncompatibleSaveVersionException {
		World world = null;
		try (DataInputStream worldInputStream = new DataInputStream(new FileInputStream(path))) {

			String worldFileIdentifier = worldInputStream.readUTF();
			if (!worldFileIdentifier.equals(PAPER_SAVE_FILE_IDENTIFIER))
				throw new SaveCorruptionException(
						"The File Identifier does not match, the file could be corrupted or malformed");

			String pluginFileVersion = worldInputStream.readUTF();
			if (!pluginFileVersion.equals(PAPER_PLUGIN_FILE_VERSION))
				throw new IncompatibleSaveVersionException(
						"The loaded plugins version is incompatible with this version of Paper");

			int uncompressedWorldSize = worldInputStream.readInt();
			byte[] uncompressedWorld = worldInputStream.readAllBytes();

			byte[] worldBytes = Zstd.decompress(uncompressedWorld, uncompressedWorldSize);
			String worldJson = new String(worldBytes);

			world = SAVE_GSON.fromJson(worldJson, World.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Load All the Saved states to the objects it needs to.
		for (Entry<String, LocationState> worldLS : world.getWorldLocationStates().entrySet())
			Paper.DATABASE.getLocation(worldLS.getKey()).setLocationState(worldLS.getValue());

		for (Entry<String, BuildingState> worldBS : world.getWorldBuildingStates().entrySet())
			Paper.DATABASE.getBuilding(worldBS.getKey()).setBuildingState(worldBS.getValue());

		for (Entry<String, ActorState> worldAS : world.getWorldActorStates().entrySet())
			Paper.DATABASE.getNpc(worldAS.getKey()).setActorState(worldAS.getValue());

		// Load the Data from the Save into the objects
		Paper.CALENDAR = world.getCalendar();
		Paper.PAPER_WORLD_PROPERTY.set(world);
		Paper.PAPER_PLAYER_PROPERTY.set(world.getPlayer());
		Paper.PAPER_LOCATION_PROPERTY.set(Paper.DATABASE.getLocation((world.getCurrentLocationId())));
		Paper.PAPER_GAME_GLOBALS.putAll(world.getWorldGlobalsMap());

		return world;
	}

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

			pluginOutputStream.flush();
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

	public static PaperPluginMetadata readPluginMetadata(String pluginPath) throws IOException {
		try (DataInputStream metadataInputStream = new DataInputStream(new FileInputStream(pluginPath))) {
			metadataInputStream.readUTF();
			metadataInputStream.readUTF();
			int uncompressedMetadataBytesSize = metadataInputStream.readInt();
			metadataInputStream.readInt();
			int compressedMetadataBytesSize = metadataInputStream.readInt();
			int compressedDatabaseBytesSize = metadataInputStream.readInt();
			byte[] compressedMetadataBytes = metadataInputStream.readNBytes(compressedMetadataBytesSize);
			metadataInputStream.readNBytes(compressedDatabaseBytesSize);
			byte[] uncompressedMetadataBytes = Zstd.decompress(compressedMetadataBytes, uncompressedMetadataBytesSize);
			PaperPluginMetadata metadata = SAVE_GSON.fromJson(new String(uncompressedMetadataBytes),
					PaperPluginMetadata.class);

			return metadata;
		}
	}

}
