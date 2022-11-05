package com.example.Bot2.bot3.incomingMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Document implements Updates {
    @Override
    public void Handler(Update update) {
        if(update.getMessage().hasDocument()){
            System.out.println("doc");
        }
    }
}
