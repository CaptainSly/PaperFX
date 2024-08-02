package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationState implements Serializable {

	private static final long serialVersionUID = 1265793962596734550L;
	private String locationName;
	private String locationDescription;

	private List<String> locationNpcIds;
	private List<String> locationCreatureIds;

	public LocationState(String locationName, String locationDescription) {
		this.locationName = locationName;
		this.locationDescription = locationDescription;
		locationNpcIds = new ArrayList<>();
		locationCreatureIds = new ArrayList<>();
	}

	public String getLocationName() {
		return locationName;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public List<String> getLocationNpcIds() {
		return locationNpcIds;
	}

	public List<String> getLocationCreatureIds() {
		return locationCreatureIds;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public void setLocationNpcIds(List<String> locationNpcIds) {
		this.locationNpcIds = locationNpcIds;
	}

	public void setLocationCreatureIds(List<String> locationCreatureIds) {
		this.locationCreatureIds = locationCreatureIds;
	}

}
