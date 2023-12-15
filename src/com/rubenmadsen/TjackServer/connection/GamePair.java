package com.rubenmadsen.TjackServer.connection;

import com.rubenmadsen.TjackServer.Packet.AChessPacket;
import com.rubenmadsen.TjackServer.Packet.InfoPacket;
import com.rubenmadsen.TjackServer.Packet.JoinedPacket;
import com.rubenmadsen.TjackServer.Packet.StartPacket;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GamePair {
    public List<ClientConnection> players = new ArrayList<>();
    String id;
    public GamePair(String id){
        this.id = id;
    }
    public void addPlayer(ClientConnection clientConnection, String playerName){
        clientConnection.setGamePair(this);
        this.players.add(clientConnection);
    }
    public boolean isFull(){
        return this.players.size() == 2;
    }
    public void removePlayer(ClientConnection clientConnection){
        this.players.remove(clientConnection);
    }
    @Override
    public String toString(){
        StringBuilder mess = new StringBuilder();
        for (int i = 0; i < this.players.size(); i++) {
            mess.append("Player ").append(i).append(1).append(" -> ").append(this.players.get(i).getSocket().getInetAddress()).append(" on port: ").append(this.players.get(i).getSocket().getPort());
            mess.append("\n");
        }
        return mess.toString();
    }
}
