package com.rubenmadsen.TjackServer.Packet;

import com.google.gson.Gson;

import java.io.Serializable;

public  class AChessPacket implements Serializable {
    public String type = this.getClass().getSimpleName();;
    public String id;


}
