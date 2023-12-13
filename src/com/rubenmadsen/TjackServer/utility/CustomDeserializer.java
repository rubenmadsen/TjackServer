package com.rubenmadsen.TjackServer.utility;

import com.google.gson.*;
import com.rubenmadsen.TjackServer.Packet.*;

import java.lang.reflect.Type;

public class CustomDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElem = jsonObject.get("type");
        Class<?> cls = null;
        if (typeElem != null) {
            String type = typeElem.getAsString();
            //System.out.println("Type element as string: " + type);
            switch (type){
                case "HostPacket" -> {
                    cls = HostPacket.class;
                }
                case "JoinPacket" -> {
                    cls = JoinPacket.class;
                }
                case "MovePacket" -> {
                    cls = MovePacket.class;
                }
                case "ChatMessagePacket" -> {
                    cls = ChatMessagePacket.class;
                }
                default -> {
                    System.out.println("Could not find type in CustomDeserializer");
                }
            }
            return context.deserialize(jsonObject, cls);
        }
        return null;
    }
}
// {"id":"","type":"MovePacket","to":{"x":1,"y":4},"from":{"x":1,"y":6}}
// {"type":"MovePacket","id":"","from":{"y":6,"x":5},"to":{"x":5,"y":4}}