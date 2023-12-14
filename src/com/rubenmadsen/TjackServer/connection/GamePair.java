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
    private List<Socket> players = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private final String identifier;
    public GamePair(String identifier){
        this.identifier = identifier;
    }

    public void rejoin(Socket player){

    }
    public boolean isFull(){
        return this.players.size() == 2;
    }
    public void addPlayer(Socket socket, String name) throws IOException, InterruptedException {
        this.players.add(socket);
        this.playerNames.add(name);
        ClientConnection.receive(socket, AChessPacket.class).subscribeOn(Schedulers.computation()).subscribe(data -> {
            //this.distributeTo(socket, data);
            System.out.println("Observable data:");
            distributeTo(socket, data);
            System.out.println();
        },throwable -> {
            System.out.println("Client fucked off");
            this.players.remove(socket);
        }, () ->{
            System.out.println("Client on complete");
        });
        System.out.println("Player " + this.players.size() + " connected");
        if(this.players.size() == 2){
            StartPacket startPacket = new StartPacket(this.playerNames.get(0),this.playerNames.get(1));
            startPacket.id = this.identifier;
            //Thread.currentThread().wait(1000);
            this.distributeTo(null,startPacket);
        }
    }
    public <T extends AChessPacket> void distributeTo(Socket sender, T packet){
        System.out.println("Player count "+this.players.stream().count());
        this.players.stream().forEach(socket -> {
            try {
                System.out.println("Send in filter");
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
