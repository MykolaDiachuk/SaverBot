package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.Config.BotConfig;
import com.example.Bot2.bot3.resource.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Message implements Updates {
    @Autowired
    DialogService dialogService = new DialogService();


    @Override
    public void Handler(Update update) {
        if (update.getMessage().hasText()) {
            dialogService.forwardMessage(update.getMessage().getChatId(), update.getMessage().getChatId(),
                    update.getMessage().getMessageId());
            System.out.println("message");
        }
    }
}
