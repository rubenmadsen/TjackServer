package com.rubenmadsen.TjackServer.Packet;

import java.awt.*;

public class MovePacket extends ChessPacket{
    public Point to;
    public Point from;

    public MovePacket(Point from, Point to){
        this.from = from;
        this.to = to;
    }
}
