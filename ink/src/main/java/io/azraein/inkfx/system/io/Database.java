package io.azraein.inkfx.system.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.azraein.inkfx.system.Action;
import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.inventory.items.Item;
import io.azraein.inkfx.system.inventory.items.Lootlist;
import io.azraein.inkfx.system.inventory.items.equipment.Equipment;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.inkfx.system.quest.Quest;

public class Database implements Serializable {

	private static final long serialVersionUID = -5878465281727704975L;

	private Map<String, Object> globalRegistry = new HashMap<>();
	private Map<String, Item> itemRegistry = new HashMap<>();
	private Map<String, Equipment> equipmentRegistry = new HashMap<>();
	private Map<String, Lootlist> lootlistRegistry = new HashMap<>();
	private Map<String, ActorRace> actorRaceRegistry = new HashMap<>();
	private Map<String, ActorClass> actorClassRegistry = new HashMap<>();
	private Map<String, Npc> npcRegistry = new HashMap<>();
	private Map<String, Building> buildingRegistry = new HashMap<>();
	private Map<String, Location> locationRegistry = new HashMap<>();
	private Map<String, Quest> questRegistry = new HashMap<>();
	private Map<String, Action> actionRegistry = new HashMap<>();

	public void addEquipment(Equipment equipment) {
		equipmentRegistry.put(equipment.getEquipmentId(), equipment);
	}

	public Equipment getEquipment(String equipmentId) {
		return equipmentRegistry.get(equipmentId);
	}

	public void addAction(Action action) {
		actionRegistry.put(action.getActionId(), action);
	}

	public Action getAction(String actionId) {
		return actionRegistry.get(actionId);
	}

	public Lootlist getLootlist(String lootlistId) {
		return lootlistRegistry.get(lootlistId);
	}

	public void addLootlist(Lootlist lootlist) {
		lootlistRegistry.put(lootlist.getLootlistId(), lootlist);
	}

	public void addQuest(Quest quest) {
		questRegistry.put(quest.getQuestId(), quest);
	}

	public Quest getQuest(String questId) {
		return questRegistry.get(questId);
	}

	public Building getBuilding(String buildingId) {
		return buildingRegistry.get(buildingId);
	}

	public void addBuilding(Building building) {
		buildingRegistry.put(building.getBuildingId(), building);
	}

	public Item getItem(String itemId) {
		return itemRegistry.get(itemId);
	}

	public void addItem(Item item) {
		itemRegistry.put(item.getItemId(), item);
	}

	public Object getGlobal(String globalId) {
		return globalRegistry.get(globalId);
	}

	public void setGlobal(String globalId, Object global) {
		globalRegistry.replace(globalId, global);
		Paper.PAPER_GAME_GLOBALS.replace(globalId, global);
		Paper.PAPER_WORLD_PROPERTY.get().getWorldGlobalsMap().replace(globalId, global);
	}

	public void addGlobal(String globalId, Object global) {
		globalRegistry.put(globalId, global);
	}

	public ActorRace getCharacterRace(String characterRaceId) {
		return actorRaceRegistry.get(characterRaceId);
	}

	public void addCharacterRace(ActorRace characterRace) {
		actorRaceRegistry.put(characterRace.getActorRaceId(), characterRace);
	}

	public ActorClass getActorClass(String charClassId) {
		return actorClassRegistry.get(charClassId);
	}

	public void addActorClass(ActorClass characterClass) {
		actorClassRegistry.put(characterClass.getActorClassId(), characterClass);
	}

	public Location getLocation(String locationId) {
		return locationRegistry.get(locationId);
	}

	public void addLocation(Location location) {
		locationRegistry.put(location.getLocationId(), location);
	}

	public Npc getNpc(String actorId) {
		return npcRegistry.get(actorId);
	}

	public void addNpc(Npc npc) {
		npcRegistry.put(npc.getActorId(), npc);
	}

	public void mergeDatabase(Database database) {
		this.getGlobalRegistry().putAll(database.getGlobalRegistry());
		this.getItemRegistry().putAll(database.getItemRegistry());
		this.getActorClassRegistry().putAll(database.getActorClassRegistry());
		this.getActorRaceRegistry().putAll(database.getActorRaceRegistry());
		this.getNpcRegistry().putAll(database.getNpcRegistry());
		this.getBuildingRegistry().putAll(database.getBuildingRegistry());
		this.getLocationRegistry().putAll(database.getLocationRegistry());
		this.getQuestRegistry().putAll(database.getQuestRegistry());
		this.getLootlistRegistry().putAll(database.getLootlistRegistry());
		this.getActionRegistry().putAll(database.getActionRegistry());
		this.getEquipmentRegistry().putAll(database.getEquipmentRegistry());
	}

	public void mergeDatabase(Database... databases) {
		for (Database database : databases)
			mergeDatabase(database);
	}

	public Map<String, Equipment> getEquipmentRegistry() {
		return equipmentRegistry;
	}

	public Map<String, Action> getActionRegistry() {
		return actionRegistry;
	}

	public Map<String, Location> getLocationRegistry() {
		return locationRegistry;
	}

	public Map<String, Object> getGlobalRegistry() {
		return globalRegistry;
	}

	public Map<String, Item> getItemRegistry() {
		return itemRegistry;
	}

	public Map<String, Lootlist> getLootlistRegistry() {
		return lootlistRegistry;
	}

	public Map<String, ActorRace> getActorRaceRegistry() {
		return actorRaceRegistry;
	}

	public Map<String, ActorClass> getActorClassRegistry() {
		return actorClassRegistry;
	}

	public Map<String, Quest> getQuestRegistry() {
		return questRegistry;
	}

	public Map<String, Npc> getNpcRegistry() {
		return npcRegistry;
	}

	public Map<String, Building> getBuildingRegistry() {
		return buildingRegistry;
	}

	public int getDatabaseSize() {
		return getGlobalRegistry().size() + getItemRegistry().size() + getActorClassRegistry().size()
				+ getActorRaceRegistry().size() + getNpcRegistry().size() + getBuildingRegistry().size()
				+ getLocationRegistry().size() + getQuestRegistry().size() + getLootlistRegistry().size();
	}

}
