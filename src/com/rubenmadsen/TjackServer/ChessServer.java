package com.rubenmadsen.TjackServer;

import com.rubenmadsen.TjackServer.Packet.*;
import com.rubenmadsen.TjackServer.connection.GamePair;
import com.rubenmadsen.TjackServer.utility.Generate;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import com.rubenmadsen.TjackServer.connection.ClientConnection;
import com.rubenmadsen.TjackServer.connection.ServerConnection;

import javax.swing.text.Utilities;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class ChessServer extends Thread{
    private int port;
    ServerConnection serverConnection;
    private Map<String, GamePair> games = new Hashtable<>();
    public ChessServer(int port){
        this.port = port;
        this.serverConnection = new ServerConnection();
        this.setup();
    }
    public synchronized void startGame(String identifier){
        this.games.put(identifier,new GamePair(identifier));
    }
    public synchronized void endGame(String identifier){
        this.games.remove(identifier);
    }

    public void printGame(String identifier){
        if (this.games.containsKey(identifier)){
            GamePair gamePair = this.games.get(identifier);
            System.out.println(gamePair.toString());
        }
    }
    public void tellAll(String message){
        this.games.values().stream().forEach(game -> {
            InfoPacket infoPacket = new InfoPacket(message);
            game.distributeTo(null, infoPacket);
        });
    }
    public void setup(){
        this.serverConnection = new ServerConnection();
        Observable<Socket> serverObservable =  serverConnection.createSocketObservable(this.port);
        serverObservable.subscribeOn(Schedulers.io())
            // Subscribe to greeting
            .subscribe(client -> {

                Observable<? extends AChessPacket> ClientConnectionObservable = ClientConnection.receive(client,AChessPacket.class);
                Disposable disposable = ClientConnectionObservable.firstElement().subscribeOn(Schedulers.io()).subscribe(packet -> {
                    // Packets
                    if (packet instanceof HostPacket hostPacket) {
                        String id = Generate.generateId(8);
                        GamePair gamePair = new GamePair(id);
                        this.games.put(id, gamePair);
                        gamePair.addPlayer(client,hostPacket.playerName);
                        JoinedPacket response = new JoinedPacket(hostPacket.playerName);
                        response.id = "dolk";//id;
                        ClientConnection.send(client, response);
                        //gamePair.distributeTo(null,response);
                        System.out.println("This guy connected:" + hostPacket.playerName);
                    }
                    else if (packet instanceof JoinPacket joinPacket){
                        GamePair gamePair = this.games.get("dolk");
                        if (!gamePair.isFull()){
                            gamePair.addPlayer(client, joinPacket.playerName);
                            JoinedPacket response = new JoinedPacket(joinPacket.playerName);
                            //gamePair.distributeTo(client,response);
                            ClientConnection.send(client, response);
                            System.out.println("This second guy:" + joinPacket.playerName + " connected to:" + joinPacket.identifier);
                        }
                    }

                }, throwable -> {
                    // Incoming data error
                    System.out.println("Client socket disconnected");
                    serverConnection.removeClient(client);
                },() ->{
                    // Incoming data complete
                    System.out.println("Client on Complete");
                });


            },throwable -> {
                // Client listener error
                System.out.println("Client connection problem");
            }, () -> {
                // Client listener complete
                System.out.println("Server on Complete");
            });
    }
}
