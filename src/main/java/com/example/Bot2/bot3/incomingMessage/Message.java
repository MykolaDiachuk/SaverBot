package com.example.Bot2.bot3.incomingMessage;

import com.example.Bot2.bot3.resource.DialogService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Message implements Updates {
   private  DialogService dialogService;
    @Override
    public void Handler(Update update) {
        if(update.getMessage().hasText()){
            dialogService.forwardMessage(update.getMessage().getChatId(), update.getMessage().getChatId(),
                   update.getMessage().getMessageId());
            System.out.println("message");
        }
    }
}
