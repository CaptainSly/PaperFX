package io.azraein.paperfx.system.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import io.azraein.paperfx.system.inventory.items.Item;
import io.azraein.paperfx.system.locations.Location;

// TODO: Fix up this global mess. It's understandable that World has one so it can save the state of the global, but why do we have 3 now? 

public class Database implements Serializable {

	private static final long serialVersionUID = -5878465281727704975L;

	private Map<String, Object> globalList = new HashMap<>();
	private Map<String, Item> itemList = new HashMap<>();
	private Map<String, ActorRace> raceList = new HashMap<>();
	private Map<String, ActorClass> actorClassList = new HashMap<>();
	private Map<String, Actor> actorList = new HashMap<>();
	private Map<String, Location> locationList = new HashMap<>();

	public Item getItem(String itemId) {
		return itemList.get(itemId);
	}

	public void addItem(Item item) {
		itemList.put(item.getItemId(), item);
	}

	public Object getGlobal(String globalId) {
		return globalList.get(globalId);
	}

	public void setGlobal(String globalId, Object global) {
		globalList.replace(globalId, global);
		Paper.PAPER_GAME_GLOBALS.replace(globalId, global);
		Paper.PAPER_WORLD_PROPERTY.get().getWorldGlobalsMap().replace(globalId, global);
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

	public ActorClass getActorClass(String charClassId) {
		return actorClassList.get(charClassId);
	}

	public void addActorClass(ActorClass characterClass) {
		actorClassList.put(characterClass.getActorClassId(), characterClass);
	}

	public Location getLocation(String locationId) {
		return locationList.get(locationId);
	}

	public void addLocation(Location location) {
		locationList.put(location.getLocationId(), location);
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
		this.getActorClassList().putAll(database.getActorClassList());
		this.getRaceList().putAll(database.getRaceList());
		this.getActorList().putAll(database.getActorList());
		this.getLocationList().putAll(database.getLocationList());
	}

	public void mergeDatabase(Database... databases) {
		for (Database database : databases)
			mergeDatabase(database);
	}

	public Map<String, Location> getLocationList() {
		return locationList;
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

	public Map<String, ActorClass> getActorClassList() {
		return actorClassList;
	}

	public Map<String, Actor> getActorList() {
		return actorList;
	}

	public int getDatabaseSize() {
		return getGlobalList().size() + getItemList().size() + getActorClassList().size() + getRaceList().size()
				+ getActorList().size() + getLocationList().size();
	}

}
