package io.azraein.paperfx.system.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubLocation implements Serializable {

	private static final long serialVersionUID = 3746348487582552602L;

	private String subLocationId;
	private String subLocationName;
	private String subLocationDescription;

	private List<String> subLocationActors;

	public SubLocation(String subLocationId, String subLocationName, String subLocationDescription) {
		this.subLocationId = subLocationId;
		this.subLocationName = subLocationName;
		this.subLocationDescription = subLocationDescription;

		subLocationActors = new ArrayList<>();
	}

	public String getSubLocationId() {
		return subLocationId;
	}

	public String getSubLocationName() {
		return subLocationName;
	}

	public String getSubLocationDescription() {
		return subLocationDescription;
	}

	public List<String> getSubLocationActors() {
		return subLocationActors;
	}

}
