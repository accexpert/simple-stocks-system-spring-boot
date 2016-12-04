package com.acc.stocks.events;

import com.acc.stocks.models.MessageEventModel;

public interface IEventObserver {
    void notifyEvent(MessageEventModel event);
}
