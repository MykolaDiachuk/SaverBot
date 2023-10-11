package com.example.body.botlogic.incomingMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void handle(Update update);
}
