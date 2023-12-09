package com.rubenmadsen.TjackServer.connection;

import com.rubenmadsen.TjackServer.Packet.ChessPacket;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GamePair {
    private List<Socket> players = new ArrayList<>();
    private String identifier;
    public GamePair(String identifier){
        this.identifier = identifier;
    }

    public void addPlayer(Socket socket) throws IOException {
        this.players.add(socket);
        ClientConnection.receive(socket).subscribeOn(Schedulers.io()).subscribe(data -> {
            ChessPacket packet = new ChessPacket();
            //this.distributeTo(socket, data);
        },throwable -> {

        }, () ->{

        });
        ClientConnection.send(socket,"Welcome");
        System.out.println("Player " + this.players.size() + " connected");
        if(this.players.size() == 2){
            for (Socket player : this.players){
                ClientConnection.send(socket,"Starting");
            }
        }
    }
    public void distributeTo(Socket sender, String data){
        this.players.stream().filter(socket -> socket != sender).forEach(socket -> {
            try {
                OutputStream os = socket.getOutputStream();
                os.write(data.getBytes());
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
