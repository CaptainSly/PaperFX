package io.azraein.paperfx.system.locations.buildings;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import io.azraein.paperfx.system.Paper;
import io.azraein.paperfx.system.actors.Npc;

public class Building implements Serializable {

    private static final long serialVersionUID = -7237720152755421182L;

    private String buildingId;

    private BuildingType buildingType;

    private final List<String> buildingActionIds;

    private BuildingState buildingState;

    public Building(String buildingId, String buildingName, String buildingDescription) {
        this.buildingId = buildingId;
        this.buildingState = new BuildingState(buildingName, buildingDescription);

        buildingActionIds = new ArrayList<>();
    }

    public void update() {
        for (String npcId : buildingState.getBuildingNpcs()) {
            Npc npc = Paper.DATABASE.getNpc(npcId);
            npc.updateActor();
        }
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public List<String> getBuildingActionIds() {
        return buildingActionIds;
    }

    public BuildingState getBuildingState() {
        return buildingState;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;
    }

    public void setBuildingActionIds(List<String> actionIds) {
        buildingActionIds.addAll(actionIds);
    }

}
