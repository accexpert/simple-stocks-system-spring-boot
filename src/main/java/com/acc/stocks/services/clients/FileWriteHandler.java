package com.acc.stocks.services.clients;

import org.apache.log4j.Logger;

public class FileWriteHandler implements IWriterHandler {
    private static final Logger LOGGER = Logger.getLogger(FileWriteHandler.class);

    public FileWriteHandler() {
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
