package com.rubenmadsen.TjackServer.Packet;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class AChessPacket implements Serializable {
    public String type = this.getClass().getSimpleName();;
    public String id;
    public String encodeJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
