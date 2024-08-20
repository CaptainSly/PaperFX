package io.azraein.inkfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.actors.Npc;
import io.azraein.inkfx.system.locations.buildings.Building;

public class Location implements Serializable {

	private static final long serialVersionUID = 1285907871914440413L;

	private final String locationId;

	private final String[] locationNeighbors;
	private final List<String> locationBuildingIds;
	private final List<String> locationActionIds;

	private LocationState locationState;

	public Location(String locationId, String locationName, String locationDescription) {
		this.locationId = locationId;
		locationState = new LocationState(locationName, locationDescription);

		locationNeighbors = new String[4];
		locationBuildingIds = new ArrayList<>();
		locationActionIds = new ArrayList<>();
	}

	public void update() {

		for (String npcId : locationState.getLocationNpcIds()) {
			Npc npc = Paper.DATABASE.getNpc(npcId);
			npc.updateActor();
		}

		// Update the location buildings as well just incase they have npcs.
		for (String buildingId : locationBuildingIds) {
			Building building = Paper.DATABASE.getBuilding(buildingId);
			building.update();
		}

	}

	public String getLocationId() {
		return locationId;
	}

	public LocationState getLocationState() {
		return locationState;
	}

	public String[] getLocationNeighbors() {
		return locationNeighbors;
	}

	public List<String> getLocationBuildingIds() {
		return locationBuildingIds;
	}

	public List<String> getLocationActionIds() {
		return locationActionIds;
	}

	public void setActionIds(List<String> locationActionIds) {
		this.locationActionIds.addAll(locationActionIds);
	}

	public void setBuildingIds(List<String> locationBuildingIds) {
		this.locationBuildingIds.addAll(locationBuildingIds);
	}

	public void setNeighborLocation(Direction dir, Location location) {
		locationNeighbors[dir.ordinal()] = location.getLocationId();
		location.getLocationNeighbors()[dir.opposite().ordinal()] = this.getLocationId();
	}

	public void setLocationState(LocationState locationState) {
		this.locationState = locationState;
	}

}
