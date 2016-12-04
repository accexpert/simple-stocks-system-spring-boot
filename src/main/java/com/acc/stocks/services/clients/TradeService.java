package com.acc.stocks.services.clients;

import com.acc.stocks.calculations.TradeCalculationsHandler;
import com.acc.stocks.events.IEventHandler;
import com.acc.stocks.events.IEventObserver;
import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.StockModel;
import com.acc.stocks.models.TradeModel;
import com.acc.stocks.models.enums.EventTypes;
import com.acc.stocks.models.enums.StockSymbols;
import com.acc.stocks.models.enums.StockTypes;
import com.acc.stocks.models.enums.TradeTypes;
import com.acc.stocks.repositories.IDataRepository;
import com.acc.stocks.utils.Constants;
import com.acc.stocks.utils.DateUtils;
import com.acc.stocks.utils.NumberUtils;
import com.acc.stocks.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "tradeService")
public class TradeService extends BaseService implements IEventObserver {
    private static final Logger LOGGER = Logger.getLogger(TradeService.class);
    private static final Double minTradeValue = 0.0;
    private static final Double maxTradeValue = 100.0;
    private static final int minShareQuantityValue = 1;

    private IDataRepository tradeDataRepository;
    private IDataRepository stockDataRepository;
    private TradeCalculationsHandler tradeCalculationsHandler;

