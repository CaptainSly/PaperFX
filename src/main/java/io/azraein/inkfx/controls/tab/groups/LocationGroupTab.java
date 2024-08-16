package io.azraein.inkfx.controls.tab.groups;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.locationTab.BuildingTab;
import io.azraein.inkfx.controls.tab.locationTab.LocationTab;

public class LocationGroupTab extends PaperGroupTab {

    public LocationGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("Location Editors");
        addTab(new LocationTab(inkFX));
        addTab(new BuildingTab(inkFX));
    }

}
