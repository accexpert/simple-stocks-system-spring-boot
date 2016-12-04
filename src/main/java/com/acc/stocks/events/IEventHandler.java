package com.acc.stocks.events;

import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.enums.EventTypes;

import java.util.Map;
import java.util.Set;

public interface IEventHandler {
    void register(EventTypes eventsType, IEventObserver observer);
    void unregister(EventTypes eventsType, IEventObserver observer);
    void publish(MessageEventModel message);
    Set<IEventObserver> getObserversForEvent(EventTypes event);
    Map<EventTypes, Set<IEventObserver>> getAllObservers();
}
