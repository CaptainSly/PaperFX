package io.azraein.paperfx.system.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.actors.creatures.Creature;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.locations.Location;
import io.azraein.paperfx.system.locations.SubLocation;

public class Database implements Serializable {

	private static final long serialVersionUID = -5878465281727704975L;

	private Map<String, Object> globalList = new HashMap<>();
	private Map<String, Item> itemList = new HashMap<>();
	private Map<String, ActorRace> raceList = new HashMap<>();
	private Map<String, ActorClass> charClassList = new HashMap<>();
	private Map<String, Actor> actorList = new HashMap<>();
	private Map<String, Creature> creatureList = new HashMap<>();
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

	public ActorRace getCharacterRace(String characterRaceId) {
		return raceList.get(characterRaceId);
	}

	public void addCharacterRace(ActorRace characterRace) {
		raceList.put(characterRace.getActorRaceId(), characterRace);
	}

	public Creature getCreature(String creatureId) {
		return creatureList.get(creatureId);
	}

	public void addCreature(Creature creature) {
		creatureList.put(creature.getCreatureId(), creature);
	}

	public ActorClass getCharacterClass(String charClassId) {
		return charClassList.get(charClassId);
	}

	public void addCharacterClass(ActorClass characterClass) {
		charClassList.put(characterClass.getActorClassId(), characterClass);
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
	
	public void mergeDatabase(Database database) {
		this.getGlobalList().putAll(database.getGlobalList());
		this.getItemList().putAll(database.getItemList());
		this.getCharClassList().putAll(database.getCharClassList());
		this.getRaceList().putAll(database.getRaceList());
		this.getActorList().putAll(database.getActorList());
		this.getCreatureList().putAll(database.getCreatureList());
		this.getLocationList().putAll(database.getLocationList());
		this.getSubLocationList().putAll(database.getSubLocationList());
	}

	public Map<String, Location> getLocationList() {
		return locationList;
	}

	public Map<String, Creature> getCreatureList() {
		return creatureList;
	}

	public Map<String, Object> getGlobalList() {
		return globalList;
	}

	public Map<String, Item> getItemList() {
		return itemList;
	}

	public Map<String, ActorRace> getRaceList() {
		return raceList;
	}

	public Map<String, ActorClass> getCharClassList() {
		return charClassList;
	}

	public Map<String, Actor> getActorList() {
		return actorList;
	}

	public Map<String, SubLocation> getSubLocationList() {
		return subLocationList;
	}

}
