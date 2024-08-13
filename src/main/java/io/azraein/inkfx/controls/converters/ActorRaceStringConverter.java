package io.azraein.inkfx.controls.converters;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.actors.classes.ActorRace;
import javafx.util.StringConverter;

public class ActorRaceStringConverter extends StringConverter<ActorRace> {

    protected InkFX inkFX;

    public ActorRaceStringConverter(InkFX inkFX) {
        this.inkFX = inkFX;
    }

    @Override
    public String toString(ActorRace object) {
        if (object != null)
            return object.getActorRaceId();

        return null;
    }

    @Override
    public ActorRace fromString(String string) {
        return inkFX.getObservableActorRaceRegistry().get(string);
    }

}
