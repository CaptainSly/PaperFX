package io.azraein.inkfx.system.io;

import io.azraein.inkfx.system.Action;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.inkfx.system.actors.dialogue.Topic;
import io.azraein.inkfx.system.inventory.items.Item;
import io.azraein.inkfx.system.inventory.items.Lootlist;
import io.azraein.inkfx.system.inventory.items.equipment.Equipment;
import io.azraein.inkfx.system.io.plugins.PaperPlugin;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.inkfx.system.quest.Quest;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ObservableDatabase {

    private final ObservableMap<String, Object> observableGlobalRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Item> observableItemRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Equipment> observableEquipmentRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Lootlist> observableLootlistRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, ActorRace> observableActorRaceRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, ActorClass> observableActorClassRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Npc> observableNpcRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Building> observableBuildingRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Location> observableLocationRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Quest> observableQuestRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Action> observableActionRegistry = FXCollections.observableHashMap();
    private final ObservableMap<String, Topic> observableTopicRegistry = FXCollections.observableHashMap();

    public void mergeDatabase(Database database) {
        this.getObservableGlobalRegistry().putAll(database.getGlobalRegistry());
        this.getObservableItemRegistry().putAll(database.getItemRegistry());
        this.getObservableActorClassRegistry().putAll(database.getActorClassRegistry());
        this.getObservableActorRaceRegistry().putAll(database.getActorRaceRegistry());
        this.getObservableNpcRegistry().putAll(database.getNpcRegistry());
        this.getObservableBuildingRegistry().putAll(database.getBuildingRegistry());
        this.getObservableLocationRegistry().putAll(database.getLocationRegistry());
        this.getObservableQuestRegistry().putAll(database.getQuestRegistry());
        this.getObservableLootlistRegistry().putAll(database.getLootlistRegistry());
        this.getObservableActionRegistry().putAll(database.getActionRegistry());
        this.getObservableEquipmentRegistry().putAll(database.getEquipmentRegistry());
        this.getObservableTopicRegistry().putAll(database.getTopicRegistry());
    }

    public void clearDatabase() {
        this.getObservableGlobalRegistry().clear();
        this.getObservableItemRegistry().clear();
        this.getObservableActorClassRegistry().clear();
        this.getObservableActorRaceRegistry().clear();
        this.getObservableNpcRegistry().clear();
        this.getObservableLocationRegistry().clear();
        this.getObservableBuildingRegistry().clear();
        this.getObservableQuestRegistry().clear();
        this.getObservableLootlistRegistry().clear();
        this.getObservableActionRegistry().clear();
        this.getObservableEquipmentRegistry().clear();
        this.getObservableTopicRegistry().clear();
    }

    public ObservableMap<String, Equipment> getObservableEquipmentRegistry() {
        return observableEquipmentRegistry;
    }

    public ObservableMap<String, Action> getObservableActionRegistry() {
        return observableActionRegistry;
    }

    public ObservableMap<String, Topic> getObservableTopicRegistry() {
        return observableTopicRegistry;
    }

    public ObservableMap<String, Object> getObservableGlobalRegistry() {
        return observableGlobalRegistry;
    }

    public ObservableMap<String, Quest> getObservableQuestRegistry() {
        return observableQuestRegistry;
    }

    public ObservableMap<String, Building> getObservableBuildingRegistry() {
        return observableBuildingRegistry;
    }

    public ObservableMap<String, Item> getObservableItemRegistry() {
        return observableItemRegistry;
    }

    public ObservableMap<String, Lootlist> getObservableLootlistRegistry() {
        return observableLootlistRegistry;
    }

    public ObservableMap<String, ActorRace> getObservableActorRaceRegistry() {
        return observableActorRaceRegistry;
    }

    public ObservableMap<String, ActorClass> getObservableActorClassRegistry() {
        return observableActorClassRegistry;
    }

    public ObservableMap<String, Npc> getObservableNpcRegistry() {
        return observableNpcRegistry;
    }

    public ObservableMap<String, Location> getObservableLocationRegistry() {
        return observableLocationRegistry;
    }

}
