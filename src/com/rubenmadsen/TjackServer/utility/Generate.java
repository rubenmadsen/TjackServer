package com.rubenmadsen.TjackServer.utility;

import java.util.Random;

public class Generate {
    static public String generateId(int len){
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random r = new Random();


            int ran = r.nextInt(10000);
            sb.append(ran);

        return sb.toString();
    }
}
