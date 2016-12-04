package com.acc.stocks.services.clients;

import com.acc.stocks.events.IEventHandler;
import com.acc.stocks.events.IEventObserver;
import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.enums.EventTypes;
import com.acc.stocks.models.enums.StockSymbols;
import com.acc.stocks.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Scanner;

@Service(value = "inputService")
public class InputService extends BaseService implements IEventObserver {
    private static final Logger LOGGER = Logger.getLogger(InputService.class);

    @Autowired
    public InputService(IWriterHandler consoleWriterService, IEventHandler eventHandler, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(consoleWriterService, eventHandler);
        eventHandler.register(EventTypes.RENDER_USER_OPTIONS, this);
        LOGGER.info(this.getClass().getSimpleName()+" created.");
        threadPoolTaskExecutor.execute(this);
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
                        getEventHandler().publish(new MessageEventModel(EventTypes.EXIT, null));
                        setStop(true);
                        break;
                    case CALCULATE:
                        if(inputValues.length<2) {
                            getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Error. Stock symbol not entered"));
                            break;
                        }
                        if(StockSymbols.getSymbol(inputValues[1].trim()) == null) {
                            getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Error. Unknown stock symbol: "+inputValues[1]));
                            break;
                        }
                        getEventHandler().publish(new MessageEventModel(EventTypes.CALCULATE_STOCK, StockSymbols.getSymbol(inputValues[1].trim())));
                        break;
                    case RENDER_STOCKS:
                        if(inputValues.length < 2) {
                            getEventHandler().publish(new MessageEventModel(EventTypes.RENDER_STOCK_DATA, null));
                            break;
                        }
                        getEventHandler().publish(new MessageEventModel(EventTypes.RENDER_STOCK_DATA, StockSymbols.getSymbol(inputValues[1].trim())));
                        break;
                    default:
                        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Unknown option. Please try again"));
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

    @Override
    public void notifyEvent(MessageEventModel event) {
        if(EventTypes.EXIT.equals(event.getEventType())) {
            setStop(true);
            return;
        }
        if(EventTypes.RENDER_USER_OPTIONS.equals(event.getEventType())) {
            showAvailableOptions();
            return;
        }
    }

    public void showAvailableOptions() {
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Available options"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Quit application: q"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Calculate data for stock: c"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "      Example: c TEA"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Display stocks: s"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "      Example: s -> show all stocks records"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "      Example: s TEA -> show TEA record"));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, Constants.LINE_SEPARATOR));
        getEventHandler().publish(new MessageEventModel(EventTypes.WRITE_OUTPUT, "Select option: "));
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
