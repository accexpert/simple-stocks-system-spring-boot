package com.acc.stocks.models;

import com.acc.stocks.models.enums.StockTypes;
import com.acc.stocks.utils.DateUtils;

import java.sql.Timestamp;

public class StockModel {
    /**
     * <p>
     *     stockId is the primary key from the database.
     * </p>
     */
    private Integer stockId;
    private String stockSymbol;
    private StockTypes stockType;
    private Double lastDividend;
    private Double fixedDividend;
    private Double parValue;
    private Timestamp created;
    private Timestamp lastUpdated;
    private Integer shareQuantity;

    public StockModel() {
        //init initial values with default values.
        this.stockSymbol = "";
        this.stockType = StockTypes.UNKNOWN;
        this.lastDividend = 0.0;
        this.fixedDividend = 0.0;
        this.parValue = 0.0;
        this.shareQuantity = 0;
        this.created = DateUtils.getCurrentTimestamp();
        this.lastUpdated = DateUtils.getCurrentTimestamp();
    }

    public StockModel(String stockSymbol, StockTypes stockType, Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity) {
        this.stockSymbol = stockSymbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.parValue = parValue;
        this.created = DateUtils.getCurrentTimestamp();
        this.lastUpdated = DateUtils.getCurrentTimestamp();
        this.shareQuantity = shareQuantity;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public StockTypes getStockType() {
        return stockType;
    }

    public void setStockType(StockTypes stockType) {
        this.stockType = stockType;
    }

    public Double getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(Double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public Double getFixedDividend() {
        return fixedDividend;
    }

    public void setFixedDividend(Double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public Double getParValue() {
        return parValue;
    }

    public void setParValue(Double parValue) {
        this.parValue = parValue;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getShareQuantity() {
        return shareQuantity;
    }

    public void setShareQuantity(Integer shareQuantity) {
        this.shareQuantity = shareQuantity;
    }

    @Override
    public String toString() {
        return "StockModel{" +
                "stockId=" + stockId +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", stockType=" + stockType +
                ", lastDividend=" + lastDividend +
                ", fixedDividend=" + fixedDividend +
                ", parValue=" + parValue +
                ", created=" + created +
                ", lastUpdated=" + lastUpdated +
                ", shareQuantity=" + shareQuantity +
                '}';
    }
}