    @Autowired
    public TradeService(IWriterHandler consoleWriterHandler, IEventHandler eventHandler, IDataRepository tradeDataRepository, IDataRepository stockDataRepository,
                        TradeCalculationsHandler tradeCalculationsHandler, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(consoleWriterHandler, eventHandler);
        this.tradeDataRepository = tradeDataRepository;
        this.stockDataRepository = stockDataRepository;
        this.tradeCalculationsHandler = tradeCalculationsHandler;
        eventHandler.register(EventTypes.EXIT, this);
        eventHandler.register(EventTypes.CALCULATE_STOCK, this);
        eventHandler.register(EventTypes.RENDER_STOCK_DATA, this);
        try {
            generateDummyDataStockRepository(); //generate dummy date for POC purposes
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        threadPoolTaskExecutor.execute(this);
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    /**
     * <p>
     *     Catch events for different operations.
     *     Rendering should be moved to a specialized service.
     * </p>
     * @param event
     */
    @Override
    public void notifyEvent(MessageEventModel event) {
        if(EventTypes.EXIT.equals(event.getEventType())) {
            setStop(true);
            return;
        }
        if(EventTypes.CALCULATE_STOCK.equals(event.getEventType())) {
            try {
                StockSymbols stockSymbol = (StockSymbols) event.getMessage();
                StockModel stockModel = (StockModel) stockDataRepository.getRecord(stockSymbol.name());
                TradeModel tradeModel = (TradeModel) tradeDataRepository.getLastRecord();
                Double dividendYield = tradeCalculationsHandler.calculateDividendYield(stockModel, tradeModel);
                Double peRation = tradeCalculationsHandler.calculatePERation(stockModel, tradeModel);
                Double geometricMean = tradeCalculationsHandler.calculateGeometricMean(tradeDataRepository.getRecords());
                Double stockPrice = tradeCalculationsHandler.calculateStockPrice(tradeDataRepository.getRecords(), (1000 * 60 * 15));
                String header = StringUtils.formatLine("Stock symbol", 15)
                        +StringUtils.formatLine("Dividend Yield", 20)
                        +StringUtils.formatLine("P/E ratio", 20)
                        +StringUtils.formatLine("Geometric Mean", 20)
                        +StringUtils.formatLine("Stock Price", 20);
                String results = StringUtils.formatLine(stockSymbol.name(), 15)
                        +StringUtils.formatLine(String.valueOf(dividendYield), 20)
                        +StringUtils.formatLine(String.valueOf(peRation), 20)
                        +StringUtils.formatLine(String.valueOf(geometricMean), 20)
                        +StringUtils.formatLine(String.valueOf(stockPrice), 20);
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, header));
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, results));
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        if(EventTypes.RENDER_STOCK_DATA.equals(event.getEventType())) {
            getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
            getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, StringUtils.formatLine("Stock symbol", 15)
                    +StringUtils.formatLine("Stock type", 15)
                    +StringUtils.formatLine("Last Dividend", 15)
                    +StringUtils.formatLine("Fixed Dividend", 15)
                    +StringUtils.formatLine("Par Value", 15)
                    +StringUtils.formatLine("Share Quantity", 15)
                    +StringUtils.formatLine("Created", 25)
                    +StringUtils.formatLine("Last Updated", 25)));
            getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
            StockSymbols stockSymbol = null!=event.getMessage() ? (StockSymbols) event.getMessage() : null;
            List<String> stockSymbolsList = new ArrayList<>();
            try {
                if(null != stockSymbol) {
                    stockSymbolsList.add(stockSymbol.name());
                } else {
                    stockSymbolsList.addAll(((List<StockModel>)stockDataRepository.getRecords()).parallelStream().map(val->val.getStockSymbol()).collect(Collectors.toList()));
                }
                stockSymbolsList.stream().forEach(val -> {
                    try {
                        StockModel stockModel = (StockModel) stockDataRepository.getRecord(val);
                        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, StringUtils.formatLine(stockModel.getStockSymbol(), 15)
                                +StringUtils.formatLine(stockModel.getStockType().name(), 15)
                                +StringUtils.formatLine(String.valueOf(stockModel.getLastDividend()), 15)
                                +StringUtils.formatLine(String.valueOf(stockModel.getFixedDividend()), 15)
                                +StringUtils.formatLine(String.valueOf(stockModel.getParValue()), 15)
                                +StringUtils.formatLine(String.valueOf(stockModel.getShareQuantity()), 15)
                                +StringUtils.formatLine(DateUtils.getFormattedDateFromTimestampLong(stockModel.getCreated()), 25)
                                +StringUtils.formatLine(DateUtils.getFormattedDateFromTimestampLong(stockModel.getLastUpdated()), 25)));
                    } catch (SQLException ex) {
                        LOGGER.error(ex.getMessage());
                        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Error reading Stock Symbol "+val));
                    }

                });
                getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
                //handler error here. display user error
                return;
            }
        }
    }

    @Override
    public void run() {
        while(!getStop()) {
            try {
                StockModel stockModel = (StockModel)stockDataRepository.getRecord(getRandomStockSymbolValue().name());
                tradeDataRepository.addRecord(new TradeModel(stockModel.getStockId(), generateShareQuantity(stockModel.getShareQuantity()), generateTradeTypeRandom(), generatePrice()));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage());
            }

        }
        LOGGER.info(this.getClass().getSimpleName()+" service closed.");
    }

    private Double generatePrice() {
        return NumberUtils.generateDouble(minTradeValue, maxTradeValue);
    }

    private Integer generateShareQuantity(Integer maxShareQuantityValue) {
        return NumberUtils.generateInteger(minShareQuantityValue, maxShareQuantityValue);
    }

    private TradeTypes generateTradeTypeRandom() {
        return NumberUtils.generateBoolean() ? TradeTypes.BUY : TradeTypes.SELL;
    }

    private StockSymbols getRandomStockSymbolValue() {
        return StockSymbols.values()[NumberUtils.generateInteger(0, StockSymbols.values().length-1)];
    }

    private void generateDummyDataStockRepository() throws SQLException {
        this.stockDataRepository.addRecord(new StockModel(StockSymbols.TEA.name(), StockTypes.COMMON, 0.0, 0.0, 100.0, 75));
        this.stockDataRepository.addRecord(new StockModel(StockSymbols.POP.name(), StockTypes.COMMON, 8.0, 0.0, 100.0, 80));
        this.stockDataRepository.addRecord(new StockModel(StockSymbols.ALE.name(), StockTypes.COMMON, 23.0, 0.0, 60.0, 70));
        this.stockDataRepository.addRecord(new StockModel(StockSymbols.GIN.name(), StockTypes.PREFERRED, 8.0, 2.0, 100.0, 85));
        this.stockDataRepository.addRecord(new StockModel(StockSymbols.JOE.name(), StockTypes.COMMON, 13.0, 0.0, 250.0, 95));
    }
}
