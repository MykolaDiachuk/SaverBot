package com.example.body.botlogic.resource;

import com.example.body.Config.BotConfig;

import com.example.body.botlogic.incomingMessage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class TelegramBotResource extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final MessageHandler messageHandler;

    private final EntityHandler entityHandler;

    private final CallbackQueryHandler callbackQueryHandler;

    private final ManyOtherHandlers manyOtherHandlers;

    @Autowired
    @Lazy
    public TelegramBotResource(BotConfig botConfig, MessageHandler messageHandler, EntityHandler entityHandler,
                               CallbackQueryHandler callbackQueryHandler,  ManyOtherHandlers manyOtherHandlers) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;
        this.entityHandler = entityHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.manyOtherHandlers = manyOtherHandlers;
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                if (update.getMessage().hasEntities())
                    entityHandler.handle(update);
                else messageHandler.handle(update);
            }else manyOtherHandlers.handle(update);
        } else callbackQueryHandler.handle(update);

    }


}
