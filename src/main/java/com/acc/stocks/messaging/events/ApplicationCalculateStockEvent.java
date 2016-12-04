package com.acc.stocks.messaging.events;

import com.acc.stocks.models.enums.StockSymbols;

public class ApplicationCalculateStockEvent extends ApplicationEventBase {
    private StockSymbols stockSymbol;

    public ApplicationCalculateStockEvent(StockSymbols stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public StockSymbols getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(StockSymbols stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    @Override
    public String toString() {
        return "ApplicationCalculateStockEvent{" +
                "stockSymbol=" + stockSymbol +
                '}';
    }
}
