package com.acc.stocks.events;

import com.acc.stocks.models.MessageEventModel;
import com.acc.stocks.models.enums.EventTypes;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventHandler implements IEventHandler {
    private static final Logger LOGGER = Logger.getLogger(EventHandler.class);
    private Map<EventTypes, Set<IEventObserver>> clientList;

    public EventHandler() {
        this.clientList = new ConcurrentHashMap<>();
        LOGGER.info(this.getClass().getSimpleName()+" created.");
    }

    @Override
    public void register(EventTypes eventsType, IEventObserver observer) {
        this.clientList.computeIfAbsent(eventsType, key -> new HashSet()).add(observer);
    }

    @Override
    public void unregister(EventTypes eventsType, IEventObserver observer) {
        this.clientList.getOrDefault(eventsType, new HashSet<>()).remove(observer);
    }

    @Override
    public void publish(MessageEventModel message) {
        this.clientList.entrySet().parallelStream().filter(map -> map.getKey().equals(message.getEventType())).forEach( (key) -> key.getValue().forEach(vl -> vl.notifyEvent(message)) );
    }

    @Override
    public Set<IEventObserver> getObserversForEvent(EventTypes event) {
        return this.clientList.getOrDefault(event, Collections.emptySet());
    }

    @Override
    public Map<EventTypes, Set<IEventObserver>> getAllObservers() {
        return this.clientList;
    }
}
