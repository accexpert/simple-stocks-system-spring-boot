package com.acc.stocks.services.clients;

public interface IWriterHandler {
    void init(String output);
    void write(String line);
    void destroy();

}
