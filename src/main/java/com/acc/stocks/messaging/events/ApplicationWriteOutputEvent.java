package com.acc.stocks.messaging.events;

public class ApplicationWriteOutputEvent extends ApplicationEventBase {
    private String message;

    public ApplicationWriteOutputEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApplicationWriteOutputEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
