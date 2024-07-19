package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationState implements Serializable {

	private static final long serialVersionUID = 1265793962596734550L;

	private String locationName;
	private String locationDescription;

	private List<String> actorsList;
	private List<String> creaturesList;

	public LocationState(String locationName, String locationDescription) {
		this.locationName = locationName;
		this.locationDescription = locationDescription;

		actorsList = new ArrayList<>();
		creaturesList = new ArrayList<>();
	}

	public void update() {

	}

	public List<String> getActorsList() {
		return actorsList;
	}

	public List<String> getCreaturesList() {
		return creaturesList;
	}

	public String getLocationName() {
		return locationName;
	}

	public String getLocationDescription() {
		return locationDescription;
	}
}
