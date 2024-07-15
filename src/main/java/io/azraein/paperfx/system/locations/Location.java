package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

	private static final long serialVersionUID = 1285907871914440413L;

	private String locationId;
	private String locationName;
	private String locationDescription;

	private Location[] locationNeighbors;
	private List<String> locationSubLocations;

	private List<String> locationActors;

	public Location(String locationId, String locationName, String locationDescription) {
		this.locationId = locationId;
		this.locationName = locationName;
		this.locationDescription = locationDescription;

		locationNeighbors = new Location[4];
		locationSubLocations = new ArrayList<>();
		locationActors = new ArrayList<>();
	}

	public String getLocationId() {
		return locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public Location[] getLocationNeighbors() {
		return locationNeighbors;
	}

	public List<String> getLocationSubLocations() {
		return locationSubLocations;
	}

	public List<String> getLocationActors() {
		return locationActors;
	}

}
