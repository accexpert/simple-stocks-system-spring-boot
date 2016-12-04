package com.acc.stocks.models.enums;

import java.util.EnumSet;

public enum StockSymbols {
    TEA,
    POP,
    ALE,
    GIN,
    JOE;

    public static StockSymbols getSymbol(String value) {
        return EnumSet.allOf(StockSymbols.class).stream().filter(val -> val.name().equals(value)).findFirst().orElse(null);
    }
}
