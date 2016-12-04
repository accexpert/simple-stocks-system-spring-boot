package com.acc.stocks;

import com.acc.stocks.calculations.TradeCalculationsHandler;
import com.acc.stocks.models.StockModel;
import com.acc.stocks.models.TradeModel;
import com.acc.stocks.models.enums.StockSymbols;
import com.acc.stocks.models.enums.StockTypes;
import com.acc.stocks.models.enums.TradeTypes;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TradeCalculationsHandlerTest {
    private TradeCalculationsHandler tradeCalculationsHandler;

    @BeforeMethod
    public void setup() {
        this.tradeCalculationsHandler = new TradeCalculationsHandler();
    }

    @DataProvider(name = "yieldValuesCommon")
    public Object[][] createYieldCommonValues() {
        return new Object[][]{
                {0.0, 0.0, 100.0, 100, 0.0},
                {10.0, 0.0, 100.0, 100, 1.0},
                {5.0, 0.0, 100.0, 100, 0.5},
                {20.0, 0.0, 100.0, 100, 2.0}
        };
    }

    @DataProvider(name = "yieldValuesPreferred")
    public Object[][] createYieldPreferredValues() {
        return new Object[][]{
                {0.0, 0.0, 0.0, 100, 0.0},
                {0.0, 0.0, 100.0, 100, 0.0},
                {10.0, 5.0, 0.0, 100, 0.0},
                {5.0, 10.0, 100.0, 100, 100.0},
                {20.0, 15.0, 100.0, 100, 150.0},
                {20.0, 20.0, 100.0, 100, 200.0}
        };
    }

    @DataProvider(name = "peRatioValues")
    public Object[][] createPERatioValues() {
        return new Object[][]{
                {0.0, 0.0, 0.0, 100, 0.0},
                {10.0, 5.0, 0.0, 100, 1.0},
                {5.0, 10.0, 100.0, 100, 2.0},
                {20.0, 15.0, 100.0, 100, 0.5}
        };
    }

    @Test(dataProvider = "yieldValuesCommon")
    public void testDividendYieldCommonType(Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity, Double expectedResult) {
        Double value = this.tradeCalculationsHandler.calculateDividendYield(generateOneKnownDataStockCommonRecord(lastDividend, fixedDividend, parValue, shareQuantity), generateOneKnownDataTradeRecord());
        Assert.assertEquals(expectedResult, value);
    }

    @Test(dataProvider = "yieldValuesPreferred")
    public void testDividendYieldPreferredType(Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity, Double expectedResult) {
        Double value = this.tradeCalculationsHandler.calculateDividendYield(generateOneKnownDataStockPreferredRecord(lastDividend, fixedDividend, parValue, shareQuantity), generateOneKnownDataTradeRecord());
        Assert.assertEquals(expectedResult, value);
    }

    @Test(dataProvider = "peRatioValues")
    public void testPERation(Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity, Double expectedResult) {
        Double value = this.tradeCalculationsHandler.calculatePERation(generateOneKnownDataStockPreferredRecord(lastDividend, fixedDividend, parValue, shareQuantity), generateOneKnownDataTradeRecord());
        Assert.assertEquals(expectedResult, value);
    }

    @Test
    public void testStockPrice() {
        Double value = this.tradeCalculationsHandler.calculateStockPrice(generateFiveKnownDataTradeRecords(), (1000 * 60 * 15));
        Assert.assertEquals(22.22222222222222, value);
    }

    @Test
    public void testGeometricMean() {
        Double value = this.tradeCalculationsHandler.calculateGeometricMean(generateFiveKnownDataTradeRecords());
        Assert.assertEquals(2.51188643150958, value);
        System.out.println(value);
    }

    private List<TradeModel> generateFiveKnownDataTradeRecords() {
        List<TradeModel> tradeModelList = new ArrayList<>();
        tradeModelList.add(new TradeModel( 1, 10, TradeTypes.BUY, 10.0 ));
        tradeModelList.add(new TradeModel( 2, 15, TradeTypes.BUY, 15.0 ));
        tradeModelList.add(new TradeModel( 3, 20, TradeTypes.BUY, 20.0 ));
        tradeModelList.add(new TradeModel( 4, 15, TradeTypes.BUY, 25.0 ));
        tradeModelList.add(new TradeModel( 5, 30, TradeTypes.BUY, 30.0 ));
        return tradeModelList;
    }

    private TradeModel generateOneKnownDataTradeRecord() {
        return new TradeModel( 1, 10, TradeTypes.BUY, 10.0 );
    }

    private StockModel generateOneKnownDataStockCommonRecord( Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity) {
        return new StockModel( StockSymbols.TEA.name(), StockTypes.COMMON, lastDividend, fixedDividend, parValue, shareQuantity );
    }

    private StockModel generateOneKnownDataStockPreferredRecord( Double lastDividend, Double fixedDividend, Double parValue, Integer shareQuantity) {
        return new StockModel( StockSymbols.TEA.name(), StockTypes.PREFERRED, lastDividend, fixedDividend, parValue, shareQuantity );
    }

    @AfterMethod
    public void destroy() {
        this.tradeCalculationsHandler = null;
    }
}
