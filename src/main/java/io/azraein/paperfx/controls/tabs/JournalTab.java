package io.azraein.paperfx.controls.tabs;

import javafx.scene.control.Tab;

public abstract class JournalTab extends Tab {

    public JournalTab(String title) {
        this.setText(title);
        setClosable(false);
    }

    public abstract void updateTab();

}
