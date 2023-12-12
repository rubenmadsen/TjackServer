package com.rubenmadsen.TjackServer.Packet;

public class InfoPacket extends AChessPacket{
    public String message;

    public InfoPacket(String message){
        this.message = message;
    }
}
