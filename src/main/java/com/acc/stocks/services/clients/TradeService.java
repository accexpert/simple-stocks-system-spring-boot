package com.acc.stocks.services.clients;

import com.acc.stocks.calculations.TradeCalculationsHandler;
import com.acc.stocks.messaging.events.ApplicationCalculateStockEvent;
import com.acc.stocks.messaging.events.ApplicationRenderStockDataEvent;
import com.acc.stocks.messaging.events.ApplicationWriteOutputEvent;
import com.acc.stocks.models.StockModel;
import com.acc.stocks.models.TradeModel;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "tradeService")
public class TradeService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(TradeService.class);
    private static final Double minTradeValue = 0.0;
    private static final Double maxTradeValue = 100.0;
    private static final int minShareQuantityValue = 1;

    private IDataRepository tradeDataRepository;
    private IDataRepository stockDataRepository;
    private TradeCalculationsHandler tradeCalculationsHandler;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    public TradeService(IWriterHandler consoleWriterHandler, IDataRepository tradeDataRepository, IDataRepository stockDataRepository,
                        TradeCalculationsHandler tradeCalculationsHandler, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(consoleWriterHandler);
        this.tradeDataRepository = tradeDataRepository;
        this.stockDataRepository = stockDataRepository;
        this.tradeCalculationsHandler = tradeCalculationsHandler;
        try {
            generateDummyDataStockRepository(); //generate dummy date for POC purposes
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
        threadPoolTaskExecutor.execute(this);
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @EventListener
    public void applicationCalculateStockEventListener(ApplicationCalculateStockEvent applicationCalculateStockEvent) {
        try {
            StockSymbols stockSymbol = applicationCalculateStockEvent.getStockSymbol();
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
            publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
            publisher.publishEvent(new ApplicationWriteOutputEvent(header));
            publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
            publisher.publishEvent(new ApplicationWriteOutputEvent(results));
            publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    @EventListener
    public void applicationRenderStockEventListener(ApplicationRenderStockDataEvent applicationRenderStockDataEvent) {
        publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        publisher.publishEvent(new ApplicationWriteOutputEvent(StringUtils.formatLine("Stock symbol", 15)
                +StringUtils.formatLine("Stock type", 15)
                +StringUtils.formatLine("Last Dividend", 15)
                +StringUtils.formatLine("Fixed Dividend", 15)
                +StringUtils.formatLine("Par Value", 15)
                +StringUtils.formatLine("Share Quantity", 15)
                +StringUtils.formatLine("Created", 25)
                +StringUtils.formatLine("Last Updated", 25)));
        publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        StockSymbols stockSymbol = null!=applicationRenderStockDataEvent.getStockSymbol() ? applicationRenderStockDataEvent.getStockSymbol() : null;
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
                    publisher.publishEvent(new ApplicationWriteOutputEvent(StringUtils.formatLine(stockModel.getStockSymbol(), 15)
                            +StringUtils.formatLine(stockModel.getStockType().name(), 15)
                            +StringUtils.formatLine(String.valueOf(stockModel.getLastDividend()), 15)
                            +StringUtils.formatLine(String.valueOf(stockModel.getFixedDividend()), 15)
                            +StringUtils.formatLine(String.valueOf(stockModel.getParValue()), 15)
                            +StringUtils.formatLine(String.valueOf(stockModel.getShareQuantity()), 15)
                            +StringUtils.formatLine(DateUtils.getFormattedDateFromTimestampLong(stockModel.getCreated()), 25)
                            +StringUtils.formatLine(DateUtils.getFormattedDateFromTimestampLong(stockModel.getLastUpdated()), 25)));
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    publisher.publishEvent(new ApplicationWriteOutputEvent("Error reading Stock Symbol "+val));
                }

            });
            publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
            //handler error here. display user error
            return;
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
