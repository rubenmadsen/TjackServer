package com.rubenmadsen.TjackServer.Packet;

public class HostPacket extends ChessPacket {
    public String playerName;
    public HostPacket(String playerName){
        this.playerName = playerName;
    }
}
