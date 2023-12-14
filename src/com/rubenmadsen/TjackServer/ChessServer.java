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
    ClientConnection clientConnection;
    private Map<String, GamePair> games = new Hashtable<>();
    public ChessServer(int port){
        this.port = port;
        this.serverConnection = new ServerConnection();
        //this.clientConnection = new ClientConnection();
        this.setup();
    }
    public synchronized void startGame(String identifier){
        //this.games.put(identifier,new GamePair(identifier));
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
    public void printAllGames(){
        for (String id : this.games.keySet()){
            GamePair gamePair = this.games.get(id);
            System.out.println("Game:" + id);
            System.out.println("\t" + gamePair.toString() + "\n");
        }
    }
    public void tellAll(String message){
        this.games.values().stream().forEach(game -> {
            ChatMessagePacket chatMessagePacket = new ChatMessagePacket(message);
            //game.distributeTo(null, chatMessagePacket);
        });
    }
    public void setup(){
        this.serverConnection = new ServerConnection();
        Observable<ClientConnection> serverObservable =  serverConnection.createSocketObservable(this.port);
        serverObservable.subscribeOn(Schedulers.io()).subscribe(clientConnection -> {
                    System.out.println("Client connected from: " + clientConnection.getSocket().getInetAddress() + " on port: " + clientConnection.getSocket().getPort());
                    //clientConnection.send(drawingPacket);
                    // Start listening to socket
                    System.out.println("Start receiving from socket");
                    clientConnection.receive().subscribeOn(Schedulers.io()).subscribe(packet -> {
                        // Packet type
                        if (packet instanceof HostPacket hostPacket) {
                            clientConnection.setHandle(hostPacket.playerName);
                            String id = Generate.generateId(4);
                            while(this.games.keySet().contains(id)){
                                id = Generate.generateId(4);
                            }
                            GamePair gamePair = new GamePair(id);
                            this.games.put(id, gamePair);
                            gamePair.addPlayer(clientConnection,hostPacket.playerName);
                            JoinedPacket response = new JoinedPacket(hostPacket.playerName);
                            response.id = id;
                            clientConnection.send(response);
                            //gamePair.distributeTo(null,response);
                            System.out.println("Player 1 '" + hostPacket.playerName + "' connect");
                        }
                        else if (packet instanceof JoinPacket joinPacket){
                            clientConnection.setHandle(joinPacket.playerName);
                            GamePair gamePair = this.games.get(joinPacket.id);
                            if (!gamePair.isFull()){
                                JoinedPacket response = new JoinedPacket(joinPacket.playerName);
                                clientConnection.send(response);
                                System.out.println("Player 1 '" + joinPacket.playerName + "' joined '" + joinPacket.id + "'");
                                gamePair.addPlayer(clientConnection, joinPacket.playerName);
                                Thread.sleep(200);
                                if (gamePair.isFull()){
                                    StartPacket startPacket = new StartPacket(gamePair.players.get(0).getHandle(),gamePair.players.get(1).getHandle());
                                    for (ClientConnection client : gamePair.players){
                                        client.send(startPacket);
                                    }
                                }
                            }
                        }
                        else if (packet instanceof MovePacket movePacket || packet instanceof ChatMessagePacket chatMessagePacket){
                            GamePair gamePair = this.games.get(packet.id);
                            for (ClientConnection client : gamePair.players){
                                if (client != clientConnection){
                                    client.send(packet);
                                }
                            }
                        }
                    }, throwable -> {
                        System.out.println("Client socket disconnected");
                        serverConnection.removeClient(clientConnection);
                    },() ->{
                        System.out.println("Client on Complete");
                    });
                });
    }
}
