package com.rubenmadsen.TjackServer.Packet;

public class ChatMessagePacket extends AChatPacket{
    public String message;

    public ChatMessagePacket(String sender,String message){
        super(sender);
        this.message = message;
    }
}
