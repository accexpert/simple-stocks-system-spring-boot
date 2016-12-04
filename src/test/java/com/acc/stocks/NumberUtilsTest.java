package com.acc.stocks;

import com.acc.stocks.utils.NumberUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NumberUtilsTest {

    @DataProvider(name = "dataInt")
    public Object[][] getDataInt() {
        return new Object[][] {
                {0, 10},
                {5, 10},
                {0, 10},
                {0, 10},
                {0, 10},
                {0, 10}
        };
    }

    @DataProvider(name = "dataDouble")
    public Object[][] getDataDouble() {
        return new Object[][] {
                {0.0, 10.0},
                {0.0, 20.0},
                {0.0, 15.0},
                {0.0, 10.0},
                {0.0, 8.0},
                {0.0, 100.0},
                {0.0, 10.0},
                {0.0, 20.0},
                {0.0, 15.0},
                {0.0, 10.0},
                {0.0, 8.0},
                {0.0, 100.0}
        };
    }

    @Test(dataProvider = "dataInt")
    public void testIntegerGenerator(Integer minValue, Integer maxValue) {
        Integer val = NumberUtils.generateInteger(minValue, maxValue);
        Assert.assertTrue(val<=maxValue);
        Assert.assertTrue(val>=minValue);
    }

    @Test(dataProvider = "dataDouble")
    public void testDoubleGenerator(Double minValue, Double maxValue) {
        Double val = NumberUtils.generateDouble(minValue, maxValue);
        Assert.assertTrue(val<=maxValue);
        Assert.assertTrue(val>=minValue);
    }

}
