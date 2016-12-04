package com.acc.stocks.services.clients;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConsoleWriterService implements IWriterHandler {
    private static final Logger LOGGER = Logger.getLogger(ConsoleWriterService.class);

    public ConsoleWriterService() {
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @Override
    public void write(String line) {
        System.out.println(line);
    }

    @Override
    public void init(String output) {

    }

    @Override
    public void destroy() {

    }
}
