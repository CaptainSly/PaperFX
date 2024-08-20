package io.azraein.inkfx.system.locations.buildings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.system.actors.Npc;

public class BuildingState implements Serializable {

    private static final long serialVersionUID = 8084855691057690902L;

    private String buildingName;
    private String buildingDescription;

    private List<String> buildingNpcs;

    public BuildingState(String buildingName, String buildingDescription) {
        this.buildingName = buildingName;
        this.buildingDescription = buildingDescription;

        buildingNpcs = new ArrayList<>();
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingDescription() {
        return buildingDescription;
    }

    public void setBuildingDescription(String buildingDescription) {
        this.buildingDescription = buildingDescription;
    }

    public List<String> getBuildingNpcs() {
        return buildingNpcs;
    }

    public void setBuildingNpcs(List<String> buildingNpcs) {
        this.buildingNpcs = buildingNpcs;
    }

    public void addNpc(Npc npc) {
        buildingNpcs.add(npc.getActorId());
    }

}
