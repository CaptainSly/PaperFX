package io.azraein.penfx.controls.converters;

import io.azraein.inkfx.system.actors.classes.ActorClass;
import io.azraein.penfx.PenFX;
import javafx.util.StringConverter;

public class ActorClassStringConverter extends StringConverter<ActorClass> {

    protected PenFX penFX;

    public ActorClassStringConverter(PenFX penFX) {
        this.penFX = penFX;
    }

    @Override
    public String toString(ActorClass object) {
        if (object != null)
            return object.getActorClassId();

        return null;
    }

    @Override
    public ActorClass fromString(String string) {
        return penFX.getObservableActorClassRegistry().get(string);
    }

}
