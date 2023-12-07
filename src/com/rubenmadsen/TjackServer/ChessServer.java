package com.rubenmadsen.TjackServer;

import com.rubenmadsen.TjackServer.connection.GamePair;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.rubenmadsen.TjackServer.connection.ClientConnection;
import com.rubenmadsen.TjackServer.connection.ServerConnection;

import java.net.Socket;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ChessServer extends Thread{
    private int port;
    ServerConnection serverConnection;
    private Map<String, GamePair> games = new Hashtable<>();
    public ChessServer(int port){
        this.port = port;
        this.serverConnection = new ServerConnection();
        this.setup();
    }
    public void printGame(String identifier){
        if (this.games.containsKey(identifier)){
            GamePair gamePair = this.games.get(identifier);
            System.out.println(gamePair.toString());

        }
    }
    public void setup(){
        this.serverConnection = new ServerConnection();
        Observable<Socket> serverObservable =  serverConnection.createSocketObservable(this.port);
        serverObservable.subscribeOn(Schedulers.io())
            // Subscribe to greeting
            .subscribe(client -> {
                System.out.println("Client connected from: " + client.getInetAddress() + " on port: " + client.getPort());
                //client.send("Welcome message");
                // Start listening to socket
                AtomicReference<String> identifier = new AtomicReference<>();
                System.out.println("Start receiving from socket");
                Disposable disposable = ClientConnection.receive(client).take(1).subscribe(data -> {
                    System.out.println("Receive greeting: " + data);
                    if(!this.games.containsKey(data))
                        this.games.put(data,new GamePair(data));
                    identifier.set(data);

                }, throwable -> {
                    System.out.println("Client socket disconnected");
                    serverConnection.removeClient(client);
                },() ->{
                    System.out.println("Client on Complete");
                });
                disposable.dispose();
                GamePair gamePair = games.get(identifier.get());
                gamePair.addPlayer(client);

            },throwable -> {
                System.out.println("Client connection problem");
            }, () -> {
                System.out.println("Server on Complete");
            });
    }
}
