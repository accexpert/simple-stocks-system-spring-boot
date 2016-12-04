package com.acc.stocks.utils;

import java.util.Random;

public class NumberUtils {
    private static Random random = new Random();

    public static Integer generateInteger(Integer minValue, Integer maxValue) {
        return minValue + random.nextInt((maxValue-minValue));
    }

    public static Double generateDouble(Double minValue, Double maxValue) {
        return minValue + (maxValue-minValue) * random.nextDouble();
    }

    public static Boolean generateBoolean() {
        return random.nextBoolean();
    }

}
