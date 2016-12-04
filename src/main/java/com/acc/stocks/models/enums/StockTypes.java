package com.acc.stocks.models.enums;

import java.util.EnumSet;

public enum StockTypes {
    UNKNOWN(0),
    COMMON(1),
    PREFERRED(2);

    private Integer stockTypeId;

    StockTypes(Integer stockTypeId) {
        this.stockTypeId = stockTypeId;
    }

    public static StockTypes getStockName(Integer stockTypeId) {
        return EnumSet.allOf(StockTypes.class).stream().filter(val -> val.stockTypeId.equals(stockTypeId)).findFirst().orElse(StockTypes.UNKNOWN);
    }
}
