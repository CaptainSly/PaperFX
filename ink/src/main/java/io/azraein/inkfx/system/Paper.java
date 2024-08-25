package io.azraein.inkfx.system;

import io.azraein.inkfx.system.actors.Player;
import io.azraein.inkfx.system.actors.dialogue.DialogueParser;
import io.azraein.inkfx.system.io.Database;
import io.azraein.inkfx.system.io.PaperIni;
import io.azraein.inkfx.system.io.audio.AudioManager;
import io.azraein.inkfx.system.io.plugins.PaperPluginLoader;
import io.azraein.inkfx.system.io.scripting.ScriptEngine;
import io.azraein.inkfx.system.locations.Location;
import io.azraein.inkfx.system.world.Calendar;
import io.azraein.inkfx.system.world.World;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Paper {

    // Global Paper Members

    public static Database DATABASE;

    public static Calendar CALENDAR;

    public static PaperPluginLoader PPL;

    public static ScriptEngine SE;

    public static PaperIni INI;

    public static DialogueParser DP;

    public static AudioManager AUDIO;


    // Global Properties

    public static final ObjectProperty<Location> PAPER_LOCATION_PROPERTY = new SimpleObjectProperty<>();
    public static final ObjectProperty<World> PAPER_WORLD_PROPERTY = new SimpleObjectProperty<>();
    public static final ObjectProperty<Player> PAPER_PLAYER_PROPERTY = new SimpleObjectProperty<>();

    // Global Observables

    public static final ObservableMap<String, Object> PAPER_GAME_GLOBALS = FXCollections.observableHashMap();

}
