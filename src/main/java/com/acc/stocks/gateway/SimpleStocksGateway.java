package com.acc.stocks.gateway;

import com.acc.stocks.config.ApplicationConfig;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SimpleStocksGateway {
    private static final Logger LOGGER = Logger.getLogger(SimpleStocksGateway.class);


    public SimpleStocksGateway() {
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    public void start() {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            context.registerShutdownHook();
            context.start();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        LOGGER.info("Simple stock app started");
    }
}
