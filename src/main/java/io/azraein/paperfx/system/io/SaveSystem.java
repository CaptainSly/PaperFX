package io.azraein.paperfx.system.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.ini4j.InvalidFileFormatException;
import org.tinylog.Logger;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;

import io.azraein.paperfx.PaperFX;
import io.azraein.paperfx.system.World;
import io.azraein.paperfx.system.actors.ActorState;
import io.azraein.paperfx.system.actors.creatures.CreatureState;
import io.azraein.paperfx.system.exceptions.IncompatiblePluginVersionException;
import io.azraein.paperfx.system.exceptions.IncompatibleSaveVersionException;
import io.azraein.paperfx.system.exceptions.PluginCorruptionException;
import io.azraein.paperfx.system.exceptions.SaveCorruptionException;
import io.azraein.paperfx.system.io.plugins.PaperPlugin;
import io.azraein.paperfx.system.io.plugins.PaperPluginMetadata;
import io.azraein.paperfx.system.locations.LocationState;

public class SaveSystem {

	public static Gson SAVE_GSON = new Gson();

	public static final String PLAYER_SAVE_VERSION = "1.0";
	public static final String PLAYER_SAVE_IDENTIFIER = "PFXSF";
	public static final String PLAYER_SAVE_FILE_EXTENSION = ".pesf";

	public static final String PAPER_PLUGIN_FILE_IDENTIFIER = "PFXPF";
	public static final String PAPER_PLUGIN_FILE_VERSION = "1.0";
	public static final String PAPER_PLUGIN_MAIN_FILE_EXTENSION = ".pepm";
	public static final String PAPER_PLUGIN_ADDON_FILE_EXTENSION = ".pepf";

	public static final String PAPER_DATA_FOLDER = "paper/data/";

	/**
	 * Creates a "Paper Save File" or a ".pesf"
	 * </p>
	 * It's a binary file that consist of a header and compressed json data of the
	 * passed in World.
	 * 
	 * @param world
	 */
	public static void createPlayerSaveFile(World world, String saveName) {
		String worldJson = SAVE_GSON.toJson(world);
		byte[] worldBytes = worldJson.getBytes();
		int worldSize = worldBytes.length;

		byte[] compressedWorld = Zstd.compress(worldBytes);

		Logger.debug("This is the compressed world as a string: " + new String(compressedWorld));

		try (DataOutputStream saveOutputStream = new DataOutputStream(new FileOutputStream(saveName + ".pesf"))) {
			// Write Header
			saveOutputStream.writeUTF(PLAYER_SAVE_IDENTIFIER);
			saveOutputStream.writeUTF(PLAYER_SAVE_VERSION);
			saveOutputStream.writeInt(worldSize);

			// Write the compressed world to file
			saveOutputStream.write(compressedWorld);
			saveOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static World loadPlayerSaveFile(String saveName)
			throws SaveCorruptionException, IncompatibleSaveVersionException {
		File saveFile = new File(saveName + ".pesf");
		World world = null;

		try (DataInputStream saveInputStream = new DataInputStream(new FileInputStream(saveFile))) {
			String saveId = saveInputStream.readUTF(); // Save Identifier

			if (!saveId.equals(PLAYER_SAVE_IDENTIFIER))
				throw new InvalidFileFormatException(
						"Though the file has a *.pesf extension, it is missing a vital header piece, try using an actual Paper Save File?");

			String saveVersion = saveInputStream.readUTF();

			if (!saveVersion.equals(PLAYER_SAVE_VERSION))
				throw new IncompatibleSaveVersionException("The current save file is outdated");

			int worldSizeUncompressed = saveInputStream.readInt();
			byte[] worldCompressedBytes = saveInputStream.readAllBytes();

			byte[] worldUncompressedBytes = Zstd.decompress(worldCompressedBytes, worldSizeUncompressed);
			String worldJson = new String(worldUncompressedBytes);

			Logger.debug(worldJson);

			world = SAVE_GSON.fromJson(worldJson, World.class);
			saveInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (world == null) {
			throw new SaveCorruptionException(
					"The Save File at: " + saveName + " can't be loaded, maybe it's corrupted?");
		}

		// Load things to Database lists.
		for (Entry<String, LocationState> eSet : world.getLocationStates().entrySet()) {
			PaperFX.getDatabase().getLocation(eSet.getKey()).setLocationState(eSet.getValue());
		}

		for (Entry<String, ActorState> eSet : world.getActorStates().entrySet()) {
			PaperFX.getDatabase().getActor(eSet.getKey()).setActorState(eSet.getValue());
		}

		for (Entry<String, CreatureState> eSet : world.getCreatureStates().entrySet()) {
			PaperFX.getDatabase().getCreature(eSet.getKey()).setCreatureState(eSet.getValue());
		}

		return world;
	}

	public static void savePlugin(PaperPlugin plugin, String path) {
		try (DataOutputStream pluginOutputStream = new DataOutputStream(new FileOutputStream(path))) {
			// Write Plugin Identifier
			pluginOutputStream.writeUTF(PAPER_PLUGIN_FILE_IDENTIFIER);

			// Write Plugin File Version
			pluginOutputStream.writeUTF(PAPER_PLUGIN_FILE_VERSION);

			// Get Database stuff ready for writing
			String databaseJson = SAVE_GSON.toJson(plugin.getPluginDatabase());
			byte[] uncompressedDatabaseBytes = databaseJson.getBytes();
			int uncompressedDatabaseSize = uncompressedDatabaseBytes.length;
			pluginOutputStream.writeInt(uncompressedDatabaseSize);

			// Write Plugin Metadata
			pluginOutputStream.writeUTF(plugin.getPluginId());
			pluginOutputStream.writeUTF(plugin.getPluginName());
			pluginOutputStream.writeUTF(plugin.getPluginAuthor());
			pluginOutputStream.writeUTF(plugin.getPluginDescription());

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
		try (ObjectInputStream pluginInputStream = new ObjectInputStream(new FileInputStream(pluginPath))) {

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
			String pluginDescription = pluginInputStream.readUTF();

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

			// Read Plugin Id
			String pluginId = metadataInputStream.readUTF();
			// Read Plugin Name
			String pluginName = metadataInputStream.readUTF();
			// Read Plugin Author
			String pluginAuthor = metadataInputStream.readUTF();
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

			return new PaperPluginMetadata(pluginId, pluginName, pluginAuthor, pluginDescription, pluginDependencies,
					pluginPath, isPluginMainFile);
		}
	}

}
