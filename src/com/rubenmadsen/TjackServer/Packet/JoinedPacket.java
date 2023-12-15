package com.rubenmadsen.TjackServer.Packet;

public class JoinedPacket extends ChessPacket{
    public String playerName;
    public int playerNo;
    public JoinedPacket(String playerName, int playerNo){
        this.playerName = playerName;
        this.playerNo = playerNo;
    }
}
