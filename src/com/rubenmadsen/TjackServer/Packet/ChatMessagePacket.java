package com.rubenmadsen.TjackServer.Packet;

public class ChatMessagePacket extends AChatPacket{
    public String message;

    public ChatMessagePacket(String message){
        this.message = message;
    }
}
