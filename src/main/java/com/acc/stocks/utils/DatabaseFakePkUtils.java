package com.acc.stocks.utils;

public class DatabaseFakePkUtils {

    private DatabaseFakePkUtils() {}

    private static Integer tradeId = 1;
    private static Integer stockId = 1;

    public static Integer getTradeId() {
        return tradeId++;
    }

    public static Integer getStockId() {
        return stockId++;
    }
}
