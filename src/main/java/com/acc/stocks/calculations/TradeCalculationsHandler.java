package com.acc.stocks.calculations;

import com.acc.stocks.models.StockModel;
import com.acc.stocks.models.TradeModel;
import com.acc.stocks.models.enums.StockTypes;
import com.acc.stocks.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

@Component
public class TradeCalculationsHandler {

    /**
     * <p>
     *     Calculate the dividend yield
     * </p>
     * @param stock
     * @param lastTrade
     * @return
     */
    public Double calculateDividendYield(StockModel stock, TradeModel lastTrade) {
        return StockTypes.COMMON.equals(stock.getStockType())
                ? stock.getLastDividend()/lastTrade.getPrice()
                : (stock.getFixedDividend()*stock.getParValue())/lastTrade.getPrice();
    }

    /**
     * <p>
     *     Calculate the P/E Ratio
     * </p>
     * @param stock
     * @param lastTrade
     * @return
     */
    public Double calculatePERation(StockModel stock, TradeModel lastTrade) {
        return stock.getLastDividend()==0.0 ? 0.0 : lastTrade.getPrice()/stock.getLastDividend();
    }

    /**
     * <p>
     *     Calculate the stock prices in the last 15 minutes
     *     This should be done using a stored procedure.
     *     Additional trades addition should be considered when calculation takes place in order
     *     to avoid incorrect results.
     * </p>
     * @param tradeModelList
     * @return
     */
    public Double calculateStockPrice(List<TradeModel> tradeModelList, long timeWindow) {
        DoubleAdder tradeValue = new DoubleAdder();
        LongAdder quantityValue = new LongAdder();
        Timestamp last15minTimestamp = new Timestamp(DateUtils.getCurrentTimestamp().getTime() - timeWindow);   //last n milliseconds
        tradeModelList.parallelStream().filter(val -> val.getTimestamp().after(last15minTimestamp)).forEach(val->
                {
                    quantityValue.add(val.getQuantityOfShares());
                    tradeValue.add(val.getPrice()*val.getQuantityOfShares());
                }
        );
        return tradeValue.doubleValue()/quantityValue.doubleValue();
    }

    /**
     * <p>
     *     Calculate the GBCE All Share index using the geometric mean of prices for all stocks
     *     This should be done using a stored procedure.
     *     Additional trades addition should be considered when calculation takes place in order
     *     to avoid incorrect results.
     * </p>
     * @param tradeModelList
     * @return
     */
    public Double calculateGeometricMean(List<TradeModel> tradeModelList) {
        DoubleAdder allSharePrices = new DoubleAdder();
        tradeModelList.parallelStream().forEach(val->allSharePrices.add(val.getPrice()));
        return Math.pow(allSharePrices.doubleValue(), 1.0/tradeModelList.size());
    }
}
