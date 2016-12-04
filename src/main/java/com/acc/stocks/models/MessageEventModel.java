package com.acc.stocks.models;

import com.acc.stocks.models.enums.EventTypes;

public class MessageEventModel {
    private EventTypes eventType;
    private Object message;

    public MessageEventModel() {

    }

    public MessageEventModel(EventTypes eventType, Object message) {
        this.eventType = eventType;
        this.message = message;
    }

    public EventTypes getEventType() {
        return eventType;
    }

    public void setEventType(EventTypes eventType) {
        this.eventType = eventType;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageEventModel{" +
                "eventType=" + eventType +
                ", message=" + message +
                '}';
    }
}
