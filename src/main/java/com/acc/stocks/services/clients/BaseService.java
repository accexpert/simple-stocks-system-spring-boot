package com.acc.stocks.services.clients;

import com.acc.stocks.events.IEventHandler;

public abstract class BaseService implements Runnable {
    private IWriterHandler writerHandler;
    private IEventHandler eventHandler;
    private Boolean stop;

    public BaseService(IWriterHandler writerHandler, IEventHandler eventHandler) {
        this.writerHandler = writerHandler;
        this.eventHandler = eventHandler;
        this.stop = false;
    }

    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }

    public IWriterHandler getWriterHandler() {
        return writerHandler;
    }

    public IEventHandler getEventHandler() {
        return eventHandler;
    }
}
