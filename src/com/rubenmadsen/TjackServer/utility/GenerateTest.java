package com.rubenmadsen.TjackServer.utility;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateTest {

    @Test
    public void testGeneration(){
        String res = Generate.generateId(8);
        System.out.println(res);
    }

}