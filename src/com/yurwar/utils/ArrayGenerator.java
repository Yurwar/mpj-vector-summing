package com.yurwar.utils;

import java.util.Random;

public class ArrayGenerator {
    private final Random random = new Random();

    public int[] getIntArray(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(50);
        }

        return arr;
    }
}