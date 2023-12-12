package com.rubenmadsen.TjackServer.Packet;

public class JoinedPacket extends ChessPacket{
    public String playerName;
    public JoinedPacket(String playerName){
        this.playerName = playerName;
    }
}
