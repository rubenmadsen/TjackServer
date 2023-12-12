package com.rubenmadsen.TjackServer.connection;

import com.rubenmadsen.TjackServer.Packet.AChessPacket;
import com.rubenmadsen.TjackServer.Packet.InfoPacket;
import com.rubenmadsen.TjackServer.Packet.JoinedPacket;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GamePair {
    private List<Socket> players = new ArrayList<>();
    private final String identifier;
    public GamePair(String identifier){
        this.identifier = identifier;
    }

    public void rejoin(Socket player){

    }
    public void addPlayer(Socket socket) throws IOException {
        this.players.add(socket);
        ClientConnection.receive(socket, AChessPacket.class).subscribeOn(Schedulers.io()).subscribe(data -> {
            //this.distributeTo(socket, data);
        },throwable -> {
            System.out.println("Client fucked off");
        }, () ->{
            System.out.println("Client on complete");
        });
        System.out.println("Player " + this.players.size() + " connected");
        if(this.players.size() == 2){
            for (Socket player : this.players){
                //ClientConnection.send(socket,new InfoPacket("Starting"));
            }
        }
    }
    public <T extends AChessPacket> void distributeTo(Socket sender, T packet){
        this.players.stream().filter(socket -> socket != sender).forEach(socket -> {
            try {
                ClientConnection.send(socket, packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Override
    public String toString(){
        StringBuilder mess = new StringBuilder();
        for (int i = 0; i < this.players.size(); i++) {
            mess.append("Player ").append(i).append(1).append(" -> ").append(this.players.get(i).getInetAddress()).append(" on port: ").append(this.players.get(i).getPort());
            mess.append("\n");
        }
        return mess.toString();
    }
}
