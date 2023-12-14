package com.rubenmadsen.TjackServer.connection;

import com.google.gson.*;
import com.rubenmadsen.TjackServer.Packet.AChessPacket;
import com.rubenmadsen.TjackServer.Packet.ChessPacket;
import com.rubenmadsen.TjackServer.Packet.HostPacket;
import com.rubenmadsen.TjackServer.Packet.JoinedPacket;
import com.rubenmadsen.TjackServer.utility.CustomDeserializer;
import io.reactivex.rxjava3.core.Observable;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientConnection {
    private String handle;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;
    public Socket getSocket(){
        return this.socket;
    }
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.setupSocket();
    }
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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
        this.bos = new BufferedOutputStream(this.os);
        this.bis = new BufferedInputStream(this.is);
    }

    public Observable<ChessPacket> receive() {
        return Observable.create(emitter -> {
            System.out.println("Connected to server");
            while (!emitter.isDisposed() && !socket.isClosed()) {
                // Accept new client connections.
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    // Process the bytes
                    String jsonString = new String(buffer, 0, bytesRead);
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(ChessPacket.class, new CustomDeserializer())
                            .create();

                    Object packet = gson.fromJson(jsonString, ChessPacket.class);
                    System.out.println("Incoming data [" + bytesRead + "]:" + jsonString);
                    emitter.onNext((ChessPacket) packet);
                }
            }
            emitter.onError(new Throwable("Socket knas"));
        });
    }

    public <T extends AChessPacket> void send(T packet) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(packet);
        gson.serializeNulls();
        System.out.println("Outgoing data [" + jsonString.length() + "]:" + jsonString);
        this.os.write(jsonString.getBytes(StandardCharsets.UTF_8));
    }
}