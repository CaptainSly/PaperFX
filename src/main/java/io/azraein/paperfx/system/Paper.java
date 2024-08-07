package io.azraein.paperfx.system;

import io.azraein.paperfx.system.io.Database;
import io.azraein.paperfx.system.io.PaperIni;
import io.azraein.paperfx.system.io.plugins.PaperPluginLoader;
import io.azraein.paperfx.system.io.scripting.ScriptEngine;
import io.azraein.paperfx.system.locations.Location;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Paper {

    // Global Paper Members

    public static Database DATABASE;

    public static PaperPluginLoader PPL;

    public static ScriptEngine SE;

    public static PaperIni INI;


    // Global Properties

    public static final ObjectProperty<Location> PAPER_LOCATION_PROPERTY = new SimpleObjectProperty<>();

}
