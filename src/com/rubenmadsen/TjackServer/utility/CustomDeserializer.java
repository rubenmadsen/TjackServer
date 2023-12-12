package com.rubenmadsen.TjackServer.utility;

import com.google.gson.*;
import com.rubenmadsen.TjackServer.Packet.HostPacket;

import java.lang.reflect.Type;

public class CustomDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElem = jsonObject.get("type");
        Class<?> cls = null;
        if (typeElem != null) {
            String type = typeElem.getAsString();
            System.out.println("Type element as string: " + type);
            switch (type){
                case "HostPacket" -> {
                    cls = HostPacket.class;
                    System.out.println("Found HostPacket");
                }
                case "JoinPacket" -> {

                }

            }
            return context.deserialize(jsonObject, cls);
        }
        return null;
    }
}
