package com.rubenmadsen.TjackServer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.rubenmadsen.TjackServer.connection.ClientConnection;
import com.rubenmadsen.TjackServer.connection.ServerConnection;

public class ChessServer extends Thread{
    private int port;
    ServerConnection serverConnection;
    public ChessServer(int port){
        this.port = port;
        this.serverConnection = new ServerConnection();
        this.setup();
    }

    public void setup(){
        this.serverConnection = new ServerConnection();
        Observable<ClientConnection> serverObservable =  serverConnection.createSocketObservable(this.port);
        serverObservable.subscribeOn(Schedulers.io())
            .subscribe(clientConnection -> {
                System.out.println("Client connected from: " + clientConnection.getSocket().getInetAddress() + " on port: " + clientConnection.getSocket().getPort());
                clientConnection.send("Welcome message");
                // Start listening to socket
                System.out.println("Start receiving from socket");
                clientConnection.receive().subscribeOn(Schedulers.io()).subscribe(data -> {
                    System.out.println("Distributing to clients");
                    // If ShapePacket
                    this.serverConnection.distribute(clientConnection, data,false);

                }, throwable -> {
                    System.out.println("Client socket disconnected");
                    serverConnection.removeClient(clientConnection);
                },() ->{
                    System.out.println("Client on Complete");
                });
            });
    }
}
