package io.azraein.penfx.controls.tab.groups;

import io.azraein.penfx.PenFX;
import io.azraein.penfx.controls.tab.actorTabs.ActorClassTab;
import io.azraein.penfx.controls.tab.actorTabs.ActorRaceTab;
import io.azraein.penfx.controls.tab.actorTabs.NpcTab;

public class NpcGroupTab extends PaperGroupTab {

    public NpcGroupTab(PenFX penFX) {
        super(penFX);
        setText("Actor Editors");
        
        addTab(new ActorClassTab(penFX));
        addTab(new ActorRaceTab(penFX));
        addTab(new NpcTab(penFX));
    }

}
