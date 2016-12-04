package com.acc.stocks;

import com.acc.stocks.utils.StringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StringUtilsTest {

    @DataProvider(name = "fixedLengthData")
    public Object[][] getFixedLengthData() {
        return new Object[][] {
                {"Test1", 10},          //in between
                {"", 10},               //empty
                {"12345678901211", 10},  //over max length
                {null, 10}
        };
    }
    @Test(dataProvider = "fixedLengthData")
    private void testFixedLength(String str, int length) {
        Assert.assertEquals(StringUtils.formatLine(str, length).length(), length);
    }


}
