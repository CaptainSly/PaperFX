package io.azraein.paperfx.system.io.type_adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.azraein.paperfx.system.actors.Actor;
import io.azraein.paperfx.system.actors.Npc;

import java.io.IOException;

public class ActorTypeAdapter extends TypeAdapter<Actor> {

    private final Gson gson;

    public ActorTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, Actor actor) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", actor.getActorType());

        // Use Gson to serialize other fields
        jsonObject.add("actorId", gson.toJsonTree(actor.getActorId()));
        jsonObject.add("actorState", gson.toJsonTree(actor.getActorState()));

        gson.toJson(jsonObject, out);
    }

    @Override
    public Actor read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        Actor actor = null;

        switch (type) {
            case "npc" -> actor = gson.fromJson(jsonObject, Npc.class);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
        // Add other subclasses here
        // case "player":
        // actor = gson.fromJson(jsonObject, Player.class);
        // break;

        return actor;
    }
}