package com.rubenmadsen.TjackServer.connection;

import io.reactivex.rxjava3.core.Observable;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    Socket socket;
    OutputStream os;
    InputStream is;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    public Socket getSocket(){
        return this.socket;
    }
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.setupSocket();
    }
    public boolean openSocket(String address, int port) {
        try {
            this.socket = new Socket(address, port);
            this.setupSocket();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setupSocket() throws IOException {
        this.oos = new ObjectOutputStream(this.socket.getOutputStream());
        this.ois = new ObjectInputStream(this.socket.getInputStream());
    }

    public Observable<String> receive() {
        return Observable.create(emitter -> {
                System.out.println("Connected to server");
                while (!emitter.isDisposed() && !socket.isClosed()) {
                    // Accept new client connections.
                    String packet;
                    while((packet = (String)this.ois.readObject()) != null){
                        emitter.onNext(packet);
                    }
                }
            emitter.onError(new Throwable("Socket knas"));
        });
    }

    public void send(String packet) throws IOException {
        this.oos.writeObject(packet);
    }
}
