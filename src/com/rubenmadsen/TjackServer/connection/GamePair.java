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
            this.distributeTo(socket, packet);
        },throwable -> {

        }, () ->{

        });
    }
    public void distributeTo(Socket sender, ChessPacket packet){
        this.players.stream().filter(socket -> socket != sender).forEach(socket -> {
            try {
                OutputStream os = socket.getOutputStream();
                //os.write();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
