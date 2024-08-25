package io.azraein.inkfx.system.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.azraein.inkfx.system.Options;
import io.azraein.inkfx.system.Utils;
import io.azraein.inkfx.system.actors.Actor;
import io.azraein.inkfx.system.actors.ActorState;
import io.azraein.inkfx.system.actors.Player;
import io.azraein.inkfx.system.io.SaveSystem;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.locations.LocationState;
import io.azraein.inkfx.system.locations.buildings.Building;
import io.azraein.inkfx.system.locations.buildings.BuildingState;

public class World implements Serializable {

    private static final long serialVersionUID = 1156984974936857799L;

    private Player player;
    private Calendar calendar;

    private String currentLocationId;

    private Map<String, LocationState> worldLocationStates;
    private Map<String, BuildingState> worldBuildingStates;
    private Map<String, ActorState> worldActorStates;
    private Map<String, Object> worldGlobals;

    private transient float lastClockUpdate = 0;
    private transient float lastAutoSaveUpdate = 0;

    public World() {
        worldLocationStates = new HashMap<>();
        worldBuildingStates = new HashMap<>();

        worldActorStates = new HashMap<>();
        worldGlobals = new HashMap<>();

        calendar = new Calendar();
    }

    public void update(float delta) {

        lastClockUpdate += delta;
        if (lastClockUpdate >= Utils.getCalendarUpdateInterval(32)) {
            calendar.update();
            lastClockUpdate = 0;
        }

        if (Options.doAutoSave) {
            lastAutoSaveUpdate += delta;
            if (lastAutoSaveUpdate >= Utils.getAutoSaveInterval(Options.autoSaveDuration)) {
                SaveSystem.autoSave();
                lastAutoSaveUpdate = 0;
            }
        }

        if (player != null)
            player.updateActor();
    }

    public void addLocationState(Location location) {
        if (worldLocationStates.containsValue(location.getLocationState())) {
            worldLocationStates.remove(location.getLocationId());
        }

        worldLocationStates.put(location.getLocationId(), location.getLocationState());
    }

    public void addBuildingState(Building building) {
        if (worldBuildingStates.containsValue(building.getBuildingState()))
            worldBuildingStates.remove(building.getBuildingId());

        worldBuildingStates.put(building.getBuildingId(), building.getBuildingState());
    }

    public void addActorState(Actor actor) {
        if (worldActorStates.containsValue(actor.getActorState())) {
            worldActorStates.remove(actor.getActorId());
        }

        worldActorStates.put(actor.getActorId(), actor.getActorState());
    }

    public void addActorState(Actor... actors) {
        for (Actor actor : actors) {
            addActorState(actor);
        }
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public Player getPlayer() {
        return player;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Map<String, Object> getWorldGlobalsMap() {
        return worldGlobals;
    }

    public Map<String, LocationState> getWorldLocationStates() {
        return worldLocationStates;
    }

    public Map<String, BuildingState> getWorldBuildingStates() {
        return worldBuildingStates;
    }

    public Map<String, ActorState> getWorldActorStates() {
        return worldActorStates;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public void setWorldGlobals(Map<String, Object> globalMap) {
        this.worldGlobals.putAll(globalMap);
    }

}
