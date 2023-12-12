package com.rubenmadsen.TjackServer.utility;

import java.util.Random;

public class Generate {
    static public String generateId(int len){
        String id = "";
        Random r = new Random();

        for (int i = 0; i < len; i++) {
            id += "" + r.nextInt(len);
        }
        return id;
    }
}
