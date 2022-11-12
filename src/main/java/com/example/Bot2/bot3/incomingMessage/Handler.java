package com.example.Bot2.bot3.incomingMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    public void handle(Update update);
}
