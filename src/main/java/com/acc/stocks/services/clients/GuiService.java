package com.acc.stocks.services.clients;

import com.acc.stocks.events.IEventHandler;
import com.acc.stocks.events.IEventObserver;
import com.acc.stocks.messaging.events.ApplicationWriteOutputEvent;
import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.enums.EventTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "guiService")
public class GuiService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(GuiService.class);
    /**
     * <p>
     *
     * </p>
     */
    private List<String> guiListContent;

    @Autowired
    public GuiService(IWriterHandler consoleWriterService, IEventHandler eventHandler, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(consoleWriterService, eventHandler);
        this.guiListContent = new ArrayList<>();
        threadPoolTaskExecutor.execute(this);
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @Override
    public void run() {
        while(!getStop()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
        }
        LOGGER.info(this.getClass().getSimpleName()+" service closed.");
    }

    @EventListener
    public void applicationWriteOutputEventListener(ApplicationWriteOutputEvent applicationWriteOutputEvent) {
        getWriterHandler().write(applicationWriteOutputEvent.getMessage());
    }
}
