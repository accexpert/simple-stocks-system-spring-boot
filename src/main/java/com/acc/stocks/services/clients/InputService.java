package com.acc.stocks.services.clients;

import com.acc.stocks.events.IEventHandler;
import com.acc.stocks.events.IEventObserver;
import com.acc.stocks.messaging.events.*;
import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.enums.EventTypes;
import com.acc.stocks.models.enums.StockSymbols;
import com.acc.stocks.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Scanner;

@Service(value = "inputService")
public class InputService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(InputService.class);
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    public InputService(IWriterHandler consoleWriterService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(consoleWriterService);
        LOGGER.info(this.getClass().getSimpleName()+" created.");
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.threadPoolTaskExecutor.execute(this);
    }

    @Override
    public void run() {
        Scanner scanner = null;
        String inputLine;
        try {
            scanner = new Scanner(System.in);
            while (!getStop()) {
                showAvailableOptions();
                inputLine = scanner.nextLine();
                String[] inputValues = inputLine.split(" ");
                switch (InputOptions.getOption(inputValues[0].trim())) {
                    case QUIT:
                        publisher.publishEvent(new ApplicationExitEvent());
                        this.threadPoolTaskExecutor.shutdown();
                        break;
                    case CALCULATE:

                        if(inputValues.length<2) {
                            publisher.publishEvent(new ApplicationWriteOutputEvent("Error. Stock symbol not entered"));
                            break;
                        }
                        if(StockSymbols.getSymbol(inputValues[1].trim()) == null) {
                            publisher.publishEvent(new ApplicationWriteOutputEvent("Error. Unknown stock symbol: "+inputValues[1]));
                            break;
                        }
                        publisher.publishEvent(new ApplicationCalculateStockEvent(StockSymbols.getSymbol(inputValues[1].trim())));
                        break;
                    case RENDER_STOCKS:
                        if(inputValues.length < 2) {
                            publisher.publishEvent(new ApplicationRenderStockDataEvent(null));
                            break;
                        }
                        publisher.publishEvent(new ApplicationRenderStockDataEvent(StockSymbols.getSymbol(inputValues[1].trim())));
                        break;
                    default:
                        publisher.publishEvent(new ApplicationWriteOutputEvent("Unknown option. Please try again"));
                        break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        } finally {
            if(null!=scanner) {
                scanner.close();
            }
        }
        LOGGER.info(this.getClass().getSimpleName()+" service closed.");
    }

    @EventListener
    public void applicationRenderUserOptionEventListener(ApplicationRenderUserOptionsEvent applicationRenderUserOptionsEvent) {
        showAvailableOptions();
    }

    public void showAvailableOptions() {
        publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        publisher.publishEvent(new ApplicationWriteOutputEvent("Available options"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("Quit application: q"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("Calculate data for stock: c"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("      Example: c TEA"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("Display stocks: s"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("      Example: s -> show all stocks records"));
        publisher.publishEvent(new ApplicationWriteOutputEvent("      Example: s TEA -> show TEA record"));
        publisher.publishEvent(new ApplicationWriteOutputEvent(Constants.LINE_SEPARATOR));
        publisher.publishEvent(new ApplicationWriteOutputEvent("Select option: "));
    }

    public enum InputOptions {
        UNKNOWN("u"),
        CALCULATE("c"),
        RENDER_STOCKS("s"),
        QUIT("q");

        private String option;

        InputOptions(String option) {
            this.option = option;
        }

        public static InputOptions getOption(String value) {
            return EnumSet.allOf(InputOptions.class).stream().filter(val -> val.option.equals(value)).findFirst().orElse(InputOptions.UNKNOWN);
        }
    }
}
