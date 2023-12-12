package com.rubenmadsen.TjackServer.Packet;

import java.awt.*;

public class MovePacket extends AChessPacket{
    public Point from;
    public Point to;

    public MovePacket(Point from, Point to){
        this.from = from;
        this.to = to;
    }
}
