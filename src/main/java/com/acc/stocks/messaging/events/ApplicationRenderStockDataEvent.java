package com.acc.stocks.messaging.events;

import com.acc.stocks.models.enums.StockSymbols;

public class ApplicationRenderStockDataEvent extends ApplicationEventBase {
    private StockSymbols stockSymbol;

    public ApplicationRenderStockDataEvent(StockSymbols stockSymbol) {
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
        return "ApplicationRenderStockDataEvent{" +
                "stockSymbol=" + stockSymbol +
                '}';
    }
}
