package com.acc.stocks;

import com.acc.stocks.events.EventHandler;
import com.acc.stocks.events.IEventObserver;
import com.acc.stocks.models.enums.EventTypes;
import com.acc.stocks.services.clients.ConsoleWriterService;
import com.acc.stocks.services.clients.IWriterHandler;
import com.acc.stocks.services.clients.InputService;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EventHandlerTest {
    private IWriterHandler writerHandler;
    @BeforeMethod
    public void setup() {
        this.writerHandler = Mockito.mock(ConsoleWriterService.class);
    }

    /**
     * Test if the same class is not registered twice
     */
    @Test
    public void eventAddTest() {
        EventHandler eventHandler = new EventHandler();
        IEventObserver eventObserver = new InputService(this.writerHandler, eventHandler);
        eventHandler.register(EventTypes.EXIT, eventObserver);
        eventHandler.register(EventTypes.EXIT, eventObserver);
        Assert.assertEquals(1, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
    }

    /**
     * Test if the observer is removed successfully
     */
    @Test
    public void eventRemoveTest() {
        EventHandler eventHandler = new EventHandler();
        IEventObserver eventObserver = new InputService(this.writerHandler, eventHandler);
        eventHandler.register(EventTypes.EXIT, eventObserver);
        Assert.assertEquals(1, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
        eventHandler.unregister(EventTypes.EXIT, eventObserver);
        Assert.assertEquals(0, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
    }

    /**
     * Test if two observers are added and removed successfully
     */
    @Test
    public void eventAddTwoObserversTest() {
        EventHandler eventHandler = new EventHandler();
        IEventObserver eventObserver1 = new InputService(this.writerHandler, eventHandler);
        IEventObserver eventObserver2 = new InputService(this.writerHandler, eventHandler);
        eventHandler.register(EventTypes.EXIT, eventObserver1);
        eventHandler.register(EventTypes.EXIT, eventObserver2);
        Assert.assertEquals(2, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
        eventHandler.unregister(EventTypes.EXIT, eventObserver1);
        Assert.assertEquals(1, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
        eventHandler.unregister(EventTypes.EXIT, eventObserver2);
        Assert.assertEquals(0, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
    }

    /**
     * Test adding and removing different observers with different event types
     */
    @Test
    public void eventAddTwoObserversDifferentEventTest() {
        EventHandler eventHandler = new EventHandler();
        IEventObserver eventObserver1 = new InputService(this.writerHandler, eventHandler);
        IEventObserver eventObserver2 = new InputService(this.writerHandler, eventHandler);
        eventHandler.register(EventTypes.EXIT, eventObserver1);
        eventHandler.register(EventTypes.WRITE_OUTPUT, eventObserver2);
        System.out.println(eventHandler.getAllObservers());
        Assert.assertEquals(1, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
        Assert.assertEquals(1, eventHandler.getObserversForEvent(EventTypes.WRITE_OUTPUT).size());
        eventHandler.unregister(EventTypes.EXIT, eventObserver1);
        Assert.assertEquals(0, eventHandler.getObserversForEvent(EventTypes.EXIT).size());
        eventHandler.unregister(EventTypes.WRITE_OUTPUT, eventObserver2);
        Assert.assertEquals(0, eventHandler.getObserversForEvent(EventTypes.WRITE_OUTPUT).size());
    }

    @AfterMethod
    public void destroy() {
        this.writerHandler = null;
    }
}
