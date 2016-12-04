package com.acc.stocks.gateway;

import com.acc.stocks.config.ApplicationConfig;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;

public class SimpleStocksGateway {
    private static final Logger LOGGER = Logger.getLogger(SimpleStocksGateway.class);


    public SimpleStocksGateway() {
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    public void start() {
        SpringApplication.run(ApplicationConfig.class);
        LOGGER.info("Simple stock app started");
    }
}
