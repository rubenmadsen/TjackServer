package com.rubenmadsen.TjackServer.connection;

import io.reactivex.rxjava3.core.Observable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ServerConnection extends Thread{
    List<Socket> clients = new ArrayList<>();
    public Observable<Socket> createSocketObservable(int port) {
        return Observable.create(emitter -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!emitter.isDisposed() && !serverSocket.isClosed()) {
                    // Accept new client connections.
                    System.out.println("Waiting for client");
                    Socket socket = serverSocket.accept();
                    System.out.println("Client trying to connect");
                    // Emit the socket to subscribers.
                    emitter.onNext(socket);
                }
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }

    public void removeClient(Socket client){
        this.clients.remove(client);
        System.out.println("Client connection removed");
    }

    /*public void distribute(ClientConnection sender, String packet, boolean includeSender) throws IOException {
        this.clients.stream().filter(clientConnection -> clientConnection != sender).forEach(clientConnection -> {
            try {
                clientConnection.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if(includeSender)
            this.clients.stream().filter(clientConnection -> clientConnection == sender).forEach(clientConnection -> {
                try {
                    clientConnection.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }*/
}
