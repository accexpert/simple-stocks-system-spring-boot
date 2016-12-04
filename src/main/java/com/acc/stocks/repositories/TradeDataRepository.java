package com.acc.stocks.repositories;

import com.acc.stocks.models.TradeModel;
import com.acc.stocks.utils.Constants;
import com.acc.stocks.utils.DatabaseFakePkUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TradeDataRepository implements IDataRepository<TradeModel> {
    private static final Logger LOGGER = Logger.getLogger(TradeDataRepository.class);
    /**
     * <p>
     *     List to hold the trade data.
     * </p>
     */
    private List<TradeModel> tradeData;

    public TradeDataRepository() {
        this.tradeData = new ArrayList<>();
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @Override
    public void addRecord(TradeModel trade) throws SQLException {
        //generate fake unique database pk
        trade.setTradeId(DatabaseFakePkUtils.getTradeId());
        //add trade to map
        this.tradeData.add(trade);
    }

    @Override
    public TradeModel getRecord(Integer tradeId) throws SQLException {
        return this.tradeData.parallelStream().filter(val -> val.getTradeId().equals(tradeId)).findFirst().orElse(null);
    }

    @Override
    public void updateRecord(TradeModel stock) throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public TradeModel getRecord(String type) throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public void removeRecord(TradeModel stock) throws SQLException {
        throw new NotImplementedException(Constants.ERROR_MSG_METHOD_NOT_IMPLEMENTED);
    }

    @Override
    public TradeModel getLastRecord() throws SQLException {
        return this.tradeData.get(this.tradeData.size()-1);
    }

    @Override
    public List<TradeModel> getRecords() throws SQLException {
        return this.tradeData;
    }
}
