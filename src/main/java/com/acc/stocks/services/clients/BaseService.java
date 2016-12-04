package com.acc.stocks.services.clients;

import com.acc.stocks.messaging.events.ApplicationExitEvent;
import org.apache.log4j.Logger;
import org.springframework.context.event.EventListener;

public abstract class BaseService implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(BaseService.class);
    private IWriterHandler writerHandler;
    private Boolean stop;

    public BaseService(IWriterHandler writerHandler) {
        this.writerHandler = writerHandler;
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

    @EventListener
    public void applicationExitEventListener(ApplicationExitEvent applicationExitEvent) {
        LOGGER.info("Exit event received in service "+this.getClass().getSimpleName());
        this.stop = true;
    }
}
