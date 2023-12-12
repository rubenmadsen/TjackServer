package com.rubenmadsen.TjackServer.Packet;

public class JoinPacket extends ChessPacket{
    public String identifier;
    public String playerName;
    public JoinPacket(String identifier, String playerName){
        this.identifier = identifier;
        this.playerName = playerName;
    }
}
