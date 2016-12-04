package com.acc.stocks.services.clients;

import org.apache.commons.lang3.StringUtils;

public interface IWriterHandler {
    void init(String output);
    void write(String line);
    void destroy();

}
