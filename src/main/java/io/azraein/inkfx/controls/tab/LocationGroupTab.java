package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.locationTab.LocationTab;

public class LocationGroupTab extends PaperGroupTab {

    public LocationGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("Location Editor");
        addTab(new LocationTab(inkFX));
    }

}
