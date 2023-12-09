package com.rubenmadsen.TjackServer.connection;

import io.reactivex.rxjava3.core.Observable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientConnection {
    /*Socket socket;
    OutputStream os;
    InputStream is;
    BufferedInputStream bis;
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
        this.os = this.socket.getOutputStream();
        this.is = this.socket.getInputStream();
        this.bis = new BufferedInputStream(this.is);
    }*/

    static public Observable<String> receive(Socket client) throws IOException {
        OutputStream os = client.getOutputStream();
        InputStream is = client.getInputStream();
        return Observable.create(emitter -> {
            BufferedInputStream bis = new BufferedInputStream(is);
                System.out.println("Connected to server");
                while (!emitter.isDisposed() && !client.isClosed()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        // Process the bytes
                        String str = new String(buffer, 0, bytesRead);
                        emitter.onNext(str);
                    }
                }
            emitter.onError(new Throwable("Socket knas"));
        });
    }

    static public void send(Socket client,String packet) throws IOException {
        byte[] bytes = packet.getBytes(StandardCharsets.UTF_8);
        System.out.println("bytes: [" + Arrays.toString(bytes) + "]");
        client.getOutputStream().write(bytes);
    }
}
