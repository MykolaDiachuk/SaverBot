package com.example.Bot2.bot3.incomingMessage;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Entity implements Updates {
    @Override
    public void Handler(Update update) {
        if(update.getMessage().hasEntities()){
            System.out.println("Ent");
        }

    }
}
