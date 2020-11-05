package com.seanachaidh.handyparking.Resources;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.seanachaidh.handyparking.Coordinate;

import java.lang.reflect.Type;

public class CoordinateJson implements JsonSerializer<Coordinate>, JsonDeserializer<Coordinate> {
    @Override
    public Coordinate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        double latitude = obj.get("latitude").getAsDouble();
        double longtitude = obj.get("longtitude").getAsDouble();

        return new Coordinate(latitude, longtitude);
    }

    @Override
    public JsonElement serialize(Coordinate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("longtitude", src.getLongtitude());
        obj.addProperty("latitude", src.getLatitude());
        return obj;
    }
}
