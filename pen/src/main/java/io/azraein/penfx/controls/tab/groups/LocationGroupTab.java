package io.azraein.penfx.controls.tab.groups;

import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.tab.locationTab.BuildingTab;
import io.azraein.penfx.controls.tab.locationTab.LocationTab;

public class LocationGroupTab extends PaperGroupTab {

    public LocationGroupTab(PenFX penFX) {
        super(penFX);
        setText("Location Editors");
        addTab(new LocationTab(penFX));
        addTab(new BuildingTab(penFX));
    }

}
