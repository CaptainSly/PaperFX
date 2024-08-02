package io.azraein.inkfx.controls.converters;

import io.azraein.inkfx.InkFX;
import io.azraein.paperfx.system.actors.classes.ActorClass;
import javafx.util.StringConverter;

public class ActorClassStringConverter extends StringConverter<ActorClass> {

    protected InkFX inkFX;

    public ActorClassStringConverter(InkFX inkFX) {
        this.inkFX = inkFX;
    }

    @Override
    public String toString(ActorClass object) {
        if (object != null)
            return object.getActorClassId();
            
        return null;
    }

    @Override
    public ActorClass fromString(String string) {
        return inkFX.getActorClassList().get(string);
    }

}
