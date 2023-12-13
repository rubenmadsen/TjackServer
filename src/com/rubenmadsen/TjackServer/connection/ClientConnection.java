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


    synchronized static public <T extends AChessPacket> Observable<T> receive(Socket client, Class<T> packetClass) throws IOException {
        //OutputStream os = client.getOutputStream();
        //InputStream is = client.getInputStream();
        return Observable.create(emitter -> {
            BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
                //System.out.println("Connected to server");
                while (!emitter.isDisposed() && !client.isClosed()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        // Process the bytes
                        String jsonString = new String(buffer, 0, bytesRead);
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(AChessPacket.class, new CustomDeserializer())
                                .create();

                        Object packet = gson.fromJson(jsonString, packetClass);

                        //System.out.println("Bytes read:" + bytesRead);
                        //System.out.println("Client Connection str:" + jsonString);
                        //ChessPacket packet = ChessPacket.decodeJson(jsonString);
                        System.out.println("Incoming data [" + bytesRead + "]:" + jsonString);

                        emitter.onNext((T)packet);
                    }
                }
                if(emitter.isDisposed()){
                    client.close();
                }
            emitter.onError(new Throwable("Socket knas"));
        });
    }

    static public <T extends AChessPacket> void send(Socket client, T packet) throws IOException {
        // Encode Json
        Gson gson = new Gson();

        String data = gson.toJson(packet);
        gson.serializeNulls();
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        System.out.println("Outgoing data [" + bytes.length + "]:" + data);
        //System.out.println("bytes: [" + Arrays.toString(bytes) + "]");
        client.getOutputStream().write(bytes);
        client.getOutputStream().flush();
    }
}