package com.example.Bot2.bot3.incomingMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

public class IncomingMessage {
    Updates updates;

    public void setUpdates(Updates updates) {
        this.updates = updates;
    }

    public void executeUpdate(Update update){
        updates.Handler(update);
    }
}
