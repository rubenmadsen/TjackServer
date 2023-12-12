package com.rubenmadsen.TjackServer.Packet;

import com.google.gson.Gson;

public class ChessPacket extends AChessPacket{

    static public ChessPacket decodeJson(String jsonString){
        Gson gson = new Gson();
        return gson.fromJson(jsonString, HostPacket.class);
    }
}
