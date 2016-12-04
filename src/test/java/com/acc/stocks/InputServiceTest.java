package com.acc.stocks;

import com.acc.stocks.services.clients.InputService;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InputServiceTest {

    @Test
    public void testInputOptionKnownValue() {
        String str = "q";
        Assert.assertEquals(InputService.InputOptions.QUIT, InputService.InputOptions.getOption(str));
    }

    @Test
    public void testInputOptionUnknownValue() {
        String str = "";
        Assert.assertEquals(InputService.InputOptions.UNKNOWN, InputService.InputOptions.getOption(str));
    }
}
