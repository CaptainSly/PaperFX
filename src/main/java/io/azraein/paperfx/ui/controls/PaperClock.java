package io.azraein.paperfx.ui.controls;

import io.azraein.paperfx.system.world.Calendar;
import javafx.scene.control.Menu;

public class PaperClock extends Menu {

    public void update(Calendar calendar) {
        setText(calendar.getDateAsString() + ", " + calendar.getTimeAsString());
    }

}
