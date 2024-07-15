package io.azraein.paperfx.system.io;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.github.luben.zstd.Zstd;

import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.classes.CharacterClass;
import io.azraein.paperfx.system.actors.classes.CharacterRace;
import io.azraein.paperfx.system.exceptions.DatabaseCorruptionException;
import io.azraein.paperfx.system.exceptions.IncompatibleDatabaseVersionException;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.locations.Location;
import io.azraein.paperfx.system.locations.SubLocation;

public class Database implements Serializable {

	private static final long serialVersionUID = -5878465281727704975L;

	public static transient final String DATABASE_NAME = "PaperFX.pdb";
	public static transient final String DATABASE_SAVE_VERSION = "1.0";
	public static transient final String DATABASE_SAVE_IDENTIFIER = "PFXDB";

	private Map<String, Object> globalList = new HashMap<>();

	private Map<String, Item> itemList = new HashMap<>();

	private Map<String, CharacterRace> raceList = new HashMap<>();
	private Map<String, CharacterClass> charClassList = new HashMap<>();

	private Map<String, Actor> actorList = new HashMap<>();

	private Map<String, Location> locationList = new HashMap<>();
	private Map<String, SubLocation> subLocationList = new HashMap<>();

	public Item getItem(String itemId) {
		return itemList.get(itemId);
	}
	
	public void addItem(Item item) {
		itemList.put(item.getItemId(), item);
	}
	
	public Object getGlobal(String globalId) {
		return globalList.get(globalId);
	}

	public void addGlobal(String globalId, Object global) {
		globalList.put(globalId, global);
	}

	public CharacterRace getCharacterRace(String characterRaceId) {
		return raceList.get(characterRaceId);
	}

	public void addCharacterRace(CharacterRace characterRace) {
		raceList.put(characterRace.getCharacterRaceId(), characterRace);
	}

	public CharacterClass getCharacterClass(String charClassId) {
		return charClassList.get(charClassId);
	}

	public void addCharacterClass(CharacterClass characterClass) {
		charClassList.put(characterClass.getCharacterClassId(), characterClass);
	}

	public Location getLocation(String locationId) {
		return locationList.get(locationId);
	}

	public void addLocation(Location location) {
		locationList.put(location.getLocationId(), location);
	}

	public SubLocation getSubLocation(String subLocationId) {
		return subLocationList.get(subLocationId);
	}

	public void addSubLocation(SubLocation subLocation) {
		subLocationList.put(subLocation.getSubLocationId(), subLocation);
	}

	public Actor getActor(String actorId) {
		return actorList.get(actorId);
	}

	public void addActor(Actor actor) {
		actorList.put(actor.getActorId(), actor);
	}

	public static void saveDatabase(Database database) {
		String databaseJson = SaveSystem.SAVE_GSON.toJson(database);
		byte[] uncompressedDatabase = databaseJson.getBytes();
		int uncompressedDatabaseSize = uncompressedDatabase.length;
		byte[] compressedDatabase = Zstd.compress(uncompressedDatabase);

		try (DataOutputStream databaseOutputStream = new DataOutputStream(new FileOutputStream(DATABASE_NAME))) {
			// Write Header
			databaseOutputStream.writeUTF(DATABASE_SAVE_IDENTIFIER);
			databaseOutputStream.writeUTF(DATABASE_SAVE_VERSION);
			databaseOutputStream.writeInt(uncompressedDatabaseSize);

			// Write Database
			databaseOutputStream.write(compressedDatabase);
			databaseOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Database loadDatabase() throws IncompatibleDatabaseVersionException, DatabaseCorruptionException {
		File file = new File(DATABASE_NAME);
		Database database = null;

		try (DataInputStream databaseInputStream = new DataInputStream(new FileInputStream(file))) {
			databaseInputStream.readUTF();
			String databaseVersion = databaseInputStream.readUTF();
			if (!databaseVersion.equals(DATABASE_SAVE_VERSION))
				throw new IncompatibleDatabaseVersionException(
						"This database is incompatible with this verison of Paper");

			int databaseUncompressedSize = databaseInputStream.readInt();
			byte[] compressedDatabaseData = databaseInputStream.readAllBytes();
			byte[] uncompressedDatabaseData = Zstd.decompress(compressedDatabaseData, databaseUncompressedSize);
			String databaseJson = new String(uncompressedDatabaseData);

			database = SaveSystem.SAVE_GSON.fromJson(databaseJson, Database.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Creates a blank empty database
		if (database == null)
			throw new DatabaseCorruptionException("The PaperFX Database seems to be corrupted");

		return database;
	}

}
