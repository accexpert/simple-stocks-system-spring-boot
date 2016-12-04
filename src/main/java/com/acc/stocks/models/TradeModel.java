package com.acc.stocks.models;

import com.acc.stocks.models.enums.TradeTypes;
import com.acc.stocks.utils.DateUtils;

import java.sql.Timestamp;

public class TradeModel {
    /**
     * tradeId is the primary key from the database
     */
    private Integer tradeId;
    /**
     * stockId is the foreignKey for stock in database
     */
    private Integer stockId;
    private Timestamp timestamp;
    private Integer quantityOfShares;
    private TradeTypes tradeType;
    private Double price;

    public TradeModel() {
    }

    public TradeModel(Integer stockId, Integer quantityOfShares, TradeTypes tradeType, Double price) {
        this.stockId = stockId;
        //transaction timestamp. this should be generated on database to prevent differences between different servers in cloud
        this.timestamp = DateUtils.getCurrentTimestamp();
        this.quantityOfShares = quantityOfShares;
        this.tradeType = tradeType;
        this.price = price;
    }

    public TradeModel(Integer stockId, Integer quantityOfShares, TradeTypes tradeType, Double price, Timestamp createdTimestamp) {
        this.stockId = stockId;
        //transaction timestamp. this should be generated on database to prevent differences between different servers in cloud
        this.timestamp = createdTimestamp;
        this.quantityOfShares = quantityOfShares;
        this.tradeType = tradeType;
        this.price = price;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getQuantityOfShares() {
        return quantityOfShares;
    }

    public void setQuantityOfShares(Integer quantityOfShares) {
        this.quantityOfShares = quantityOfShares;
    }

    public TradeTypes getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeTypes tradeType) {
        this.tradeType = tradeType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TradeModel{" +
                "tradeId=" + tradeId +
                ", stockId=" + stockId +
                ", timestamp=" + timestamp +
                ", quantityOfShares=" + quantityOfShares +
                ", tradeType=" + tradeType +
                ", price=" + price +
                '}';
    }
}
