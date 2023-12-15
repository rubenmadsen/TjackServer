package com.rubenmadsen.TjackServer.Packet;

public class AChatPacket extends ChessPacket{
    String sender;

    public AChatPacket(String sender){
        this.sender = sender;
    }
}
