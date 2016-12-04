package com.acc.stocks.repositories;

import com.acc.stocks.models.StockModel;
import com.acc.stocks.utils.Constants;
import com.acc.stocks.utils.DatabaseFakePkUtils;
import com.acc.stocks.utils.DateUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockDataRepository implements IDataRepository<StockModel> {
    private static final Logger LOGGER = Logger.getLogger(StockDataRepository.class);
    /**
     * <p>
     *     Map to hold the stock data.
     *     Key is stock symbol (e.g. TEA, POP, ALE etc.) and value is the stock data record.
     * </p>
     */
    private List<StockModel> stockData;

    public StockDataRepository() {
        this.stockData = new ArrayList<>();
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @Override
    public void addRecord(StockModel stock) throws SQLException {
        //generate fake unique database pk
        stock.setStockId(DatabaseFakePkUtils.getStockId());
        this.stockData.add(stock);
    }

    @Override
    public StockModel getRecord(Integer symbol) throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public StockModel getRecord(String type) throws SQLException {
        return this.stockData.parallelStream().filter(val -> val.getStockSymbol().equals(type)).findFirst().orElse(null);
    }

    /**
     * <p>
     *     Update a stock record.
     *     Important: before any update check the lastUpdated timestamp in order to make sure that
     *     the current record is not overriden by an older record in a concurrent distributed system
     * </p>
     * @param stock
     * @throws SQLException
     */
    @Override
    public void updateRecord(StockModel stock) throws SQLException {
        stock.setLastUpdated(DateUtils.getCurrentTimestamp());
        this.stockData.parallelStream().filter(val -> val.getStockId().equals(stock.getStockId())).findAny().ifPresent(val -> val=stock);
    }

    @Override
    public void removeRecord(StockModel stock) throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public StockModel getLastRecord() throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public List<StockModel> getRecords() throws SQLException {
        return this.stockData;
    }
}
