package io.azraein.paperfx.system.io;

import java.io.*;

import org.tinylog.Logger;

import com.github.luben.zstd.Zstd;
import com.google.gson.Gson;

import io.azraein.paperfx.system.World;
import io.azraein.paperfx.system.exceptions.IncompatibleSaveVersionException;
import io.azraein.paperfx.system.exceptions.SaveCorruptionException;

public class SaveSystem {

	public static Gson SAVE_GSON = new Gson();

	public static final String SAVE_VERSION = "1.0";
	public static final String SAVE_IDENTIFIER = "PESF";

	/**
	 * Creates a "Paper Save File" or a ".pesf"
	 * </p>
	 * It's a binary file that consist of a header and compressed json data of the
	 * passed in World.
	 * 
	 * @param world
	 */
	public static void createSaveFile(World world, String saveName) {
		String worldJson = SAVE_GSON.toJson(world);
		byte[] worldBytes = worldJson.getBytes();
		int worldSize = worldBytes.length;

		byte[] compressedWorld = Zstd.compress(worldBytes);

		Logger.debug("This is the compressed world as a string: " + new String(compressedWorld));

		try (DataOutputStream saveOutputStream = new DataOutputStream(new FileOutputStream(saveName + ".pesf"))) {
			// Write Header
			saveOutputStream.writeUTF(SAVE_IDENTIFIER);
			saveOutputStream.writeUTF(SAVE_VERSION);
			saveOutputStream.writeInt(worldSize);

			// Write the compressed world to file
			saveOutputStream.write(compressedWorld);
			saveOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static World loadSaveFile(String saveName) throws SaveCorruptionException, IncompatibleSaveVersionException {
		File saveFile = new File(saveName + ".pesf");
		World world = null;

		try (DataInputStream saveInputStream = new DataInputStream(new FileInputStream(saveFile))) {
			saveInputStream.readUTF(); // Save Identifier
			String saveVersion = saveInputStream.readUTF();

			if (!saveVersion.equals(SAVE_VERSION))
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

		return world;
	}

}
