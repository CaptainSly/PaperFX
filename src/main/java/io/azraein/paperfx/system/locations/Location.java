package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

	private static final long serialVersionUID = 1285907871914440413L;

	private final String locationId;

	private final String[] locationNeighbors;
	private final List<String> locationBuildingIds;

	private LocationState locationState;

	public Location(String locationId, String locationName, String locationDescription) {
		this.locationId = locationId;
		locationState = new LocationState(locationName, locationDescription);

		locationNeighbors = new String[4];
		locationBuildingIds = new ArrayList<>();
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
