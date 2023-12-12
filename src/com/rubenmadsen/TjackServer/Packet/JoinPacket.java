package com.rubenmadsen.TjackServer.Packet;

public class JoinPacket extends ChessPacket{
    public String playerName;
    public JoinPacket(String identifier, String playerName){
        this.id = identifier;
        this.playerName = playerName;
    }
}
