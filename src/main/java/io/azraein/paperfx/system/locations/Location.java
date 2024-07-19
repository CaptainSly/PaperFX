package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

	private static final long serialVersionUID = 1285907871914440413L;

	private String locationId;

	private String[] locationNeighbors;
	private List<String> locationSubLocations;

	private LocationState locationState;

	public Location(String locationId, String locationName, String locationDescription) {
		this.locationId = locationId;
		locationState = new LocationState(locationName, locationDescription);

		locationNeighbors = new String[4];
		locationSubLocations = new ArrayList<>();
	}

	public String getLocationId() {
		return locationId;
	}

	public LocationState getLocationState() {
		return locationState;
	}

	public void setLocationState(LocationState locationState) {
		this.locationState = locationState;
	}

	public String[] getLocationNeighbors() {
		return locationNeighbors;
	}

	public List<String> getLocationSubLocations() {
		return locationSubLocations;
	}

}
