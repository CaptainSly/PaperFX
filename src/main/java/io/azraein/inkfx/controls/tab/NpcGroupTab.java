package io.azraein.inkfx.controls.tab;

import io.azraein.inkfx.InkFX;
import io.azraein.inkfx.controls.tab.actorTabs.ActorClassTab;
import io.azraein.inkfx.controls.tab.actorTabs.ActorRaceTab;
import io.azraein.inkfx.controls.tab.actorTabs.NpcTab;

public class NpcGroupTab extends PaperGroupTab {

    public NpcGroupTab(InkFX inkFX) {
        super(inkFX);
        setText("NPC Editor");
        
        addTab(new ActorClassTab(inkFX));
        addTab(new ActorRaceTab(inkFX));
        addTab(new NpcTab(inkFX));
    }

}
