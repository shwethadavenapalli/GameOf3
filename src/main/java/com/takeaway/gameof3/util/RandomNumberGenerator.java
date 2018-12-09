package com.takeaway.gameof3.util;

import java.util.Random;

/**
 * Created by Shwetha on 09-12-2018.
 */
public class RandomNumberGenerator {

    public static int getRandomNumber(){
        Random random = new Random();
        int min = 3;
        int max = Integer.MAX_VALUE;
        return random.nextInt((max - min) + 1) + min;
    }
}
