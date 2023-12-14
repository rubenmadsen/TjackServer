package com.rubenmadsen.TjackServer.Packet;

public class StartPacket extends ChessPacket{
    public String player1;
    public String player2;

    public StartPacket(String player1, String player2){
        this.player1 = player1;
        this.player2 = player2;
    }
}
