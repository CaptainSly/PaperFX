package io.azraein.penfx.controls.converters;

import io.azraein.inkfx.system.actors.classes.ActorRace;
import io.azraein.penfx.PenFX;
import javafx.util.StringConverter;

public class ActorRaceStringConverter extends StringConverter<ActorRace> {

    protected PenFX penFX;

    public ActorRaceStringConverter(PenFX penFX) {
        this.penFX = penFX;
    }

    @Override
    public String toString(ActorRace object) {
        if (object != null)
            return object.getActorRaceId();

        return null;
    }

    @Override
    public ActorRace fromString(String string) {
        return penFX.getObservableActorRaceRegistry().get(string);
    }

}
